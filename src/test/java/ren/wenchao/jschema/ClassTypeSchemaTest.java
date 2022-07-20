package ren.wenchao.jschema;

import org.junit.Assert;
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
        Assert.assertEquals("{\n" +
                "  \"type\" : \"record\",\n" +
                "  \"name\" : \"C\",\n" +
                "  \"namespace\" : \"ren.wenchao.jschema.ClassTypeSchemaTest\",\n" +
                "  \"types\" : {\n" +
                "    \"ren.wenchao.jschema.ClassTypeSchemaTest.A\" : {\n" +
                "      \"type\" : \"record\",\n" +
                "      \"name\" : \"A\",\n" +
                "      \"namespace\" : \"ren.wenchao.jschema.ClassTypeSchemaTest\",\n" +
                "      \"types\" : { },\n" +
                "      \"fields\" : [ {\n" +
                "        \"name\" : \"i1\",\n" +
                "        \"type\" : \"int\"\n" +
                "      } ]\n" +
                "    },\n" +
                "    \"ren.wenchao.jschema.ClassTypeSchemaTest.B\" : {\n" +
                "      \"type\" : \"record\",\n" +
                "      \"name\" : \"B\",\n" +
                "      \"namespace\" : \"ren.wenchao.jschema.ClassTypeSchemaTest\",\n" +
                "      \"types\" : { },\n" +
                "      \"fields\" : [ {\n" +
                "        \"name\" : \"a\",\n" +
                "        \"type\" : \"ren.wenchao.jschema.ClassTypeSchemaTest.A\"\n" +
                "      }, {\n" +
                "        \"name\" : \"i2\",\n" +
                "        \"type\" : \"int\"\n" +
                "      } ]\n" +
                "    }\n" +
                "  },\n" +
                "  \"fields\" : [ {\n" +
                "    \"name\" : \"a\",\n" +
                "    \"type\" : \"ren.wenchao.jschema.ClassTypeSchemaTest.A\"\n" +
                "  }, {\n" +
                "    \"name\" : \"b\",\n" +
                "    \"type\" : \"ren.wenchao.jschema.ClassTypeSchemaTest.B\"\n" +
                "  }, {\n" +
                "    \"name\" : \"i3\",\n" +
                "    \"type\" : \"int\"\n" +
                "  } ]\n" +
                "}", bSchema);
    }
}
