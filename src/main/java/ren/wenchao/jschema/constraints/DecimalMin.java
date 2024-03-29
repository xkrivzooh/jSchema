package ren.wenchao.jschema.constraints;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import java.io.IOException;
import java.math.BigDecimal;

public class DecimalMin implements Constraint {
    private final BigDecimal minValue;
    private final boolean inclusive;
    private final String message;


    public DecimalMin(String value) {
        this(value, true, "");
    }

    public DecimalMin(String value, String message) {
        this(value, true, message);
    }

    public DecimalMin(String value, boolean inclusive, String message) {
        BigDecimal minValue1;
        try {
            minValue1 = new BigDecimal(value);
        } catch (Exception e) {
            minValue1 = null;
        }
        this.minValue = minValue1;
        this.inclusive = inclusive;
        this.message = message;
    }

    public static Constraint resolve(JsonNode value) {
        Preconditions.checkArgument((value != null) && (!value.isNull()));
        String minValue = Constraint.safeGetTextValue(value, "minValue");
        String inclusive = Constraint.safeGetTextValue(value, "inclusive");
        String message = Constraint.safeGetTextValue(value, "message");
        return new DecimalMin(minValue, Boolean.parseBoolean(inclusive), message);
    }

    @Override
    public void toJson(JsonGenerator gen) throws IOException {
        gen.writeFieldName("DecimalMin");
        gen.writeStartObject();
        gen.writeStringField("maxValue", minValue.toString());
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
            int comparisonResult = new BigDecimal(valueNode.toString()).compareTo(minValue);
            return inclusive ? comparisonResult >= 0 : comparisonResult > 0;
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
            int comparisonResult = new BigDecimal(value.toString()).compareTo(minValue);
            return inclusive ? comparisonResult >= 0 : comparisonResult > 0;
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
                return String.format("必须是数字，且值必须大于等于%s", minValue.toString());
            } else {
                return String.format("必须是数字，且值必须大于%s", minValue.toString());
            }
        }
        return message;
    }
}
