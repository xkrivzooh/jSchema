package ren.wenchao.jschema;

import org.junit.Assert;
import org.junit.Test;

public class EnumSchemaTest {
    public static enum A {
        a, b
    }

    public static enum B {
        a("a");
        private final String name;

        B(String name) {
            this.name = name;
        }
    }

    public static class C {
        String str;
        A a;
        B b;
    }

    public static class D {
        A a;
        A a1;
    }

    @Test
    public void test1() {
        System.out.println(TypeSchema.getSchema(A.class).toString(true));
        Assert.assertEquals("{\n" +
                "  \"type\" : \"enum\",\n" +
                "  \"name\" : \"A\",\n" +
                "  \"namespace\" : \"ren.wenchao.jschema.EnumSchemaTest\",\n" +
                "  \"symbols\" : [ \"a\", \"b\" ]\n" +
                "}", TypeSchema.getSchema(A.class).toString(true));
    }

    @Test
    public void test2() {
        System.out.println(TypeSchema.getSchema(B.class).toString(true));
        Assert.assertEquals("{\n" +
                "  \"type\" : \"enum\",\n" +
                "  \"name\" : \"B\",\n" +
                "  \"namespace\" : \"ren.wenchao.jschema.EnumSchemaTest\",\n" +
                "  \"symbols\" : [ \"a\" ]\n" +
                "}", TypeSchema.getSchema(B.class).toString(true));
    }

    @Test
    public void test3() {
        System.out.println(TypeSchema.getSchema(C.class).toString(true));
        Assert.assertEquals("{\n" +
                "  \"type\" : \"record\",\n" +
                "  \"name\" : \"C\",\n" +
                "  \"namespace\" : \"ren.wenchao.jschema.EnumSchemaTest\",\n" +
                "  \"types\" : {\n" +
                "    \"ren.wenchao.jschema.EnumSchemaTest.A\" : {\n" +
                "      \"type\" : \"enum\",\n" +
                "      \"name\" : \"A\",\n" +
                "      \"namespace\" : \"ren.wenchao.jschema.EnumSchemaTest\",\n" +
                "      \"symbols\" : [ \"a\", \"b\" ]\n" +
                "    },\n" +
                "    \"ren.wenchao.jschema.EnumSchemaTest.B\" : {\n" +
                "      \"type\" : \"enum\",\n" +
                "      \"name\" : \"B\",\n" +
                "      \"namespace\" : \"ren.wenchao.jschema.EnumSchemaTest\",\n" +
                "      \"symbols\" : [ \"a\" ]\n" +
                "    }\n" +
                "  },\n" +
                "  \"fields\" : [ {\n" +
                "    \"name\" : \"a\",\n" +
                "    \"type\" : \"ren.wenchao.jschema.EnumSchemaTest.A\"\n" +
                "  }, {\n" +
                "    \"name\" : \"b\",\n" +
                "    \"type\" : \"ren.wenchao.jschema.EnumSchemaTest.B\"\n" +
                "  }, {\n" +
                "    \"name\" : \"str\",\n" +
                "    \"type\" : \"string\"\n" +
                "  } ]\n" +
                "}", TypeSchema.getSchema(C.class).toString(true));
    }

    @Test
    public void test4() {
        System.out.println(TypeSchema.getSchema(D.class).toString(true));
        Assert.assertEquals("{\n" +
                "  \"type\" : \"record\",\n" +
                "  \"name\" : \"D\",\n" +
                "  \"namespace\" : \"ren.wenchao.jschema.EnumSchemaTest\",\n" +
                "  \"types\" : {\n" +
                "    \"ren.wenchao.jschema.EnumSchemaTest.A\" : {\n" +
                "      \"type\" : \"enum\",\n" +
                "      \"name\" : \"A\",\n" +
                "      \"namespace\" : \"ren.wenchao.jschema.EnumSchemaTest\",\n" +
                "      \"symbols\" : [ \"a\", \"b\" ]\n" +
                "    }\n" +
                "  },\n" +
                "  \"fields\" : [ {\n" +
                "    \"name\" : \"a\",\n" +
                "    \"type\" : \"ren.wenchao.jschema.EnumSchemaTest.A\"\n" +
                "  }, {\n" +
                "    \"name\" : \"a1\",\n" +
                "    \"type\" : \"ren.wenchao.jschema.EnumSchemaTest.A\"\n" +
                "  } ]\n" +
                "}", TypeSchema.getSchema(D.class).toString(true));
    }
}
