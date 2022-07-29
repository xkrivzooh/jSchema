package ren.wenchao.jschema.constraints;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class NotNull implements Constraint {
    private final String message;

    public NotNull() {
        this.message = "值不能为null";
    }

    public NotNull(String message) {
        this.message = message;
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
        return valueNode != null;
    }

    @Override
    public boolean validate(Object value) {
        return value != null;
    }

    @Override
    public String validateFieldMessage() {
        return message;
    }
}
