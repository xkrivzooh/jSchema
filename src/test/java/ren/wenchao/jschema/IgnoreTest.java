package ren.wenchao.jschema;

import org.junit.Assert;
import org.junit.Test;

public class IgnoreTest extends BaseTest{

    static class A {
        int a;
        @Ignore
        int b;
    }

    static class B {
        int b;
        @Ignore
        A a;
    }

    @Test
    public void test1() {
        Assert.assertEquals("{\n" +
                "  \"type\" : \"record\",\n" +
                "  \"name\" : \"A\",\n" +
                "  \"namespace\" : \"ren.wenchao.jschema.IgnoreTest\",\n" +
                "  \"types\" : { },\n" +
                "  \"fields\" : [ {\n" +
                "    \"name\" : \"a\",\n" +
                "    \"type\" : \"int\"\n" +
                "  } ]\n" +
                "}", getPrettySchemaString(A.class));
    }

    @Test
    public void test2() {
        Assert.assertEquals("{\n" +
                "  \"type\" : \"record\",\n" +
                "  \"name\" : \"B\",\n" +
                "  \"namespace\" : \"ren.wenchao.jschema.IgnoreTest\",\n" +
                "  \"types\" : { },\n" +
                "  \"fields\" : [ {\n" +
                "    \"name\" : \"b\",\n" +
                "    \"type\" : \"int\"\n" +
                "  } ]\n" +
                "}", getPrettySchemaString(B.class));
    }
}
