package ren.wenchao.jschema;

import org.junit.Test;

import static org.junit.Assert.*;

public class TypeSchemaTest {

    @Test
    public void test_int_schema_create() {
        String schema = TypeSchema.getSchema(int.class);
        assertEquals("\"int\"", schema);
    }

    @Test
    public void test_long_schema_create() {
        String schema = TypeSchema.getSchema(long.class);
        assertEquals("\"long\"", schema);
    }

    @Test
    public void test_string_schema_create() {
        String schema = TypeSchema.getSchema(String.class);
        assertEquals("\"string\"", schema);
    }

    @Test
    public void test_boolean_schema_create() {
        String schema = TypeSchema.getSchema(boolean.class);
        assertEquals("\"boolean\"", schema);
    }
}