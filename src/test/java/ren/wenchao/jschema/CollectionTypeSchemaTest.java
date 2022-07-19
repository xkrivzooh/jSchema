package ren.wenchao.jschema;

import org.junit.Test;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class CollectionTypeSchemaTest {

    @Test
    public void test_array_schema_create() {
        String schema = TypeSchema.getSchemaString(int[].class);
        assertEquals("{\"type\":\"array\",\"items\":\"int\",\"java-class\":\"[I\"}", schema);

        System.out.println(TypeSchema.getSchemaString(byte[].class));
        System.out.println(TypeSchema.getSchemaString(Byte[].class));
        System.out.println(TypeSchema.getSchemaString(short[].class));
        System.out.println(TypeSchema.getSchemaString(Short[].class));
        System.out.println(TypeSchema.getSchemaString(float[].class));
        System.out.println(TypeSchema.getSchemaString(Float[].class));
        System.out.println(TypeSchema.getSchemaString(double[].class));
        System.out.println(TypeSchema.getSchemaString(Double[].class));
        System.out.println(TypeSchema.getSchemaString(char[].class));
        System.out.println(TypeSchema.getSchemaString(Character[].class));
        System.out.println(TypeSchema.getSchemaString(boolean[].class));
        System.out.println(TypeSchema.getSchemaString(Boolean[].class));
        System.out.println(TypeSchema.getSchemaString(String[].class));
        System.out.println(TypeSchema.getSchemaString(Object[].class));
    }

    @Test
    public void testMap0() {
        TypeReference<Map<String, Integer>> mapTypeReference = new TypeReference<Map<String, Integer>>(){};
        System.out.println(TypeSchema.getSchema(mapTypeReference.getType()).toString(true));
    }

    @Test
    public void testMap1() {
        TypeReference<Map<Integer, List<Integer>>> mapTypeReference = new TypeReference<Map<Integer, List<Integer>>>(){};
        System.out.println(TypeSchema.getSchema(mapTypeReference.getType()).toString(true));
    }
}
