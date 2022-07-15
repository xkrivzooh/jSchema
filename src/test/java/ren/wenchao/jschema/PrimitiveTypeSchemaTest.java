package ren.wenchao.jschema;

import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.*;

public class PrimitiveTypeSchemaTest {

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

    @Test
    public void test_byteBuffer_schema_create() {
        String schema = TypeSchema.getSchema(ByteBuffer.class);
        assertEquals("\"bytes\"", schema);
    }

    @Test
    public void test_float_schema_create() {
        String schema = TypeSchema.getSchema(float.class);
        assertEquals("\"float\"", schema);
    }

    @Test
    public void test_double_schema_create() {
        String schema = TypeSchema.getSchema(double.class);
        assertEquals("\"double\"", schema);
    }

    @Test
    public void test_void_schema_create() {
        String schema = TypeSchema.getSchema(void.class);
        assertEquals("\"null\"", schema);
    }
}