package ren.wenchao.jschema.constraints;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import java.io.IOException;
import java.math.BigDecimal;

public class Min implements Constraint {

    private final BigDecimal minValue;
    private final String message;

    public Min(String maxValue) {
        this(maxValue, "");
    }

    public Min(String value, String message) {
        BigDecimal minValue1;
        try {
            minValue1 = new BigDecimal(value);
        } catch (Exception e) {
            minValue1 = null;
        }
        this.minValue = minValue1;
        this.message = message;
    }

    public static Constraint resolve(JsonNode value) {
        Preconditions.checkArgument((value != null) && (!value.isNull()));
        String minValue = Constraint.safeGetTextValue(value, "minValue");
        String message = Constraint.safeGetTextValue(value, "message");
        return new Min(minValue, message);
    }


    @Override
    public void toJson(JsonGenerator gen) throws IOException {
        gen.writeFieldName("Min");
        gen.writeStartObject();
        gen.writeStringField("minValue", minValue.toString());
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
            return new BigDecimal(valueNode.toString()).compareTo(minValue) != -1;
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
            return new BigDecimal(value.toString()).compareTo(minValue) != -1;
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
            return String.format("必须是数字，且值必须大于等于%s", minValue.toString());
        }
        return message;
    }
}
