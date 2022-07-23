package ren.wenchao.jschema;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

public class SetSchemaTest {

    @Test
    public void test1() {
        TypeReference<Set<String>> mapTypeReference = new TypeReference<Set<String>>() {
        };
        System.out.println(TypeSchema.getSchema(mapTypeReference.getType()).toString(true));
        Assert.assertEquals("{\n" +
                "  \"type\" : \"Set\",\n" +
                "  \"items\" : \"String\",\n" +
                "  \"java-class\" : \"java.util.Set\"\n" +
                "}", TypeSchema.getSchema(mapTypeReference.getType()).toString(true));
    }

    @Test
    public void test2() {
        TypeReference<Set<Object>> mapTypeReference = new TypeReference<Set<Object>>() {
        };
        System.out.println(TypeSchema.getSchema(mapTypeReference.getType()).toString(true));
        Assert.assertEquals("{\n" +
                "  \"type\" : \"Set\",\n" +
                "  \"items\" : {\n" +
                "    \"type\" : \"record\",\n" +
                "    \"name\" : \"Object\",\n" +
                "    \"namespace\" : \"java.lang\",\n" +
                "    \"types\" : { },\n" +
                "    \"fields\" : [ ]\n" +
                "  },\n" +
                "  \"java-class\" : \"java.util.Set\"\n" +
                "}", TypeSchema.getSchema(mapTypeReference.getType()).toString(true));
    }

    @Test
    public void test3() {
        TypeReference<Set<Set<Integer>>> mapTypeReference = new TypeReference<Set<Set<Integer>>>() {
        };
        System.out.println(TypeSchema.getSchema(mapTypeReference.getType()).toString(true));
        Assert.assertEquals("{\n" +
                "  \"type\" : \"Set\",\n" +
                "  \"items\" : {\n" +
                "    \"type\" : \"Set\",\n" +
                "    \"items\" : \"Integer\",\n" +
                "    \"java-class\" : \"java.util.Set\"\n" +
                "  },\n" +
                "  \"java-class\" : \"java.util.Set\"\n" +
                "}", TypeSchema.getSchema(mapTypeReference.getType()).toString(true));
    }

    @Test
    public void test4() {
        TypeReference<Set<Set<Map<String, String>>>> mapTypeReference = new TypeReference<Set<Set<Map<String, String>>>>() {
        };
        System.out.println(TypeSchema.getSchema(mapTypeReference.getType()).toString(true));
        Assert.assertEquals("{\n" +
                "  \"type\" : \"Set\",\n" +
                "  \"items\" : {\n" +
                "    \"type\" : \"Set\",\n" +
                "    \"items\" : {\n" +
                "      \"type\" : \"Map\",\n" +
                "      \"keys\" : \"String\",\n" +
                "      \"values\" : \"String\",\n" +
                "      \"java-class\" : \"java.util.Map\"\n" +
                "    },\n" +
                "    \"java-class\" : \"java.util.Set\"\n" +
                "  },\n" +
                "  \"java-class\" : \"java.util.Set\"\n" +
                "}", TypeSchema.getSchema(mapTypeReference.getType()).toString(true));
    }

}
