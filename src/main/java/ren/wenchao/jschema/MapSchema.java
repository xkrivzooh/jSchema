package ren.wenchao.jschema;

import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

class MapSchema extends TypeSchema {

    private final TypeSchema valueType;

    MapSchema(TypeSchema valueType) {
        super(SchemaType.MAP);
        this.valueType = valueType;
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
        return equalCachedHash(that) && valueType.equals(that.valueType) && propsEqual(that);
    }

    @Override
    int computeHash() {
        return super.computeHash() + valueType.computeHash();
    }

    @Override
    void toJson(Names names, JsonGenerator gen) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("type", "map");
        gen.writeFieldName("values");
        valueType.toJson(names, gen);
        writeProps(gen);
        gen.writeEndObject();
    }

}
