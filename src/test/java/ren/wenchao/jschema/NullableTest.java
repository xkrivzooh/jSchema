package ren.wenchao.jschema;

import org.junit.Assert;
import org.junit.Test;

public class NullableTest {

    public static class A {
        private int a;
        @Nullable
        private Integer integerWithNullable;
        @Nullable
        private int aWithNullable;
        private String b;
        @Nullable
        private String bWithNullable;
    }

    @Test
    public void test1() {
        System.out.println(TypeSchema.getSchema(A.class).toString(true));
        Assert.assertEquals("{\n" +
                "  \"type\" : \"record\",\n" +
                "  \"name\" : \"A\",\n" +
                "  \"namespace\" : \"ren.wenchao.jschema.NullableTest\",\n" +
                "  \"types\" : { },\n" +
                "  \"fields\" : [ {\n" +
                "    \"name\" : \"a\",\n" +
                "    \"type\" : \"int\"\n" +
                "  }, {\n" +
                "    \"name\" : \"aWithNullable\",\n" +
                "    \"type\" : \"int\",\n" +
                "    \"nullable\" : true\n" +
                "  }, {\n" +
                "    \"name\" : \"b\",\n" +
                "    \"type\" : \"string\"\n" +
                "  }, {\n" +
                "    \"name\" : \"bWithNullable\",\n" +
                "    \"type\" : \"string\",\n" +
                "    \"nullable\" : true\n" +
                "  }, {\n" +
                "    \"name\" : \"integerWithNullable\",\n" +
                "    \"type\" : {\n" +
                "      \"type\" : \"int\",\n" +
                "      \"primitive-type\" : false\n" +
                "    },\n" +
                "    \"nullable\" : true\n" +
                "  } ]\n" +
                "}", TypeSchema.getSchema(A.class).toString(true));
    }

}

