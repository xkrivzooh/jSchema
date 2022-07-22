package ren.wenchao.jschema;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;

public class StringableTest extends BaseTest{

    @Stringable
    static class A {
        int a;
    }

    @Test
    public void test1() {
        Assert.assertEquals("{\n" +
                "  \"type\" : \"String\",\n" +
                "  \"java-class\" : \"ren.wenchao.jschema.StringableTest$A\"\n" +
                "}", getPrettySchemaString(A.class));
    }

    static class B {
        @Stringable
        BigDecimal bigDecimal;
        BigDecimal bigDecimal1;
        @Stringable
        BigInteger bigInteger;
        BigInteger bigInteger1;
        @Stringable
        File file;
        File file1;
        @Stringable
        URI uri;
        URI uri1;
        @Stringable
        URL url;
        URL url1;
        @Stringable
        Object object;
        Object object1;
    }

    @Test
    public void test2() {
        Assert.assertEquals("{\n" +
                "  \"type\" : \"record\",\n" +
                "  \"name\" : \"B\",\n" +
                "  \"namespace\" : \"ren.wenchao.jschema.StringableTest\",\n" +
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
                "    \"name\" : \"bigDecimal\",\n" +
                "    \"type\" : \"String\"\n" +
                "  }, {\n" +
                "    \"name\" : \"bigDecimal1\",\n" +
                "    \"type\" : {\n" +
                "      \"type\" : \"String\",\n" +
                "      \"java-class\" : \"java.math.BigDecimal\"\n" +
                "    }\n" +
                "  }, {\n" +
                "    \"name\" : \"bigInteger\",\n" +
                "    \"type\" : \"String\"\n" +
                "  }, {\n" +
                "    \"name\" : \"bigInteger1\",\n" +
                "    \"type\" : {\n" +
                "      \"type\" : \"String\",\n" +
                "      \"java-class\" : \"java.math.BigInteger\"\n" +
                "    }\n" +
                "  }, {\n" +
                "    \"name\" : \"file\",\n" +
                "    \"type\" : \"String\"\n" +
                "  }, {\n" +
                "    \"name\" : \"file1\",\n" +
                "    \"type\" : {\n" +
                "      \"type\" : \"String\",\n" +
                "      \"java-class\" : \"java.io.File\"\n" +
                "    }\n" +
                "  }, {\n" +
                "    \"name\" : \"object\",\n" +
                "    \"type\" : \"String\"\n" +
                "  }, {\n" +
                "    \"name\" : \"object1\",\n" +
                "    \"type\" : \"java.lang.Object\"\n" +
                "  }, {\n" +
                "    \"name\" : \"uri\",\n" +
                "    \"type\" : \"String\"\n" +
                "  }, {\n" +
                "    \"name\" : \"uri1\",\n" +
                "    \"type\" : {\n" +
                "      \"type\" : \"String\",\n" +
                "      \"java-class\" : \"java.net.URI\"\n" +
                "    }\n" +
                "  }, {\n" +
                "    \"name\" : \"url\",\n" +
                "    \"type\" : \"String\"\n" +
                "  }, {\n" +
                "    \"name\" : \"url1\",\n" +
                "    \"type\" : {\n" +
                "      \"type\" : \"String\",\n" +
                "      \"java-class\" : \"java.net.URL\"\n" +
                "    }\n" +
                "  } ]\n" +
                "}", getPrettySchemaString(B.class));
    }
}
