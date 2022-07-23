package ren.wenchao.jschema;

import org.junit.Assert;
import org.junit.Test;

public class NestedClassSchemaTest extends BaseTest {

    static enum Enum {
        e1, e2
    }

    static class A {
        Object aObject;
        Enum enumA;
    }

    static class B {
        Object bObject;
        A ba1;
        A ba2;
        Enum enumB;
    }

    static class C {
        Object cObject;
        B b;
        A a;
        Enum enumC;
    }

    static class D {
        Object dObject;
        A a;
        B b;
        C c;
        Enum enumD;
    }

    @Test
    public void test0() {
        System.out.println(getPrettySchemaString(A.class));
        Assert.assertEquals("{\n" +
                "  \"type\" : \"record\",\n" +
                "  \"name\" : \"A\",\n" +
                "  \"namespace\" : \"ren.wenchao.jschema.NestedClassSchemaTest\",\n" +
                "  \"types\" : {\n" +
                "    \"java.lang.Object\" : {\n" +
                "      \"type\" : \"record\",\n" +
                "      \"name\" : \"Object\",\n" +
                "      \"namespace\" : \"java.lang\",\n" +
                "      \"types\" : { },\n" +
                "      \"fields\" : [ ]\n" +
                "    },\n" +
                "    \"ren.wenchao.jschema.NestedClassSchemaTest.Enum\" : {\n" +
                "      \"type\" : \"enum\",\n" +
                "      \"name\" : \"Enum\",\n" +
                "      \"namespace\" : \"ren.wenchao.jschema.NestedClassSchemaTest\",\n" +
                "      \"symbols\" : [ \"e1\", \"e2\" ]\n" +
                "    }\n" +
                "  },\n" +
                "  \"fields\" : [ {\n" +
                "    \"name\" : \"aObject\",\n" +
                "    \"type\" : \"java.lang.Object\"\n" +
                "  }, {\n" +
                "    \"name\" : \"enumA\",\n" +
                "    \"type\" : \"ren.wenchao.jschema.NestedClassSchemaTest.Enum\"\n" +
                "  } ]\n" +
                "}", getPrettySchemaString(A.class));
    }

    @Test
    public void test1() {
        System.out.println(getPrettySchemaString(B.class));
        Assert.assertEquals("{\n" +
                "  \"type\" : \"record\",\n" +
                "  \"name\" : \"B\",\n" +
                "  \"namespace\" : \"ren.wenchao.jschema.NestedClassSchemaTest\",\n" +
                "  \"types\" : {\n" +
                "    \"java.lang.Object\" : {\n" +
                "      \"type\" : \"record\",\n" +
                "      \"name\" : \"Object\",\n" +
                "      \"namespace\" : \"java.lang\",\n" +
                "      \"types\" : { },\n" +
                "      \"fields\" : [ ]\n" +
                "    },\n" +
                "    \"ren.wenchao.jschema.NestedClassSchemaTest.A\" : {\n" +
                "      \"type\" : \"record\",\n" +
                "      \"name\" : \"A\",\n" +
                "      \"namespace\" : \"ren.wenchao.jschema.NestedClassSchemaTest\",\n" +
                "      \"types\" : {\n" +
                "        \"ren.wenchao.jschema.NestedClassSchemaTest.Enum\" : {\n" +
                "          \"type\" : \"enum\",\n" +
                "          \"name\" : \"Enum\",\n" +
                "          \"namespace\" : \"ren.wenchao.jschema.NestedClassSchemaTest\",\n" +
                "          \"symbols\" : [ \"e1\", \"e2\" ]\n" +
                "        }\n" +
                "      },\n" +
                "      \"fields\" : [ {\n" +
                "        \"name\" : \"aObject\",\n" +
                "        \"type\" : \"java.lang.Object\"\n" +
                "      }, {\n" +
                "        \"name\" : \"enumA\",\n" +
                "        \"type\" : \"ren.wenchao.jschema.NestedClassSchemaTest.Enum\"\n" +
                "      } ]\n" +
                "    }\n" +
                "  },\n" +
                "  \"fields\" : [ {\n" +
                "    \"name\" : \"bObject\",\n" +
                "    \"type\" : \"java.lang.Object\"\n" +
                "  }, {\n" +
                "    \"name\" : \"ba1\",\n" +
                "    \"type\" : \"ren.wenchao.jschema.NestedClassSchemaTest.A\"\n" +
                "  }, {\n" +
                "    \"name\" : \"ba2\",\n" +
                "    \"type\" : \"ren.wenchao.jschema.NestedClassSchemaTest.A\"\n" +
                "  }, {\n" +
                "    \"name\" : \"enumB\",\n" +
                "    \"type\" : \"ren.wenchao.jschema.NestedClassSchemaTest.Enum\"\n" +
                "  } ]\n" +
                "}", getPrettySchemaString(B.class));
    }

