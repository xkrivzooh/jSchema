package ren.wenchao.jschema;

import org.junit.Test;

public class ParserTest {

    @Test
    public void test1() {
        //language=JSON
        String json = "{\n" +
                "  \"type\": \"record\",\n" +
                "  \"name\": \"B\",\n" +
                "  \"namespace\": \"com.xkrivzooh.schema.avro.Test\",\n" +
                "  \"fields\": [\n" +
                "    {\n" +
                "      \"name\": \"a\",\n" +
                "      \"type\": {\n" +
                "        \"type\": \"record\",\n" +
                "        \"name\": \"A\",\n" +
                "        \"fields\": [\n" +
                "          {\n" +
                "            \"name\": \"aInt\",\n" +
                "            \"type\": \"int\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"aInteger\",\n" +
                "            \"type\": \"int\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"aInt\",\n" +
                "      \"type\": \"int\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"aInteger\",\n" +
                "      \"type\": \"int\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"aa\",\n" +
                "      \"type\": \"A\"\n" +
                "    }\n" +
                "  ]\n" +
                "}\n";

        TypeSchema typeSchema = new Parser().parse(json);
        System.out.println("");
    }
}
