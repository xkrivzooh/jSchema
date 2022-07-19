package ren.wenchao.jschema;

import com.fasterxml.jackson.databind.JsonNode;

class FieldUtils {

    static final ThreadLocal<Boolean> VALIDATE_DEFAULTS = ThreadLocal.withInitial(() -> true);

    static JsonNode validateDefault(String fieldName, TypeSchema schema, JsonNode defaultValue) {
        if (VALIDATE_DEFAULTS.get() && (defaultValue != null) && !isValidDefault(schema, defaultValue)) { // invalid default
            String message = "Invalid default for field " + fieldName + ": " + defaultValue + " not a " + schema;
            throw new SchemaRuntimeException(message); // throw exception
        }
        return defaultValue;
    }

    private static boolean isValidDefault(TypeSchema schema, JsonNode defaultValue) {
        if (defaultValue == null) return false;
        switch (schema.getType()) {
            case STRING:
            case BYTES:
            case ENUM:
            case FIXED:
                return defaultValue.isTextual();
            case INT:
                return defaultValue.isIntegralNumber() && defaultValue.canConvertToInt();
            case LONG:
                return defaultValue.isIntegralNumber() && defaultValue.canConvertToLong();
            case FLOAT:
            case DOUBLE:
                return defaultValue.isNumber();
            case BOOLEAN:
                return defaultValue.isBoolean();
            case NULL:
                return defaultValue.isNull();
            case ARRAY:
                if (!defaultValue.isArray()) return false;
                for (JsonNode element : defaultValue)
                    if (!isValidDefault(schema.getElementType(), element)) return false;
                return true;
            case MAP:
                if (!defaultValue.isObject()) return false;
                for (JsonNode value : defaultValue)
                    if (!isValidDefault(schema.getValueType(), value)) return false;
                return true;
            case UNION: // union default: first branch
                return isValidDefault(schema.getTypes().get(0), defaultValue);
            case RECORD:
                if (!defaultValue.isObject()) return false;
                for (Field field : schema.getFields())
                    if (!isValidDefault(field.schema(), defaultValue.has(field.name()) ? defaultValue.get(field.name()) : field.defaultValue()))
                        return false;
                return true;
            default:
                return false;
        }
    }

}