    @Test
    public void test2() {
        System.out.println(getPrettySchemaString(D.class));
        Assert.assertEquals("{\n" +
                "  \"type\" : \"record\",\n" +
                "  \"name\" : \"D\",\n" +
                "  \"namespace\" : \"ren.wenchao.jschema.NestedClassSchemaTest\",\n" +
                "  \"types\" : {\n" +
                "    \"ren.wenchao.jschema.NestedClassSchemaTest.A\" : {\n" +
                "      \"type\" : \"record\",\n" +
                "      \"name\" : \"A\",\n" +
                "      \"namespace\" : \"ren.wenchao.jschema.NestedClassSchemaTest\",\n" +
                "      \"types\" : {\n" +
                "        \"java.lang.Object\" : {\n" +
                "          \"type\" : \"record\",\n" +
                "          \"name\" : \"Object\",\n" +
                "          \"namespace\" : \"java.lang\",\n" +
                "          \"types\" : { },\n" +
                "          \"fields\" : [ ]\n" +
                "        },\n" +
                "        \"ren.wenchao.jschema.NestedClassSchemaTest.Enum\" : {\n" +
                "          \"type\" : \"enum\",\n" +
                "          \"name\" : \"Enum\",\n" +
                "          \"namespace\" : \"ren.wenchao.jschema.NestedClassSchemaTest\",\n" +
                "          \"symbols\" : [ \"e1\", \"e2\" ]\n" +
                "        }\n" +
                "      },\n" +
                "      \"fields\" : [ {\n" +
                "        \"name\" : \"aObject\",\n" +
                "        \"type\" : \"java.lang.Object\"\n" +
                "      }, {\n" +
                "        \"name\" : \"enumA\",\n" +
                "        \"type\" : \"ren.wenchao.jschema.NestedClassSchemaTest.Enum\"\n" +
                "      } ]\n" +
                "    },\n" +
                "    \"ren.wenchao.jschema.NestedClassSchemaTest.B\" : {\n" +
                "      \"type\" : \"record\",\n" +
                "      \"name\" : \"B\",\n" +
                "      \"namespace\" : \"ren.wenchao.jschema.NestedClassSchemaTest\",\n" +
                "      \"types\" : { },\n" +
                "      \"fields\" : [ {\n" +
                "        \"name\" : \"bObject\",\n" +
                "        \"type\" : \"java.lang.Object\"\n" +
                "      }, {\n" +
                "        \"name\" : \"ba1\",\n" +
                "        \"type\" : \"ren.wenchao.jschema.NestedClassSchemaTest.A\"\n" +
                "      }, {\n" +
                "        \"name\" : \"ba2\",\n" +
                "        \"type\" : \"ren.wenchao.jschema.NestedClassSchemaTest.A\"\n" +
                "      }, {\n" +
                "        \"name\" : \"enumB\",\n" +
                "        \"type\" : \"ren.wenchao.jschema.NestedClassSchemaTest.Enum\"\n" +
                "      } ]\n" +
                "    },\n" +
                "    \"ren.wenchao.jschema.NestedClassSchemaTest.C\" : {\n" +
                "      \"type\" : \"record\",\n" +
                "      \"name\" : \"C\",\n" +
                "      \"namespace\" : \"ren.wenchao.jschema.NestedClassSchemaTest\",\n" +
                "      \"types\" : { },\n" +
                "      \"fields\" : [ {\n" +
                "        \"name\" : \"a\",\n" +
                "        \"type\" : \"ren.wenchao.jschema.NestedClassSchemaTest.A\"\n" +
                "      }, {\n" +
                "        \"name\" : \"b\",\n" +
                "        \"type\" : \"ren.wenchao.jschema.NestedClassSchemaTest.B\"\n" +
                "      }, {\n" +
                "        \"name\" : \"cObject\",\n" +
                "        \"type\" : \"java.lang.Object\"\n" +
                "      }, {\n" +
                "        \"name\" : \"enumC\",\n" +
                "        \"type\" : \"ren.wenchao.jschema.NestedClassSchemaTest.Enum\"\n" +
                "      } ]\n" +
                "    }\n" +
                "  },\n" +
                "  \"fields\" : [ {\n" +
                "    \"name\" : \"a\",\n" +
                "    \"type\" : \"ren.wenchao.jschema.NestedClassSchemaTest.A\"\n" +
                "  }, {\n" +
                "    \"name\" : \"b\",\n" +
                "    \"type\" : \"ren.wenchao.jschema.NestedClassSchemaTest.B\"\n" +
                "  }, {\n" +
                "    \"name\" : \"c\",\n" +
                "    \"type\" : \"ren.wenchao.jschema.NestedClassSchemaTest.C\"\n" +
                "  }, {\n" +
                "    \"name\" : \"dObject\",\n" +
                "    \"type\" : \"java.lang.Object\"\n" +
                "  }, {\n" +
                "    \"name\" : \"enumD\",\n" +
                "    \"type\" : \"ren.wenchao.jschema.NestedClassSchemaTest.Enum\"\n" +
                "  } ]\n" +
                "}", getPrettySchemaString(D.class));
    }


}
