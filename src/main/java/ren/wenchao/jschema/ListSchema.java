package ren.wenchao.jschema;

import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

class ListSchema extends TypeSchema {

    private final TypeSchema elementType;

    ListSchema(TypeSchema elementType) {
        super(SchemaType.LIST);
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
        if (!(o instanceof ListSchema)) {
            return false;
        }
        ListSchema that = (ListSchema) o;
        return equalCachedHash(that) && elementType.equals(that.elementType) && propsEqual(that);
    }

    @Override
    int computeHash() {
        return super.computeHash() + elementType.computeHash();
    }

    @Override
    void toJson(Names names, JsonGenerator gen) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("type", "list");
        gen.writeFieldName("items");
        elementType.toJson(names, gen);
        writeProps(gen);
        gen.writeEndObject();
    }
}
