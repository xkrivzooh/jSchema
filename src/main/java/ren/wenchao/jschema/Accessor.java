package ren.wenchao.jschema;

import com.fasterxml.jackson.databind.JsonNode;

class Accessor {
    public abstract static class JsonPropertiesAccessor {
        protected abstract void addProp(JsonProperties props, String name, JsonNode value);
    }

    public abstract static class FieldAccessor {
        protected abstract JsonNode defaultValue(Field field);

        protected abstract Field createField(String name, TypeSchema schema, String doc, JsonNode defaultValue,
                                             boolean validate, Field.Order order);

        protected abstract Field createField(String name, TypeSchema schema, String doc, JsonNode defaultValue);
    }

    private static volatile JsonPropertiesAccessor jsonPropertiesAccessor;

    private static volatile FieldAccessor fieldAccessor;

    public static void setAccessor(JsonPropertiesAccessor accessor) {
        if (jsonPropertiesAccessor != null) {
            throw new IllegalStateException("JsonPropertiesAccessor already initialized");
        }
        jsonPropertiesAccessor = accessor;
    }

    public static void setAccessor(FieldAccessor accessor) {
        if (fieldAccessor != null) {
            throw new IllegalStateException("FieldAccessor already initialized");
        }
        fieldAccessor = accessor;
    }

    private static FieldAccessor fieldAccessor() {
        if (fieldAccessor == null) {
            ensureLoaded(Field.class);
        }
        return fieldAccessor;
    }

    private static void ensureLoaded(Class<?> c) {
        try {
            Class.forName(c.getName());
        } catch (ClassNotFoundException e) {
            // Shall never happen as the class is specified by its Class instance
        }
    }

    public static void addProp(JsonProperties props, String name, JsonNode value) {
        jsonPropertiesAccessor.addProp(props, name, value);
    }

    public static JsonNode defaultValue(Field field) {
        return fieldAccessor.defaultValue(field);
    }

    public static Field createField(String name, TypeSchema schema, String doc, JsonNode defaultValue, boolean validate,
                                    Field.Order order) {
        return fieldAccessor().createField(name, schema, doc, defaultValue, validate, order);
    }

    public static Field createField(String name, TypeSchema schema, String doc, JsonNode defaultValue) {
        return fieldAccessor().createField(name, schema, doc, defaultValue);
    }

}
