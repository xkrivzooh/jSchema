package ren.wenchao.jschema;

import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;
import java.util.*;

@SuppressWarnings(value = "unchecked")
class RecordSchema extends NamedSchema {
    private List<Field> fields;
    private Map<String, Field> fieldMap;
    private final boolean isError;

    private static final ThreadLocal<Set> SEEN_EQUALS = ThreadLocal.withInitial(HashSet::new);
    private static final ThreadLocal<Map> SEEN_HASHCODE = ThreadLocal.withInitial(IdentityHashMap::new);


    public RecordSchema(NameWrapper name, String doc, boolean isError) {
        super(SchemaType.RECORD, name, doc);
        this.isError = isError;
    }

    public RecordSchema(NameWrapper name, String doc, boolean isError, List<Field> fields) {
        super(SchemaType.RECORD, name, doc);
        this.isError = isError;
        setFields(fields);
    }

    @Override
    public boolean isError() {
        return isError;
    }

    @Override
    public Field getField(String fieldname) {
        if (fieldMap == null)
            throw new SchemaRuntimeException("Schema fields not set yet");
        return fieldMap.get(fieldname);
    }

    @Override
    public List<Field> getFields() {
        if (fields == null)
            throw new SchemaRuntimeException("Schema fields not set yet");
        return fields;
    }

    @Override
    public void setFields(List<Field> fields) {
        if (this.fields != null) {
            throw new SchemaRuntimeException("Fields are already set");
        }
        int i = 0;
        fieldMap = new HashMap<>(Math.multiplyExact(2, fields.size()));
        LockableArrayList<Field> ff = new LockableArrayList<>(fields.size());
        for (Field f : fields) {
            if (f.pos() != -1) {
                throw new SchemaRuntimeException("Field already used: " + f);
            }
            f.pos(i++);
            final Field existingField = fieldMap.put(f.name(), f);
            if (existingField != null) {
                throw new SchemaRuntimeException(
                        String.format("Duplicate field %s in record %s: %s and %s.", f.name(), name, f, existingField));
            }
            ff.add(f);
        }
        this.fields = ff.lock();
        this.hashCode = NO_HASHCODE;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof RecordSchema))
            return false;
        RecordSchema that = (RecordSchema) o;
        if (!equalCachedHash(that))
            return false;
        if (!equalNames(that))
            return false;
        if (!propsEqual(that))
            return false;
        Set seen = SEEN_EQUALS.get();
        SeenPair here = new SeenPair(this, o);
        if (seen.contains(here))
            return true; // prevent stack overflow
        boolean first = seen.isEmpty();
        try {
            seen.add(here);
            return Objects.equals(fields, that.fields);
        } finally {
            if (first)
                seen.clear();
        }
    }

    @Override
    int computeHash() {
        Map seen = SEEN_HASHCODE.get();
        if (seen.containsKey(this))
            return 0; // prevent stack overflow
        boolean first = seen.isEmpty();
        try {
            seen.put(this, this);
            return super.computeHash() + fields.hashCode();
        } finally {
            if (first)
                seen.clear();
        }
    }

    @Override
    void toJson(Names names, JsonGenerator gen) throws IOException {
        if (writeNameRef(names, gen))
            return;
        String savedSpace = names.space(); // save namespace
        gen.writeStartObject();
        gen.writeStringField("type", isError ? "error" : "record");
        writeName(names, gen);
        names.space(name.getSpace()); // set default namespace

        if (getDoc() != null) {
            gen.writeStringField("doc", getDoc());
        }

        gen.writeFieldName("types");
        fillTypes(names, gen);

        if (fields != null) {
            gen.writeFieldName("fields");
            fieldsToJson(names, gen);
        }

        writeProps(gen);
        aliasesToJson(gen);
        gen.writeEndObject();
        names.space(savedSpace); // restore namespace
    }

    @Override
    void fieldsToJson(Names names, JsonGenerator gen) throws IOException {
        gen.writeStartArray();
        for (Field f : fields) {
            gen.writeStartObject();
            gen.writeStringField("name", f.name());
            gen.writeFieldName("type");
            f.schema().toJson(names, gen);
            if (f.doc() != null)
                gen.writeStringField("doc", f.doc());
            if (f.hasDefaultValue()) {
                gen.writeFieldName("default");
                gen.writeTree(f.defaultValue());
            }
            if (f.order() != Field.Order.ASCENDING)
                gen.writeStringField("order", f.order().getName());
            if (f.aliases() != null && f.aliases().size() != 0) {
                gen.writeFieldName("aliases");
                gen.writeStartArray();
                for (String alias : f.aliases())
                    gen.writeString(alias);
                gen.writeEndArray();
            }
            f.writeProps(gen);
            gen.writeEndObject();
        }
        gen.writeEndArray();
    }

    void fillTypes(Names names, JsonGenerator gen) throws IOException {
        gen.writeStartObject();
        if (fields != null) {
            for (Field field : fields) {
                TypeSchema fieldSchema = field.schema();
                if (fieldSchema.getType() == SchemaType.RECORD) {
                    RecordSchema recordSchema = (RecordSchema) fieldSchema;
                    if (names.get(new NameWrapper(fieldSchema.getName(), fieldSchema.getNamespace())) == null) {
                        gen.writeFieldName(fieldSchema.getFullName());
                        recordSchema.toJson(names, gen);
                    }
                } else if (fieldSchema.getType() == SchemaType.ENUM) {
                    EnumSchema enumSchema = (EnumSchema) fieldSchema;
                    if (names.get(new NameWrapper(fieldSchema.getName(), fieldSchema.getNamespace())) == null) {
                        gen.writeFieldName(fieldSchema.getFullName());
                        enumSchema.toJson(names, gen);
                    }
                }
            }
        }
        gen.writeEndObject();
    }
}
