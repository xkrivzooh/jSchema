package ren.wenchao.jschema;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
                "    \"type\" : \"String\"\n" +
                "  }, {\n" +
                "    \"name\" : \"bWithNullable\",\n" +
                "    \"type\" : \"String\",\n" +
                "    \"nullable\" : true\n" +
                "  }, {\n" +
                "    \"name\" : \"integerWithNullable\",\n" +
                "    \"type\" : \"Integer\",\n" +
                "    \"nullable\" : true\n" +
                "  } ]\n" +
                "}", TypeSchema.getSchema(A.class).toString(true));
    }

    public static class B {
        @Nullable
        byte[] bytes;
        @Nullable
        Object obj;
        List<String> list;
        Set<String> set;
        Map<String, String> map;
    }

    @Test
    public void test2() {
        System.out.println(TypeSchema.getSchema(B.class).toString(true));
        Assert.assertEquals("{\n" +
                "  \"type\" : \"record\",\n" +
                "  \"name\" : \"B\",\n" +
                "  \"namespace\" : \"ren.wenchao.jschema.NullableTest\",\n" +
                "  \"types\" : {\n" +
                "    \"java.lang.Object\" : {\n" +
                "      \"type\" : \"record\",\n" +
                "      \"name\" : \"Object\",\n" +
                "      \"namespace\" : \"java.lang\",\n" +
                "      \"types\" : { },\n" +
                "      \"fields\" : [ ]\n" +
                "    }\n" +
                "  },\n" +
                "  \"fields\" : [ {\n" +
                "    \"name\" : \"bytes\",\n" +
                "    \"type\" : {\n" +
                "      \"type\" : \"array\",\n" +
                "      \"items\" : \"byte\",\n" +
                "      \"java-class\" : \"[B\"\n" +
                "    },\n" +
                "    \"nullable\" : true\n" +
                "  }, {\n" +
                "    \"name\" : \"list\",\n" +
                "    \"type\" : {\n" +
                "      \"type\" : \"List\",\n" +
                "      \"items\" : \"String\",\n" +
                "      \"java-class\" : \"java.util.List\"\n" +
                "    }\n" +
                "  }, {\n" +
                "    \"name\" : \"map\",\n" +
                "    \"type\" : {\n" +
                "      \"type\" : \"Map\",\n" +
                "      \"keys\" : \"String\",\n" +
                "      \"values\" : \"String\",\n" +
                "      \"java-class\" : \"java.util.Map\"\n" +
                "    }\n" +
                "  }, {\n" +
                "    \"name\" : \"obj\",\n" +
                "    \"type\" : \"java.lang.Object\",\n" +
                "    \"nullable\" : true\n" +
                "  }, {\n" +
                "    \"name\" : \"set\",\n" +
                "    \"type\" : {\n" +
                "      \"type\" : \"Set\",\n" +
                "      \"items\" : \"String\",\n" +
                "      \"java-class\" : \"java.util.Set\"\n" +
                "    }\n" +
                "  } ]\n" +
                "}", TypeSchema.getSchema(B.class).toString(true));
    }
}

