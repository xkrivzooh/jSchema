package ren.wenchao.jschema;

import org.junit.Test;
import ren.wenchao.jschema.helpers.A;

import static org.junit.Assert.assertEquals;

public class ClassTypeSchemaTest {

    @Test
    public void test1() {
        String schema = TypeSchema.getSchema(A.class).toString(true);
        assertEquals("{\n" +
                "  \"type\" : \"record\",\n" +
                "  \"name\" : \"A\",\n" +
                "  \"namespace\" : \"ren.wenchao.jschema.helpers\",\n" +
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
}
