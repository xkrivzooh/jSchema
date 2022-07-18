package ren.wenchao.jschema;

import org.junit.Test;

import static org.junit.Assert.*;

public class CollectionTypeSchemaTest {

    @Test
    public void test_array_schema_create() {
        String schema = TypeSchema.getSchema(int[].class);
        assertEquals("\"int\"", schema);
    }

}
