package ren.wenchao.jschema;

import org.junit.Assert;
import org.junit.Test;

public class DocTest extends BaseTest{

    private enum DocTestEnum {
        ENUM_1, ENUM_2
    }

    @Doc("DocTest class docs")
    private static class A {
        @Doc("Some Documentation")
        int foo;

        @Doc("Some other Documentation")
        DocTestEnum enums;

        @Doc("And again")
        A defaultTest;
    }

    @Test
    public void test1() {
        Assert.assertEquals("{\n" +
                "  \"type\" : \"record\",\n" +
                "  \"name\" : \"A\",\n" +
                "  \"namespace\" : \"ren.wenchao.jschema.DocTest\",\n" +
                "  \"doc\" : \"DocTest class docs\",\n" +
                "  \"types\" : {\n" +
                "    \"ren.wenchao.jschema.DocTest.DocTestEnum\" : {\n" +
                "      \"type\" : \"enum\",\n" +
                "      \"name\" : \"DocTestEnum\",\n" +
                "      \"namespace\" : \"ren.wenchao.jschema.DocTest\",\n" +
                "      \"symbols\" : [ \"ENUM_1\", \"ENUM_2\" ]\n" +
                "    }\n" +
                "  },\n" +
                "  \"fields\" : [ {\n" +
                "    \"name\" : \"defaultTest\",\n" +
                "    \"type\" : \"ren.wenchao.jschema.DocTest.A\",\n" +
                "    \"doc\" : \"And again\"\n" +
                "  }, {\n" +
                "    \"name\" : \"enums\",\n" +
                "    \"type\" : \"ren.wenchao.jschema.DocTest.DocTestEnum\",\n" +
                "    \"doc\" : \"Some other Documentation\"\n" +
                "  }, {\n" +
                "    \"name\" : \"foo\",\n" +
                "    \"type\" : \"int\",\n" +
                "    \"doc\" : \"Some Documentation\"\n" +
                "  } ]\n" +
                "}", getPrettySchemaString(A.class));
    }
}
