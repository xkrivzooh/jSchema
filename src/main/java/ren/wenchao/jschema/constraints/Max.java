package ren.wenchao.jschema.constraints;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import java.io.IOException;
import java.math.BigDecimal;

public class Max implements Constraint {
    private final BigDecimal maxValue;
    private final String message;

    public Max(String maxValue) {
        this(maxValue, "");
    }

    public Max(String value, String message) {
        BigDecimal maxValue1;
        try {
            maxValue1 = new BigDecimal(value);
        } catch (Exception e) {
            maxValue1 = null;
        }
        this.maxValue = maxValue1;
        this.message = message;
    }

    public static Constraint resolve(JsonNode value) {
        Preconditions.checkArgument((value != null) && (!value.isNull()));
        String maxValue = Constraint.safeGetTextValue(value, "maxValue");
        String message = Constraint.safeGetTextValue(value, "message");
        return new Max(maxValue, message);
    }

    @Override
    public void toJson(JsonGenerator gen) throws IOException {
        gen.writeFieldName("Max");
        gen.writeStartObject();
        gen.writeStringField("maxValue", maxValue.toString());
        gen.writeStringField("message", getMessage());
        gen.writeEndObject();
    }

    @Override
    public boolean validate(JsonNode valueNode) {
        //null values are valid
        if (valueNode == null || valueNode.isNull()) {
            return true;
        }
        try {
            return new BigDecimal(valueNode.toString()).compareTo(maxValue) != 1;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    @Override
    public boolean validate(Object value) {
        //null values are valid
        if (value == null) {
            return true;
        }
        try {
            return new BigDecimal(value.toString()).compareTo(maxValue) != 1;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    @Override
    public String validateFieldMessage() {
        return getMessage();
    }

    private String getMessage() {
        if (Strings.isNullOrEmpty(message)) {
            return String.format("必须是数字，且值必须小于等于%s", maxValue.toString());
        }
        return message;
    }
}
