package ren.wenchao.jschema;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class ListSchemaTest {
    @Test
    public void test1() {
        TypeReference<List<Integer>> mapTypeReference = new TypeReference<List<Integer>>() {
        };
        System.out.println(TypeSchema.getSchema(mapTypeReference.getType()).toString(true));
        Assert.assertEquals("{\n" +
                "  \"type\" : \"list\",\n" +
                "  \"items\" : {\n" +
                "    \"type\" : \"int\",\n" +
                "    \"primitive-type\" : false\n" +
                "  },\n" +
                "  \"java-class\" : \"java.util.List\"\n" +
                "}", TypeSchema.getSchema(mapTypeReference.getType()).toString(true));
    }

    @Test
    public void test2() {
        TypeReference<List<Object>> mapTypeReference = new TypeReference<List<Object>>() {
        };
        System.out.println(TypeSchema.getSchema(mapTypeReference.getType()).toString(true));
        Assert.assertEquals("{\n" +
                "  \"type\" : \"list\",\n" +
                "  \"items\" : {\n" +
                "    \"type\" : \"record\",\n" +
                "    \"name\" : \"Object\",\n" +
                "    \"namespace\" : \"java.lang\",\n" +
                "    \"types\" : { },\n" +
                "    \"fields\" : [ ]\n" +
                "  },\n" +
                "  \"java-class\" : \"java.util.List\"\n" +
                "}", TypeSchema.getSchema(mapTypeReference.getType()).toString(true));
    }

    @Test
    public void test3() {
        TypeReference<List<List<Integer>>> mapTypeReference = new TypeReference<List<List<Integer>>>() {
        };
        System.out.println(TypeSchema.getSchema(mapTypeReference.getType()).toString(true));
        Assert.assertEquals("{\n" +
                "  \"type\" : \"list\",\n" +
                "  \"items\" : {\n" +
                "    \"type\" : \"list\",\n" +
                "    \"items\" : {\n" +
                "      \"type\" : \"int\",\n" +
                "      \"primitive-type\" : false\n" +
                "    },\n" +
                "    \"java-class\" : \"java.util.List\"\n" +
                "  },\n" +
                "  \"java-class\" : \"java.util.List\"\n" +
                "}", TypeSchema.getSchema(mapTypeReference.getType()).toString(true));
    }

    @Test
    public void test4() {
        TypeReference<List<List<Map<String, String>>>> mapTypeReference = new TypeReference<List<List<Map<String, String>>>>() {
        };
        System.out.println(TypeSchema.getSchema(mapTypeReference.getType()).toString(true));
        Assert.assertEquals("{\n" +
                "  \"type\" : \"list\",\n" +
                "  \"items\" : {\n" +
                "    \"type\" : \"list\",\n" +
                "    \"items\" : {\n" +
                "      \"type\" : \"map\",\n" +
                "      \"keys\" : \"String\",\n" +
                "      \"values\" : \"String\",\n" +
                "      \"java-class\" : \"java.util.Map\"\n" +
                "    },\n" +
                "    \"java-class\" : \"java.util.List\"\n" +
                "  },\n" +
                "  \"java-class\" : \"java.util.List\"\n" +
                "}", TypeSchema.getSchema(mapTypeReference.getType()).toString(true));
    }
}
