package ren.wenchao.jschema.constraints;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Preconditions;

import java.io.IOException;

public class AssertFalse implements Constraint{
    private final String message;

    public AssertFalse() {
        this("值必须是false");
    }

    public AssertFalse(String message) {
        this.message = message;
    }

    public static Constraint resolve(JsonNode value) {
        Preconditions.checkArgument((value != null) && (!value.isNull()));
        String message = Constraint.safeGetTextValue(value, "message");
        return new AssertFalse(message);
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
        //null values are valid
        if (valueNode == null || valueNode.isNull()) {
            return true;
        }
        if (!valueNode.isBoolean()) {
            return false;
        }
        return !valueNode.booleanValue();
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
        return !((boolean) value);
    }

    @Override
    public String validateFieldMessage() {
        return message;
    }
}
