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
    public void test_integer_schema_create() {
        String schema = TypeSchema.getSchema(Integer.class);
        assertEquals("{\"type\":\"int\",\"primitive-type\":false}", schema);
    }

    @Test
    public void test_short_schema_create() {
        String schema = TypeSchema.getSchema(short.class);
        assertEquals("\"short\"", schema);
    }

    @Test
    public void test_Short_schema_create() {
        String schema = TypeSchema.getSchema(Short.class);
        assertEquals("{\"type\":\"short\",\"primitive-type\":false}", schema);
    }

    @Test
    public void test_long_schema_create() {
        String schema = TypeSchema.getSchema(long.class);
        assertEquals("\"long\"", schema);
    }

    @Test
    public void test_Long_schema_create() {
        String schema = TypeSchema.getSchema(Long.class);
        assertEquals("{\"type\":\"long\",\"primitive-type\":false}", schema);
    }

    @Test
    public void test_string_schema_create() {
        String schema = TypeSchema.getSchema(String.class);
        assertEquals("\"string\"", schema);
    }

    @Test
    public void test_char_schema_create() {
        String schema = TypeSchema.getSchema(char.class);
        assertEquals("\"char\"", schema);
    }

    @Test
    public void test_Character_schema_create() {
        String schema = TypeSchema.getSchema(Character.class);
        assertEquals("{\"type\":\"char\",\"primitive-type\":false}", schema);
    }

    @Test
    public void test_boolean_schema_create() {
        String schema = TypeSchema.getSchema(boolean.class);
        assertEquals("\"boolean\"", schema);
    }

    @Test
    public void test_Boolean_schema_create() {
        String schema = TypeSchema.getSchema(Boolean.class);
        assertEquals("{\"type\":\"boolean\",\"primitive-type\":false}", schema);
    }

    @Test
    public void test_byte_schema_create() {
        String schema = TypeSchema.getSchema(byte.class);
        assertEquals("\"byte\"", schema);
    }

    @Test
    public void test_Byte_schema_create() {
        String schema = TypeSchema.getSchema(Byte.class);
        assertEquals("{\"type\":\"byte\",\"primitive-type\":false}", schema);
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
    public void test_Float_schema_create() {
        String schema = TypeSchema.getSchema(Float.class);
        assertEquals("{\"type\":\"float\",\"primitive-type\":false}", schema);
    }


    @Test
    public void test_double_schema_create() {
        String schema = TypeSchema.getSchema(double.class);
        assertEquals("\"double\"", schema);
    }

    @Test
    public void test_Double_schema_create() {
        String schema = TypeSchema.getSchema(Double.class);
        assertEquals("{\"type\":\"double\",\"primitive-type\":false}", schema);
    }

    @Test
    public void test_void_schema_create() {
        String schema = TypeSchema.getSchema(void.class);
        assertEquals("\"void\"", schema);
    }
}