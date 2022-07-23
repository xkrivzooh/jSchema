package ren.wenchao.jschema;

import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void test1() {
        String schemaString = "{\n" +
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
                "}";

        TypeSchema typeSchema = TypeSchemaParser.parse(schemaString);

        Assert.assertEquals("D", typeSchema.getName());
        Assert.assertEquals("ren.wenchao.jschema.NestedClassSchemaTest", typeSchema.getNamespace());
        Assert.assertEquals("ren.wenchao.jschema.NestedClassSchemaTest.D", typeSchema.getFullName());
        Assert.assertEquals(5, typeSchema.getFields().size());

        Field enumD = typeSchema.getField("enumD");
        Assert.assertNotNull(enumD);
        TypeSchema enumDSchema = enumD.schema();
        Assert.assertEquals("ren.wenchao.jschema.NestedClassSchemaTest.Enum", enumDSchema.getFullName());



        System.out.println("");
    }
}
