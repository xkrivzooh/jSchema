package ren.wenchao.jschema.constraints;

import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class NotNull implements Constraint {
    private final String message;

    public NotNull() {
        this.message = "";
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
}
