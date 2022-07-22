package ren.wenchao.jschema;

import org.junit.Assert;
import org.junit.Test;

public class DefaultTest extends BaseTest {
    private static class A {
        @Default("1")
        int foo;
    }

    @Test
    public void test1() {
        System.out.println(TypeSchema.getSchema(A.class).toString(true));
        Assert.assertEquals("{\n" +
                "  \"type\" : \"record\",\n" +
                "  \"name\" : \"A\",\n" +
                "  \"namespace\" : \"ren.wenchao.jschema.DefaultTest\",\n" +
                "  \"types\" : { },\n" +
                "  \"fields\" : [ {\n" +
                "    \"name\" : \"foo\",\n" +
                "    \"type\" : \"int\",\n" +
                "    \"default\" : 1\n" +
                "  } ]\n" +
                "}", TypeSchema.getSchema(A.class).toString(true));
    }
}
