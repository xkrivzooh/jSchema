package ren.wenchao.jschema;

import org.junit.Test;

public class SchemaParserTest extends BaseTest{

    @Test(expected = SchemaRuntimeException.class)
    public void testParseEmptySchema() {
        TypeSchemaParser.parse("");
    }
}
