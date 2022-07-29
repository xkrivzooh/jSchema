package ren.wenchao.jschema.constraints;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class AssertFalse implements Constraint{
    private final String message;

    public AssertFalse() {
        this("值必须是false");
    }

    public AssertFalse(String message) {
        this.message = message;
    }

    @Override
    public void toJson(JsonGenerator gen) throws IOException {
        gen.writeFieldName("AssertFalse");
        gen.writeStartObject();
        gen.writeStringField("message", message);
        gen.writeEndObject();
    }

    @Override
    public boolean validate(JsonNode valueNode) {
        if (valueNode == null) {
            return false;
        }
        if (!valueNode.isBoolean()) {
            return false;
        }
        return !valueNode.booleanValue();
    }

    @Override
    public boolean validate(Object value) {
        if (value == null) {
            return false;
        }
        if (!boolean.class.isAssignableFrom(value.getClass())) {
            return false;
        }
        return !((boolean) value);
    }

    @Override
    public String validateFieldMessage() {
        return message;
    }
}
