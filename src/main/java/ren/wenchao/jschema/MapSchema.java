package ren.wenchao.jschema;

import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

class MapSchema extends TypeSchema {

    private final TypeSchema keyType;
    private final TypeSchema valueType;

    MapSchema(TypeSchema keyType, TypeSchema valueType) {
        super(SchemaType.MAP);
        this.keyType = keyType;
        this.valueType = valueType;
    }

    @Override
    public TypeSchema getKeyType() {
        return keyType;
    }

    @Override
    public TypeSchema getValueType() {
        return valueType;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof MapSchema)) return false;
        MapSchema that = (MapSchema) o;
        return equalCachedHash(that) && keyType.equals(that.keyType) && valueType.equals(that.valueType) && propsEqual(that);
    }

    @Override
    int computeHash() {
        return super.computeHash() + keyType.computeHash() + valueType.computeHash();
    }

    @Override
    void toJson(Names names, JsonGenerator gen) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("type", "Map");
        gen.writeFieldName("keys");
        keyType.toJson(names, gen);
        gen.writeFieldName("values");
        valueType.toJson(names, gen);
        writeProps(gen);
        gen.writeEndObject();
    }

}
