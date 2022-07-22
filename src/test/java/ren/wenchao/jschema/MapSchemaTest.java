package ren.wenchao.jschema;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class MapSchemaTest {

    @Test
    public void testMap2() {
        TypeReference<Map<Integer, Integer>> mapTypeReference = new TypeReference<Map<Integer, Integer>>() {
        };
        System.out.println(TypeSchema.getSchema(mapTypeReference.getType()).toString(true));
        Assert.assertEquals("{\n" +
                "  \"type\" : \"map\",\n" +
                "  \"keys\" : {\n" +
                "    \"type\" : \"int\",\n" +
                "    \"primitive-type\" : false\n" +
                "  },\n" +
                "  \"values\" : {\n" +
                "    \"type\" : \"int\",\n" +
                "    \"primitive-type\" : false\n" +
                "  },\n" +
                "  \"java-class\" : \"java.util.Map\"\n" +
                "}", TypeSchema.getSchema(mapTypeReference.getType()).toString(true));
    }


    @Test
    public void testMap0() {
        TypeReference<Map<String, Integer>> mapTypeReference = new TypeReference<Map<String, Integer>>() {
        };
        System.out.println(TypeSchema.getSchema(mapTypeReference.getType()).toString(true));

        Assert.assertEquals("{\n" +
                "  \"type\" : \"map\",\n" +
                "  \"keys\" : \"String\",\n" +
                "  \"values\" : {\n" +
                "    \"type\" : \"int\",\n" +
                "    \"primitive-type\" : false\n" +
                "  },\n" +
                "  \"java-class\" : \"java.util.Map\"\n" +
                "}", TypeSchema.getSchema(mapTypeReference.getType()).toString(true));
    }

    @Test
    public void testMap1() {
        TypeReference<Map<Integer, List<Integer>>> mapTypeReference = new TypeReference<Map<Integer, List<Integer>>>() {
        };
        System.out.println(TypeSchema.getSchema(mapTypeReference.getType()).toString(true));
    }

    @Test
    public void testMap3() {
        TypeSchema map = TypeSchema.createMap(TypeSchema.getSchema(int.class), TypeSchema.getSchema(int.class));
        Assert.assertEquals("{\n" +
                "  \"type\" : \"map\",\n" +
                "  \"keys\" : \"int\",\n" +
                "  \"values\" : \"int\"\n" +
                "}", map.toString(true));
    }
}
