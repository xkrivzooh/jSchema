package ren.wenchao.jschema;

import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

class ArraySchema extends TypeSchema{

    private final TypeSchema elementType;

    ArraySchema(TypeSchema elementType) {
        super(SchemaType.ARRAY);
        this.elementType = elementType;
    }

    @Override
    public TypeSchema getElementType() {
        return elementType;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ArraySchema)) {
            return false;
        }
        ArraySchema that = (ArraySchema) o;
        return equalCachedHash(that) && elementType.equals(that.elementType) && propsEqual(that);
    }

    @Override
    int computeHash() {
        return super.computeHash() + elementType.computeHash();
    }

    @Override
    void toJson(Names names, JsonGenerator gen) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("type", "array");
        gen.writeFieldName("items");
        elementType.toJson(names, gen);
        writeProps(gen);
        gen.writeEndObject();
    }
}
