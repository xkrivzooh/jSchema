package ren.wenchao.jschema;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ClassTypeSchemaTest {

    public static class A {
        private int i1;
    }

    public static class B {
        private int i2;
        private A a;
    }

    public static class C {
        private int i3;
        private A a;
        private B b;
    }

    @Test
    public void test1() {
        String schema = TypeSchema.getSchema(A.class).toString(true);
        assertEquals("{\n" +
                "  \"type\" : \"record\",\n" +
                "  \"name\" : \"A\",\n" +
                "  \"namespace\" : \"ren.wenchao.jschema.ClassTypeSchemaTest\",\n" +
                "  \"fields\" : [ {\n" +
                "    \"name\" : \"aInt\",\n" +
                "    \"type\" : \"int\"\n" +
                "  }, {\n" +
                "    \"name\" : \"aInteger\",\n" +
                "    \"type\" : {\n" +
                "      \"type\" : \"int\",\n" +
                "      \"primitive-type\" : false\n" +
                "    }\n" +
                "  } ]\n" +
                "}", schema);
    }

    @Test
    public void test2() {
        String bSchema = TypeSchema.getSchema(C.class).toString(true);
        System.out.println(bSchema);
    }
}
