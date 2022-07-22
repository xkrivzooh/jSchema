package ren.wenchao.jschema;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SchemaParserTest extends BaseTest{

    @Test(expected = SchemaRuntimeException.class)
    public void testParseEmptySchema() {
        TypeSchemaParser.parse("");
    }

    @Test
    public void testNull() throws Exception {
        assertEquals(TypeSchema.create(SchemaType.NULL), TypeSchemaParser.parse("\"null\""));
        assertEquals(TypeSchema.create(SchemaType.NULL), TypeSchemaParser.parse("{\"type\":\"null\"}"));
    }

}
