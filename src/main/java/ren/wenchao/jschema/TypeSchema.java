package ren.wenchao.jschema;

import com.fasterxml.jackson.core.JsonGenerator;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class TypeSchema extends JsonProperties implements Serializable {

    private final SchemaType type;

    int hashCode = NO_HASHCODE;
    protected static final int NO_HASHCODE = Integer.MIN_VALUE;
    static final String NULL_ABLE_PROP = "nullable";
    static final String CLASS_PROP = "java-class";
    static final String KEY_CLASS_PROP = "java-key-class";

    private static final String STRING_OUTER_PARENT_REFERENCE = "this$0";
    static final Set<String> SCHEMA_RESERVED = new HashSet<>(Arrays.asList("doc", "fields", "items", "name", "namespace", "size", "symbols", "values", "type", "aliases", "types", "keys"));

    static final Set<String> FIELD_RESERVED = Collections
            .unmodifiableSet(new HashSet<>(Arrays.asList("default", "doc", "name", "order", "type", "aliases", "types", "keys")));
    static final Set<String> ENUM_RESERVED = new HashSet<>(SCHEMA_RESERVED);

    static {
        ENUM_RESERVED.add("default");
    }

    /**
     * Read/write some common builtin classes as strings. Representing these as
     * strings isn't always best, as they aren't always ordered ideally, but at
     * least they're stored. Also note that, for compatibility, only classes that
     * wouldn't be otherwise correctly readable or writable should be added here,
     * e.g., those without a no-arg constructor or those whose fields are all
     * transient.
     */
    private static Set<Class> stringableClasses = new HashSet<>(Arrays.asList(java.math.BigDecimal.class, java.math.BigInteger.class, java.net.URI.class, java.net.URL.class, java.io.File.class));


    TypeSchema(SchemaType type) {
        super(type == SchemaType.ENUM ? ENUM_RESERVED : SCHEMA_RESERVED);
        this.type = type;
    }

    private static final LoadingCache<Type, TypeSchema> typeSchemaCache = CacheBuilder.newBuilder().build(new CacheLoader<Type, TypeSchema>() {
        @Override
        public TypeSchema load(Type key) throws Exception {
            return createSchema(key, Maps.newHashMap());
        }
    });

    public static String getSchemaString(Type type) {
        return getSchema(type).toString();
    }

    public static TypeSchema getSchema(Type type) {
        try {
            return typeSchemaCache.get(type);
        } catch (Exception e) {
            throw (e instanceof SchemaRuntimeException) ? (SchemaRuntimeException) e : new SchemaRuntimeException(e);
        }
    }


    private static TypeSchema createSchema(Type type, Map<String, TypeSchema> names) {
        if (type instanceof GenericArrayType) { // generic array
            Type component = ((GenericArrayType) type).getGenericComponentType();
            if (component == Byte.TYPE) {// byte array
                return TypeSchema.create(SchemaType.BYTES);
            }
            return TypeSchema.createArray(createSchema(component, names));
        } else if (type instanceof ParameterizedType) {
            ParameterizedType ptype = (ParameterizedType) type;
            Class raw = (Class) ptype.getRawType();
            Type[] params = ptype.getActualTypeArguments();
            if (Map.class.isAssignableFrom(raw)) { // Map
                Class key = (Class) params[0];
                if (isStringable(key)) { // Stringable key
                    TypeSchema schema = TypeSchema.createMap(createSchema(params[0], names), createSchema(params[1], names));
                    schema.addProp(KEY_CLASS_PROP, key.getName());
                    return schema;
                }
                TypeSchema keySchema = createSchema(params[0], names);
                TypeSchema valueSchema = createSchema(params[1], names);
                TypeSchema schema = TypeSchema.createMap(keySchema, valueSchema);
                schema.addProp(CLASS_PROP, raw.getName());
                return schema;
            } else if (Collection.class.isAssignableFrom(raw)) { // Collection
                if (params.length != 1) {
                    throw new SchemaRuntimeException("No array type specified.");
                }
                if (List.class.isAssignableFrom(raw)) {
                    TypeSchema schema = TypeSchema.createList(createSchema(params[0], names));
                    schema.addProp(CLASS_PROP, raw.getName());
                    return schema;
                }

                if (Set.class.isAssignableFrom(raw)) {
                    TypeSchema schema = TypeSchema.createSet(createSchema(params[0], names));
                    schema.addProp(CLASS_PROP, raw.getName());
                    return schema;
                }
                TypeSchema schema = TypeSchema.createArray(createSchema(params[0], names));
                schema.addProp(CLASS_PROP, raw.getName());
                return schema;
            }
        } else if ((type == Byte.class) || (type == Byte.TYPE)) {
            boolean primitive = ((Class<?>) type).isPrimitive();
            if (primitive) {
                return TypeSchema.create(SchemaType.BYTE);
            }
            return TypeSchema.create(SchemaType.BYTE_WRAPPER);
        } else if ((type == Short.class) || (type == Short.TYPE)) {
            boolean primitive = ((Class<?>) type).isPrimitive();
            if (primitive) {
                return TypeSchema.create(SchemaType.SHORT);
            }
            return TypeSchema.create(SchemaType.SHORT_WRAPPER);
        } else if ((type == Character.class) || (type == Character.TYPE)) {
            boolean primitive = ((Class<?>) type).isPrimitive();
            if (primitive) {
                return TypeSchema.create(SchemaType.CHAR);
            }
            return TypeSchema.create(SchemaType.CHAR_WRAPPER);
        } else if (type instanceof Class) { // Class
            Class<?> c = (Class<?>) type;
            if (c.isPrimitive() || // primitives
                    c == Void.class || c == Boolean.class || c == Integer.class || c == Long.class || c == Float.class || c == Double.class || c == Byte.class || c == Short.class || c == Character.class)
                return createSchema0(type, names);
            if (c.isArray()) { // array
                Class component = c.getComponentType();
                TypeSchema result = TypeSchema.createArray(createSchema(component, names));
                result.addProp(CLASS_PROP, c.getName());
                return result;
            }
            Schema explicit = c.getAnnotation(Schema.class);
            if (explicit != null) // explicit schema
                return new Parser().parse(explicit.value());
            if (CharSequence.class.isAssignableFrom(c)) // String
                return TypeSchema.create(SchemaType.STRING);
            if (ByteBuffer.class.isAssignableFrom(c)) // bytes
                return TypeSchema.create(SchemaType.BYTES);
            if (Collection.class.isAssignableFrom(c)) {
                // array
                throw new SchemaRuntimeException("Can't find element type of Collection");
            }
            String fullName = c.getName();
            TypeSchema schema = names.get(fullName);
            if (schema == null) {
                Doc annotatedDoc = c.getAnnotation(Doc.class); // Docstring
                String doc = (annotatedDoc != null) ? annotatedDoc.value() : null;
                String name = c.getSimpleName();
                String space = c.getPackage() == null ? "" : c.getPackage().getName();
                if (c.getEnclosingClass() != null) { // nested class
                    space = c.getEnclosingClass().getName();
                }
                if (isStringable(c)) { // Stringable
                    TypeSchema result = TypeSchema.create(SchemaType.STRING);
                    result.addProp(CLASS_PROP, c.getName());
                    return result;
                } else if (c.isEnum()) { // Enum
                    List<String> symbols = new ArrayList<>();
                    Enum[] constants = (Enum[]) c.getEnumConstants();
                    for (Enum constant : constants)
                        symbols.add(constant.name());
                    schema = TypeSchema.createEnum(name, doc, space, symbols);
                    consumeAliasAnnotation(c, schema);
                } else { // record
                    List<Field> fields = new ArrayList<>();
                    boolean error = Throwable.class.isAssignableFrom(c);
                    schema = TypeSchema.createRecord(name, doc, space, error);
                    consumeAliasAnnotation(c, schema);
                    names.put(c.getName(), schema);
                    for (java.lang.reflect.Field field : getCachedFields(c))
                        if ((field.getModifiers() & (Modifier.TRANSIENT | Modifier.STATIC)) == 0 && !field.isAnnotationPresent(Ignore.class)) {
                            TypeSchema fieldSchema = createFieldSchema(field, names);
                            annotatedDoc = field.getAnnotation(Doc.class); // Docstring
                            doc = (annotatedDoc != null) ? annotatedDoc.value() : null;

                            Object defaultValue = createSchemaDefaultValue(type, field, fieldSchema);

                            Name annotatedName = field.getAnnotation(Name.class); // Rename fields
                            String fieldName = (annotatedName != null) ? annotatedName.value() : field.getName();
                            if (STRING_OUTER_PARENT_REFERENCE.equals(fieldName)) {
                                throw new SchemaRuntimeException("Class " + fullName + " must be a static inner class");
                            }
                            Field recordField = new Field(fieldName, fieldSchema, doc, defaultValue);

                            Meta[] metadata = field.getAnnotationsByType(Meta.class); // add metadata
                            for (Meta meta : metadata) {
                                if (recordField.getObjectProps().containsKey(meta.key())) {
                                    throw new SchemaRuntimeException("Duplicate field prop key: " + meta.key());
                                }
                                recordField.addProp(meta.key(), meta.value());
                            }

                            if (field.isAnnotationPresent(Nullable.class)) {
                                recordField.addProp(NULL_ABLE_PROP, true);
                            }

                            for (Field f : fields) {
                                if (f.name().equals(fieldName))
                                    throw new SchemaRuntimeException("double field entry: " + fieldName);
                            }

                            consumeFieldAlias(field, recordField);

                            fields.add(recordField);
                        }
                    if (error) {// add Throwable message
                        fields.add(new Field("detailMessage", TypeSchema.create(SchemaType.STRING), null, null));
                    }
                    schema.setFields(fields);
                    Meta[] metadata = c.getAnnotationsByType(Meta.class);
                    for (Meta meta : metadata) {
                        if (schema.getObjectProps().containsKey(meta.key())) {
                            throw new SchemaRuntimeException("Duplicate type prop key: " + meta.key());
                        }
                        schema.addProp(meta.key(), meta.value());
                    }
                }
                names.put(fullName, schema);
            }
            return schema;
        }
        return createSchema0(type, names);
    }

    /**
     * Create the schema for a Java type.
     */
    @SuppressWarnings(value = "unchecked")
    private static TypeSchema createSchema0(java.lang.reflect.Type type, Map<String, TypeSchema> names) {
        if (type instanceof Class && CharSequence.class.isAssignableFrom((Class) type))
            return TypeSchema.create(SchemaType.STRING);
        else if (type == ByteBuffer.class)
            return TypeSchema.create(SchemaType.BYTES);
        else if ((type == Integer.class) || (type == Integer.TYPE)) {
            boolean primitive = ((Class<?>) Objects.requireNonNull(type)).isPrimitive();
            if (primitive) {
                return TypeSchema.create(SchemaType.INT);
            }
            return TypeSchema.create(SchemaType.INT_WRAPPER);
        } else if ((type == Long.class) || (type == Long.TYPE)) {
            boolean primitive = ((Class<?>) Objects.requireNonNull(type)).isPrimitive();
            if (primitive) {
                return TypeSchema.create(SchemaType.LONG);
            }
            return TypeSchema.create(SchemaType.LONG_WRAPPER);
        } else if ((type == Float.class) || (type == Float.TYPE)) {
            boolean primitive = ((Class<?>) Objects.requireNonNull(type)).isPrimitive();
            if (primitive) {
                return TypeSchema.create(SchemaType.FLOAT);
            }
            return TypeSchema.create(SchemaType.FLOAT_WRAPPER);
        } else if ((type == Double.class) || (type == Double.TYPE)) {
            boolean primitive = ((Class<?>) Objects.requireNonNull(type)).isPrimitive();
            if (primitive) {
                return TypeSchema.create(SchemaType.DOUBLE);
            }
            return TypeSchema.create(SchemaType.DOUBLE_WRAPPER);
        } else if ((type == Boolean.class) || (type == Boolean.TYPE)) {
            boolean primitive = ((Class<?>) Objects.requireNonNull(type)).isPrimitive();
            if (primitive) {
                return TypeSchema.create(SchemaType.BOOLEAN);
            }
            return TypeSchema.create(SchemaType.BOOLEAN_WRAPPER);
        } else if ((type == Void.class) || (type == Void.TYPE))
            return TypeSchema.create(SchemaType.VOID);
        else if (type instanceof ParameterizedType) {
            ParameterizedType ptype = (ParameterizedType) type;
            Class raw = (Class) ptype.getRawType();
            java.lang.reflect.Type[] params = ptype.getActualTypeArguments();
            if (Collection.class.isAssignableFrom(raw)) { // array
                if (params.length != 1)
                    throw new SchemaRuntimeException("No array type specified.");
                return TypeSchema.createArray(createSchema0(params[0], names));
            } else if (Map.class.isAssignableFrom(raw)) { // map
                java.lang.reflect.Type key = params[0];
                java.lang.reflect.Type value = params[1];
                if (!(key instanceof Class && CharSequence.class.isAssignableFrom((Class) key)))
                    throw new SchemaRuntimeException("Map key class not CharSequence: " + key);
                return TypeSchema.createMap(createSchema0(key, names), createSchema0(value, names));
            } else {
                return createSchema0(raw, names);
            }
        } else if (type instanceof Class) { // class
            Class c = (Class) type;
            String fullName = c.getName();
            TypeSchema schema = names.get(fullName);
            if (schema == null)
                try {
                    schema = (TypeSchema) (c.getDeclaredField("SCHEMA$").get(null));

                    if (!fullName.equals(getClassName(schema)))
                        // HACK: schema mismatches class. maven shade plugin? try replacing.
                        schema = new Parser()
                                .parse(schema.toString().replace(schema.getNamespace(), c.getPackage().getName()));
                } catch (NoSuchFieldException e) {
                    throw new SchemaRuntimeException("Not a Specific class: " + c);
                } catch (IllegalAccessException e) {
                    throw new SchemaRuntimeException(e);
                }
            names.put(fullName, schema);
            return schema;
        }
        throw new SchemaRuntimeException("Unknown type: " + type);
    }

    static TypeSchema create(SchemaType type) {
        switch (type) {
            case BYTE:
                return new ByteSchema();
            case BYTE_WRAPPER:
                return new ByteWrapperSchema();
            case BYTES:
                return new BytesSchema();
            case SHORT:
                return new ShortSchema();
            case SHORT_WRAPPER:
                return new ShortWrapperSchema();
            case INT:
                return new IntSchema();
            case INT_WRAPPER:
                return new IntWrapperSchema();
            case LONG:
                return new LongSchema();
            case LONG_WRAPPER:
                return new LongWrapperSchema();
            case FLOAT:
                return new FloatSchema();
            case FLOAT_WRAPPER:
                return new FloatWrapperSchema();
            case DOUBLE:
                return new DoubleSchema();
            case DOUBLE_WRAPPER:
                return new DoubleWrapperSchema();
            case BOOLEAN:
                return new BooleanSchema();
            case BOOLEAN_WRAPPER:
                return new BooleanWrapperSchema();
            case CHAR:
                return new CharSchema();
            case CHAR_WRAPPER:
                return new CharWrapperSchema();
            case STRING:
                return new StringSchema();
            case VOID:
                return new VoidSchema();
            case NULL:
                return new NullSchema();
            default:
                throw new SchemaRuntimeException("Can't create a: " + type);
        }
    }

    /**
     * Returns the Java class name indicated by a schema's name and namespace.
     */
    public static String getClassName(TypeSchema schema) {
        String namespace = schema.getNamespace();
        String name = schema.getName();
        if (namespace == null || "".equals(namespace))
            return name;
        String dot = namespace.endsWith("$") ? "" : "."; // back-compatibly handle $
        return namespace + dot + name;
    }


    private static final Map<Class<?>, java.lang.reflect.Field[]> FIELDS_CACHE = new ConcurrentHashMap<>();

    // Return of this class and its superclasses to serialize.
    private static java.lang.reflect.Field[] getCachedFields(Class<?> recordClass) {
        return FIELDS_CACHE.computeIfAbsent(recordClass, rc -> getFields(rc, true));
    }

    private static java.lang.reflect.Field[] getFields(Class<?> recordClass, boolean excludeJava) {
        java.lang.reflect.Field[] fieldsList;
        Map<String, java.lang.reflect.Field> fields = new LinkedHashMap<>();
        Class<?> c = recordClass;
        do {
            if (excludeJava && c.getPackage() != null && c.getPackage().getName().startsWith("java."))
                break; // skip java built-in classes
            java.lang.reflect.Field[] declaredFields = c.getDeclaredFields();
            Arrays.sort(declaredFields, Comparator.comparing(java.lang.reflect.Field::getName));
            for (java.lang.reflect.Field field : declaredFields)
                if ((field.getModifiers() & (Modifier.TRANSIENT | Modifier.STATIC)) == 0)
                    if (fields.put(field.getName(), field) != null)
                        throw new SchemaRuntimeException(c + " contains two fields named: " + field);
            c = c.getSuperclass();
        } while (c != null);
        fieldsList = fields.values().toArray(new java.lang.reflect.Field[0]);
        return fieldsList;
    }

    //True if a class should be serialized with toString().
    private static boolean isStringable(Class<?> c) {
        return c.isAnnotationPresent(Stringable.class) || stringableClasses.contains(c);
    }

    /**
     * If this is a record, enum, add an alias.
     */
    public void addAlias(String alias) {
        throw new SchemaRuntimeException("Not a named type: " + this);
    }

    /**
     * If this is a record, enum, add an alias.
     */
    public void addAlias(String alias, String space) {
        throw new SchemaRuntimeException("Not a named type: " + this);
    }

    /**
     * If this is a record, enum, return its aliases, if any.
     */
    public Set<String> getAliases() {
        throw new SchemaRuntimeException("Not a named type: " + this);
    }


    /**
     * Returns true if this record is an error type.
     */
    public boolean isError() {
        throw new SchemaRuntimeException("Not a record: " + this);
    }

    /**
     * If this is a record, set its fields. The fields can be set only once in a
     * schema.
     */
    public void setFields(List<Field> fields) {
        throw new SchemaRuntimeException("Not a record: " + this);
    }

    void fieldsToJson(Names names, JsonGenerator gen) throws IOException {
        throw new SchemaRuntimeException("Not a record: " + this);
    }

    public String getName() {
        return type.getName();
    }

    /**
     * If this is a record, enum, returns its namespace, if any.
     */
    public String getNamespace() {
        throw new SchemaRuntimeException("Not a named type: " + this);
    }

    /**
     * If this is an enum, return its symbols.
     */
    public List<String> getEnumSymbols() {
        throw new SchemaRuntimeException("Not an enum: " + this);
    }

    /**
     * If this is an enum, return its default value.
     */
    public String getEnumDefault() {
        throw new SchemaRuntimeException("Not an enum: " + this);
    }

    /**
     * If this is an enum, return a symbol's ordinal value.
     */
    public int getEnumOrdinal(String symbol) {
        throw new SchemaRuntimeException("Not an enum: " + this);
    }

    /**
     * If this is an enum, returns true if it contains given symbol.
     */
    public boolean hasEnumSymbol(String symbol) {
        throw new SchemaRuntimeException("Not an enum: " + this);
    }


    /**
     * If this is a record, enum, returns its docstring, if available.
     * Otherwise, returns null.
     */
    public String getDoc() {
        return null;
    }

    /**
     * If this is a record, enum, returns its namespace-qualified name,
     * otherwise returns the name of the primitive type.
     */
    public String getFullName() {
        return getName();
    }

    /**
     * Return the type of this schema.
     */
    public SchemaType getType() {
        return type;
    }

    int computeHash() {
        return getType().hashCode() + propsHashCode();
    }

    final boolean equalCachedHash(TypeSchema other) {
        return (hashCode == other.hashCode) || (hashCode == NO_HASHCODE) || (other.hashCode == NO_HASHCODE);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TypeSchema)) {
            return false;
        }
        TypeSchema that = (TypeSchema) o;
        if (!(this.type == that.type)) return false;
        return equalCachedHash(that) && propsEqual(that);
    }

    @Override
    public final int hashCode() {
        if (hashCode == NO_HASHCODE) {
            hashCode = computeHash();
        }
        return hashCode;
    }

    /**
     * If this is a union, return the branch with the provided full name.
     */
    public Integer getIndexNamed(String name) {
        throw new SchemaRuntimeException("Not a union: " + this);
    }

    /**
     * Create an enum schema.
     */
    public static TypeSchema createEnum(String name, String doc, String namespace, List<String> values) {
        return new EnumSchema(new NameWrapper(name, namespace), doc, new LockableArrayList<>(values), null);
    }

    /**
     * Create an enum schema.
     */
    public static TypeSchema createEnum(String name, String doc, String namespace, List<String> values, String enumDefault) {
        return new EnumSchema(new NameWrapper(name, namespace), doc, new LockableArrayList<>(values), enumDefault);
    }

    /**
     * Create a named record schema.
     */
    public static TypeSchema createRecord(String name, String doc, String namespace, boolean isError) {
        return new RecordSchema(new NameWrapper(name, namespace), doc, isError);
    }

    /**
     * Create a named record schema with fields already set.
     */
    public static TypeSchema createRecord(String name, String doc, String namespace, boolean isError, List<Field> fields) {
        return new RecordSchema(new NameWrapper(name, namespace), doc, isError, fields);
    }

    /**
     * Create a schema for a field.
     */
    protected static TypeSchema createFieldSchema(java.lang.reflect.Field field, Map<String, TypeSchema> names) {
        //todo暂不支持AvroEncode
//        AvroEncode enc = field.getAnnotation(AvroEncode.class);
//        if (enc != null)
//            try {
//                return enc.using().getDeclaredConstructor().newInstance().getSchema();
//            } catch (Exception e) {
//                throw new AvroRuntimeException("Could not create schema from custom serializer for " + field.getName());
//            }

        Schema explicit = field.getAnnotation(Schema.class);
        if (explicit != null) // explicit schema
            return new Parser().parse(explicit.value());

        TypeSchema schema = createSchema(field.getGenericType(), names);
        if (field.isAnnotationPresent(Stringable.class)) { // Stringable
            schema = TypeSchema.create(SchemaType.STRING);
        }
        return schema;
    }

    private static void consumeFieldAlias(java.lang.reflect.Field field, Field recordField) {
        Alias[] aliases = field.getAnnotationsByType(Alias.class);
        for (Alias alias : aliases) {
            if (!alias.space().equals(Alias.NULL)) {
                throw new SchemaRuntimeException(
                        "Namespaces are not allowed on field aliases. " + "Offending field: " + recordField.name());
            }
            recordField.addAlias(alias.alias());
        }
    }


    private static void consumeAliasAnnotation(Class<?> c, TypeSchema schema) {
        Alias[] aliases = c.getAnnotationsByType(Alias.class);
        for (Alias alias : aliases) {
            String space = alias.space();
            if (Alias.NULL.equals(space))
                space = null;
            schema.addAlias(alias.alias(), space);
        }
    }


    /**
     * If this is a record, returns the Field with the given name
     * <tt>fieldName</tt>. If there is no field by that name, a <tt>null</tt> is
     * returned.
     */
    public Field getField(String fieldname) {
        throw new SchemaRuntimeException("Not a record: " + this);
    }

    /**
     * If this is an array, returns its element type.
     */
    public TypeSchema getElementType() {
        throw new SchemaRuntimeException("Not an array: " + this);
    }

    /**
     * If this is a map, returns its key type.
     */
    public TypeSchema getKeyType() {
        throw new SchemaRuntimeException("Not a map: " + this);
    }

    /**
     * If this is a map, returns its value type.
     */
    public TypeSchema getValueType() {
        throw new SchemaRuntimeException("Not a map: " + this);
    }

    /**
     * If this is a union, returns its types.
     */
    public List<TypeSchema> getTypes() {
        throw new SchemaRuntimeException("Not a union: " + this);
    }


    /**
     * If this is a record, returns the fields in it. The returned list is in the
     * order of their positions.
     */
    public List<Field> getFields() {
        throw new SchemaRuntimeException("Not a record: " + this);
    }

    /**
     * Create an array schema.
     */
    public static TypeSchema createArray(TypeSchema elementType) {
        return new ArraySchema(elementType);
    }

    /**
     * Create an list schema
     */
    public static TypeSchema createList(TypeSchema elementType) {
        return new ListSchema(elementType);
    }

    /**
     * Create an set schema
     */
    public static TypeSchema createSet(TypeSchema elementType) {
        return new SetSchema(elementType);
    }

    /**
     * Create a map schema.
     */
    public static TypeSchema createMap(TypeSchema keyType, TypeSchema valueType) {
        return new MapSchema(keyType, valueType);
    }

    /**
     * Get default value for a schema field. Derived classes can override this
     * method to provide values based on object instantiation
     *
     * @param type        Type
     * @param field       Field
     * @param fieldSchema Schema of the field
     * @return The default value
     */
    private static Object createSchemaDefaultValue(Type type, java.lang.reflect.Field field, TypeSchema fieldSchema) {
        Object defaultValue;
//        if (defaultGenerated) {
//            defaultValue = getOrCreateDefaultValue(type, field);
//            if (defaultValue != null) {
//                return deepCopy(fieldSchema, defaultValue);
//            }
//            // if we can't get the default value, try to use previous code below
//        }

        Default defaultAnnotation = field.getAnnotation(Default.class);
        defaultValue = (defaultAnnotation == null) ? null : JacksonUtils.parseJsonToObject(defaultAnnotation.value());
        return defaultValue;
    }


    @Override
    public String toString() {
        return toString(false);
    }

    public String toString(boolean pretty) {
        return toString(new Names(), pretty);
    }

    String toString(Names names, boolean pretty) {
        try {
            StringWriter writer = new StringWriter();
            JsonGenerator gen = JacksonUtils.getFactory().createGenerator(writer);
            if (pretty) {
                gen.useDefaultPrettyPrinter();
            }
            toJson(names, gen);
            gen.flush();
            return writer.toString();
        } catch (IOException e) {
            throw new SchemaRuntimeException(e);
        }
    }

    void toJson(Names names, JsonGenerator gen) throws IOException {
        if (!hasProps()) { // no props defined
            gen.writeString(getName()); // just write name
        } else {
            gen.writeStartObject();
            gen.writeStringField("type", getName());
            writeProps(gen);
            gen.writeEndObject();
        }
    }
}
