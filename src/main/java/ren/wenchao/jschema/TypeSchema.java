package ren.wenchao.jschema;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TypeSchema extends JsonProperties implements Serializable {

    private final SchemaType type;
    static final JsonFactory FACTORY = new JsonFactory();
    static final ObjectMapper MAPPER = new ObjectMapper(FACTORY);
    private static final int NO_HASHCODE = Integer.MIN_VALUE;
    private static final Set<String> SCHEMA_RESERVED = new HashSet<>(Arrays.asList("doc", "fields", "items", "name", "namespace", "size", "symbols", "values", "type", "aliases"));

    private static final Set<String> ENUM_RESERVED = new HashSet<>(SCHEMA_RESERVED);

    static {
        ENUM_RESERVED.add("default");

        FACTORY.enable(JsonParser.Feature.ALLOW_COMMENTS);
        FACTORY.setCodec(MAPPER);
    }

    TypeSchema(SchemaType type) {
        super(type == SchemaType.ENUM ? ENUM_RESERVED : SCHEMA_RESERVED);
        this.type = type;
    }

    //todo 可以使用classValue来优化，Use ClassValue to avoid locking classloaders and is GC and thread safe.
    private static final LoadingCache<Type, TypeSchema> typeSchemaCache = CacheBuilder.newBuilder().build(new CacheLoader<Type, TypeSchema>() {
        @Override
        public TypeSchema load(Type key) throws Exception {
            return getSchema0(key);
        }
    });

    public static String getSchema(Type type) {
        try {
            return typeSchemaCache.get(type).toString();
        } catch (Exception e) {
            throw (e instanceof SchemaRuntimeException) ? (SchemaRuntimeException) e : new SchemaRuntimeException(e);
        }
    }

    private static TypeSchema getSchema0(Type type) {
        if (type instanceof Class && CharSequence.class.isAssignableFrom((Class) type)) {
            return create(SchemaType.STRING);
        } else if (type == ByteBuffer.class) {
            return create(SchemaType.BYTES);
        } else if ((type == Integer.class) || (type == Integer.TYPE)) {
            return create(SchemaType.INT);
        } else if ((type == Long.class) || (type == Long.TYPE)) {
            return create(SchemaType.LONG);
        } else if ((type == Float.class) || (type == Float.TYPE)) {
            return create(SchemaType.FLOAT);
        } else if ((type == Double.class) || (type == Double.TYPE)) {
            return create(SchemaType.DOUBLE);
        } else if ((type == Boolean.class) || (type == Boolean.TYPE)) {
            return create(SchemaType.BOOLEAN);
        } else if ((type == Void.class) || (type == Void.TYPE)) {
            return create(SchemaType.NULL);
        } else if (type instanceof ParameterizedType) {

        } else if (type instanceof Class) {
        }

        throw new SchemaRuntimeException("Unknown type: " + type);
    }

    static TypeSchema create(SchemaType type) {
        switch (type) {
            case STRING:
                return new StringSchema();
            case BYTES:
                return new BytesSchema();
            case INT:
                return new IntSchema();
            case LONG:
                return new LongSchema();
            case FLOAT:
                return new FloatSchema();
            case DOUBLE:
                return new DoubleSchema();
            case BOOLEAN:
                return new BooleanSchema();
            case NULL:
                return new NullSchema();
            default:
                throw new SchemaRuntimeException("Can't create a: " + type);
        }
    }

    public String getName() {
        return type.getName();
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
            JsonGenerator gen = FACTORY.createGenerator(writer);
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
