package ren.wenchao.jschema;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;


class JacksonUtils {
    static final JsonFactory FACTORY = new JsonFactory();
    static final ObjectMapper MAPPER = new ObjectMapper(FACTORY);

    static {
        FACTORY.enable(JsonParser.Feature.ALLOW_COMMENTS);
        FACTORY.setCodec(MAPPER);
    }


    static JsonFactory getFactory() {
        return FACTORY;
    }

    static ObjectMapper getMapper() {
        return MAPPER;
    }

    private JacksonUtils() {
    }

    public static JsonNode toJsonNode(Object datum) {
        if (datum == null) {
            return null;
        }
        try {
            TokenBuffer generator = new TokenBuffer(new ObjectMapper(), false);
            toJson(datum, generator);
            return new ObjectMapper().readTree(generator.asParser());
        } catch (IOException e) {
            throw new SchemaRuntimeException(e);
        }
    }

    @SuppressWarnings(value = "unchecked")
    static void toJson(Object datum, JsonGenerator generator) throws IOException {
        if (datum == JsonProperties.NULL_VALUE) { // null
            generator.writeNull();
        } else if (datum instanceof Map) { // record, map
            generator.writeStartObject();
            for (Map.Entry<Object, Object> entry : ((Map<Object, Object>) datum).entrySet()) {
                generator.writeFieldName(entry.getKey().toString());
                toJson(entry.getValue(), generator);
            }
            generator.writeEndObject();
        } else if (datum instanceof Collection) { // array
            generator.writeStartArray();
            for (Object element : (Collection<?>) datum) {
                toJson(element, generator);
            }
            generator.writeEndArray();
        } else if (datum instanceof byte[]) { // bytes, fixed
            generator.writeString(new String((byte[]) datum, StandardCharsets.ISO_8859_1));
        } else if (datum instanceof CharSequence || datum instanceof Enum<?>) { // string, enum
            generator.writeString(datum.toString());
        } else if (datum instanceof Double) { // double
            generator.writeNumber((Double) datum);
        } else if (datum instanceof Float) { // float
            generator.writeNumber((Float) datum);
        } else if (datum instanceof Long) { // long
            generator.writeNumber((Long) datum);
        } else if (datum instanceof Integer) { // int
            generator.writeNumber((Integer) datum);
        } else if (datum instanceof Boolean) { // boolean
            generator.writeBoolean((Boolean) datum);
        } else if (datum instanceof BigInteger) {
            generator.writeNumber((BigInteger) datum);
        } else if (datum instanceof BigDecimal) {
            generator.writeNumber((BigDecimal) datum);
        } else {
            throw new SchemaRuntimeException("Unknown datum class: " + datum.getClass());
        }
    }

    public static Object toObject(JsonNode jsonNode) {
        return toObject(jsonNode, null);
    }

    public static Object toObject(JsonNode jsonNode, TypeSchema schema) {
        if (jsonNode == null) {
            return null;
        } else if (jsonNode.isNull()) {
            return JsonProperties.NULL_VALUE;
        } else if (jsonNode.isBoolean()) {
            return jsonNode.asBoolean();
        } else if (jsonNode.isInt()) {
            if (schema == null || schema.getType().equals(SchemaType.INT) || schema.getType().equals(SchemaType.INT_WRAPPER)) {
                return jsonNode.asInt();
            } else if (schema.getType().equals(SchemaType.LONG) || schema.getType().equals(SchemaType.LONG_WRAPPER)) {
                return jsonNode.asLong();
            } else if (schema.getType().equals(SchemaType.FLOAT) || schema.getType().equals(SchemaType.FLOAT_WRAPPER)) {
                return (float) jsonNode.asDouble();
            } else if (schema.getType().equals(SchemaType.DOUBLE) || schema.getType().equals(SchemaType.DOUBLE_WRAPPER)) {
                return jsonNode.asDouble();
            }
        } else if (jsonNode.isLong()) {
            if (schema == null || schema.getType().equals(SchemaType.LONG) || schema.getType().equals(SchemaType.LONG_WRAPPER)) {
                return jsonNode.asLong();
            } else if (schema.getType().equals(SchemaType.INT) || schema.getType().equals(SchemaType.INT_WRAPPER)) {
                if (jsonNode.canConvertToInt()) {
                    return jsonNode.asInt();
                } else {
                    return jsonNode.asLong();
                }
            } else if (schema.getType().equals(SchemaType.FLOAT) || schema.getType().equals(SchemaType.FLOAT_WRAPPER)) {
                return (float) jsonNode.asDouble();
            } else if (schema.getType().equals(SchemaType.DOUBLE) || schema.getType().equals(SchemaType.DOUBLE_WRAPPER)) {
                return jsonNode.asDouble();
            }
        } else if (jsonNode.isDouble() || jsonNode.isFloat()) {
            if (schema == null || schema.getType().equals(SchemaType.DOUBLE) || schema.getType().equals(SchemaType.DOUBLE_WRAPPER)) {
                return jsonNode.asDouble();
            } else if (schema.getType().equals(SchemaType.FLOAT) || schema.getType().equals(SchemaType.FLOAT_WRAPPER)) {
                return (float) jsonNode.asDouble();
            }
        } else if (jsonNode.isTextual()) {
            if (schema == null || schema.getType().equals(SchemaType.STRING) || schema.getType().equals(SchemaType.ENUM)) {
                return jsonNode.asText();
            } else if (schema.getType().equals(SchemaType.BYTES)) {
                return jsonNode.textValue().getBytes(StandardCharsets.ISO_8859_1);
            }
        } else if (jsonNode.isArray()) {
            List<Object> l = new ArrayList<>();
            for (JsonNode node : jsonNode) {
                l.add(toObject(node, schema == null ? null : schema.getElementType()));
            }
            return l;
        } else if (jsonNode.isObject()) {
            Map<Object, Object> m = new LinkedHashMap<>();
            for (Iterator<String> it = jsonNode.fieldNames(); it.hasNext(); ) {
                String key = it.next();
                final TypeSchema s;
                if (schema != null && schema.getType().equals(SchemaType.MAP)) {
                    s = schema.getValueType();
                } else if (schema != null && schema.getType().equals(SchemaType.RECORD)) {
                    s = schema.getField(key).schema();
                } else {
                    s = null;
                }
                Object value = toObject(jsonNode.get(key), s);
                m.put(key, value);
            }
            return m;
        }
        return null;
    }

    /**
     * Convert an object into a map
     *
     * @param datum The object
     * @return Its Map representation
     */
    public static Map objectToMap(Object datum) {
        ObjectMapper mapper = new ObjectMapper();
        // we only care about fields
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        return mapper.convertValue(datum, Map.class);
    }

    public static JsonNode parseJson(String s) {
        try {
            return MAPPER.readTree(FACTORY.createParser(s));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object parseJsonToObject(String s) {
        return JacksonUtils.toObject(JacksonUtils.parseJson(s));
    }

    public static String safeGetTextValue(JsonNode node, String key) {
        if (node == null) {
            return "";
        }
        JsonNode value = node.get(key);
        if (value == null) {
            return "";
        }
        return value.asText();
    }

    public static String safeGetValue(JsonNode node, String key) {
        if (node == null) {
            return "";
        }
        JsonNode value = node.get(key);
        if (value == null) {
            return "";
        }
        return value.toString();
    }
}
