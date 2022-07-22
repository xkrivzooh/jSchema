package ren.wenchao.jschema;

import org.junit.Assert;
import org.junit.Test;

public class NameTest extends BaseTest{

    static class A {
        @Name(value = "a1")
        int a;
        @Name(value = "b1")
        String b;
    }

    @Test
    public void test1() {
        Assert.assertEquals("{\n" +
                "  \"type\" : \"record\",\n" +
                "  \"name\" : \"A\",\n" +
                "  \"namespace\" : \"ren.wenchao.jschema.NameTest\",\n" +
                "  \"types\" : { },\n" +
                "  \"fields\" : [ {\n" +
                "    \"name\" : \"a1\",\n" +
                "    \"type\" : \"int\"\n" +
                "  }, {\n" +
                "    \"name\" : \"b1\",\n" +
                "    \"type\" : \"string\"\n" +
                "  } ]\n" +
                "}", getPrettySchemaString(A.class));
    }
}
