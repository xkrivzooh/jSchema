package ren.wenchao.jschema.constraints;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public interface Constraint {

    void toJson(JsonGenerator gen) throws IOException;

    boolean validate(JsonNode valueNode);

    boolean validate(Object value);

    String validateFieldMessage();

    static String safeGetTextValue(JsonNode node, String key) {
        if (node == null) {
            return "";
        }
        JsonNode value = node.get(key);
        if (value == null) {
            return "";
        }
        return value.asText();
    }
}
