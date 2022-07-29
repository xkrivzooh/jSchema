package ren.wenchao.jschema.constraints;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Strings;

import java.io.IOException;

public class Null implements Constraint {
    private final String message;

    public Null() {
        this.message = "值必须为null";
    }

    public Null(String message) {
        this.message = message;
    }

    @Override
    public void toJson(JsonGenerator gen) throws IOException {
        gen.writeFieldName("Null");
        gen.writeStartObject();
        gen.writeStringField("message", message);
        gen.writeEndObject();
    }

    @Override
    public boolean validate(JsonNode valueNode) {
        return valueNode == null || valueNode.isNull();
    }

    @Override
    public boolean validate(Object value) {
        return value == null;
    }

    @Override
    public String validateFieldMessage() {
        return Strings.nullToEmpty(message);
    }
}
