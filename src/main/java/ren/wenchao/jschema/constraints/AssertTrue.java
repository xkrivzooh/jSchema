package ren.wenchao.jschema.constraints;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class AssertTrue implements Constraint {
    private final String message;

    public AssertTrue() {
        this("值必须是true");
    }

    public AssertTrue(String message) {
        this.message = message;
    }

    @Override
    public void toJson(JsonGenerator gen) throws IOException {
        gen.writeFieldName("AssertTrue");
        gen.writeStartObject();
        gen.writeStringField("message", message);
        gen.writeEndObject();
    }

    @Override
    public boolean validate(JsonNode valueNode) {
        //null values are valid
        if (valueNode == null || valueNode.isNull()) {
            return true;
        }
        if (!valueNode.isBoolean()) {
            return false;
        }
        return valueNode.booleanValue();
    }

    @Override
    public boolean validate(Object value) {
        //null values are valid
        if (value == null) {
            return true;
        }
        if (!boolean.class.isAssignableFrom(value.getClass())) {
            return false;
        }
        return (boolean) value;
    }

    @Override
    public String validateFieldMessage() {
        return message;
    }
}
