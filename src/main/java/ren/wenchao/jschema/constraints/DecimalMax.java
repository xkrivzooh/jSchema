package ren.wenchao.jschema.constraints;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Strings;

import java.io.IOException;
import java.math.BigDecimal;

public class DecimalMax implements Constraint {
    private final BigDecimal maxValue;
    private final boolean inclusive;
    private final String message;

    public DecimalMax(String value) {
        this(value, true, "");
    }

    public DecimalMax(String value, String message) {
        this(value, true, message);
    }

    public DecimalMax(String value, boolean inclusive, String message) {
        BigDecimal maxValue1;
        try {
            maxValue1 = new BigDecimal(value);
        } catch (Exception e) {
            maxValue1 = null;
        }
        this.maxValue = maxValue1;
        this.inclusive = inclusive;
        this.message = message;
    }

    @Override
    public void toJson(JsonGenerator gen) throws IOException {
        gen.writeFieldName("DecimalMax");
        gen.writeStartObject();
        gen.writeStringField("maxValue", maxValue.toString());
        gen.writeStringField("inclusive", String.valueOf(inclusive));
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
            int comparisonResult = new BigDecimal(valueNode.toString()).compareTo(maxValue);
            return inclusive ? comparisonResult <= 0 : comparisonResult < 0;
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
            int comparisonResult = new BigDecimal(value.toString()).compareTo(maxValue);
            return inclusive ? comparisonResult <= 0 : comparisonResult < 0;
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
            if (inclusive) {
                return String.format("??????????????????????????????????????????%s", maxValue.toString());
            } else {
                return String.format("????????????????????????????????????%s", maxValue.toString());
            }
        }
        return message;
    }
}
