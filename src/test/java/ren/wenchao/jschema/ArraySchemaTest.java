package ren.wenchao.jschema;

import org.junit.Test;

import static org.junit.Assert.*;

public class ArraySchemaTest {

    @Test
    public void test_array_schema_create() {
        assertEquals("{\"type\":\"array\",\"items\":\"int\",\"java-class\":\"[I\"}", TypeSchema.getSchemaString(int[].class));
        assertEquals("{\"type\":\"array\",\"items\":\"byte\",\"java-class\":\"[B\"}", TypeSchema.getSchemaString(byte[].class));
        assertEquals("{\"type\":\"array\",\"items\":\"Byte\",\"java-class\":\"[Ljava.lang.Byte;\"}", TypeSchema.getSchemaString(Byte[].class));
        assertEquals("{\"type\":\"array\",\"items\":\"short\",\"java-class\":\"[S\"}", TypeSchema.getSchemaString(short[].class));
        assertEquals("{\"type\":\"array\",\"items\":\"Short\",\"java-class\":\"[Ljava.lang.Short;\"}", TypeSchema.getSchemaString(Short[].class));
        assertEquals("{\"type\":\"array\",\"items\":\"float\",\"java-class\":\"[F\"}", TypeSchema.getSchemaString(float[].class));
        assertEquals("{\"type\":\"array\",\"items\":{\"type\":\"float\",\"primitive-type\":false},\"java-class\":\"[Ljava.lang.Float;\"}", TypeSchema.getSchemaString(Float[].class));
        assertEquals("{\"type\":\"array\",\"items\":\"double\",\"java-class\":\"[D\"}", TypeSchema.getSchemaString(double[].class));
        assertEquals("{\"type\":\"array\",\"items\":{\"type\":\"double\",\"primitive-type\":false},\"java-class\":\"[Ljava.lang.Double;\"}", TypeSchema.getSchemaString(Double[].class));
        assertEquals("{\"type\":\"array\",\"items\":\"char\",\"java-class\":\"[C\"}", TypeSchema.getSchemaString(char[].class));
        assertEquals("{\"type\":\"array\",\"items\":{\"type\":\"char\",\"primitive-type\":false},\"java-class\":\"[Ljava.lang.Character;\"}", TypeSchema.getSchemaString(Character[].class));
        assertEquals("{\"type\":\"array\",\"items\":\"boolean\",\"java-class\":\"[Z\"}", TypeSchema.getSchemaString(boolean[].class));
        assertEquals("{\"type\":\"array\",\"items\":{\"type\":\"boolean\",\"primitive-type\":false},\"java-class\":\"[Ljava.lang.Boolean;\"}", TypeSchema.getSchemaString(Boolean[].class));
        assertEquals("{\"type\":\"array\",\"items\":\"String\",\"java-class\":\"[Ljava.lang.String;\"}", TypeSchema.getSchemaString(String[].class));
        assertEquals("{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"Object\",\"namespace\":\"java.lang\",\"types\":{},\"fields\":[]},\"java-class\":\"[Ljava.lang.Object;\"}", TypeSchema.getSchemaString(Object[].class));
    }




}
