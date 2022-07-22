package ren.wenchao.jschema;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SchemaTypeTest {

    @Test
    public void test1() {
        assertEquals("byte", SchemaType.BYTE.getName());
        assertEquals("Byte", SchemaType.BYTE_WRAPPER.getName());
        assertEquals("bytes", SchemaType.BYTES.getName());

        assertEquals("short", SchemaType.SHORT.getName());
        assertEquals("Short", SchemaType.SHORT_WRAPPER.getName());

        assertEquals("int", SchemaType.INT.getName());
        assertEquals("Integer", SchemaType.INT_WRAPPER.getName());

        assertEquals("long", SchemaType.LONG.getName());
        assertEquals("Long", SchemaType.LONG_WRAPPER.getName());

        assertEquals("float", SchemaType.FLOAT.getName());
        assertEquals("Float", SchemaType.FLOAT_WRAPPER.getName());

        assertEquals("double", SchemaType.DOUBLE.getName());
        assertEquals("Double", SchemaType.DOUBLE_WRAPPER.getName());

        assertEquals("boolean", SchemaType.BOOLEAN.getName());
        assertEquals("Boolean", SchemaType.BOOLEAN_WRAPPER.getName());

        assertEquals("char", SchemaType.CHAR.getName());
        assertEquals("Character", SchemaType.CHAR_WRAPPER.getName());

        assertEquals("String", SchemaType.STRING.getName());
        assertEquals("array", SchemaType.ARRAY.getName());
        assertEquals("Map", SchemaType.MAP.getName());
        assertEquals("List", SchemaType.LIST.getName());
        assertEquals("Set", SchemaType.SET.getName());


        assertEquals("class", SchemaType.RECORD.getName());
        assertEquals("null", SchemaType.NULL.getName());
        assertEquals("void", SchemaType.VOID.getName());
        assertEquals("enum", SchemaType.ENUM.getName());
    }
}