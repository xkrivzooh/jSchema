package ren.wenchao.jschema;

import org.junit.Assert;
import org.junit.Test;

public class MetaTest extends BaseTest{

    @Meta(key = "k1", value = "v1")
    @Meta(key = "k2", value = "v2")
    static class A {
        @Meta(key = "k3", value = "v3")
        @Meta(key = "k4", value = "v4")
        int a;
    }

    @Test
    public void test1() {
        System.out.println(getPrettySchemaString(A.class));
        Assert.assertEquals("{\n" +
                "  \"type\" : \"record\",\n" +
                "  \"name\" : \"A\",\n" +
                "  \"namespace\" : \"ren.wenchao.jschema.MetaTest\",\n" +
                "  \"types\" : { },\n" +
                "  \"fields\" : [ {\n" +
                "    \"name\" : \"a\",\n" +
                "    \"type\" : \"int\",\n" +
                "    \"k3\" : \"v3\",\n" +
                "    \"k4\" : \"v4\"\n" +
                "  } ],\n" +
                "  \"k1\" : \"v1\",\n" +
                "  \"k2\" : \"v2\"\n" +
                "}", getPrettySchemaString(A.class));
    }
}
