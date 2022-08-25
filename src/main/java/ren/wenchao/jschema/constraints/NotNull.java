package ren.wenchao.jschema.constraints;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import java.io.IOException;

public class NotNull implements Constraint {
    private final String message;

    public NotNull() {
        this.message = "值不能为null";
    }

    public NotNull(String message) {
        this.message = message;
    }

    public static Constraint resolve(JsonNode value) {
        Preconditions.checkArgument((value != null) && (!value.isNull()));
        String message = Constraint.safeGetTextValue(value, "message");
        return new NotNull(message);
    }

    @Override
    public void toJson(JsonGenerator gen) throws IOException {
        gen.writeFieldName("NotNull");
        gen.writeStartObject();
        gen.writeStringField("message", message);
        gen.writeEndObject();
    }

    @Override
    public boolean validate(JsonNode valueNode) {
        return (valueNode != null) && (!valueNode.isNull());
    }

    @Override
    public boolean validate(Object value) {
        return value != null;
    }

    @Override
    public String validateFieldMessage() {
        return Strings.nullToEmpty(message);
    }
}
