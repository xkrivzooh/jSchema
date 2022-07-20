package ren.wenchao.jschema;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RecordSchemaTest {

    @Test
    public void testRecordWithNullDoc() {
        TypeSchema schema = TypeSchema.createRecord("name", null, "namespace", false);
        String schemaString = schema.toString();
        assertNotNull(schemaString);
    }

    @Test
    public void testRecordWithNullNamespace() {
        TypeSchema schema = TypeSchema.createRecord("name", "doc", null, false);
        String schemaString = schema.toString();
        assertNotNull(schemaString);
    }

    private TypeSchema createDefaultRecord() {
        return TypeSchema.createRecord("name", "doc", "namespace", false);
    }

    @Test
    public void testEmptyRecordSchema() {
        TypeSchema schema = createDefaultRecord();
        String schemaString = schema.toString();
        assertNotNull(schemaString);
    }

    @Test
    public void testSchemaWithFields() {
        List<Field> fields = new ArrayList<>();
        fields.add(new Field("field_name1", TypeSchema.create(SchemaType.NULL), null, null));
        fields.add(new Field("field_name2", TypeSchema.create(SchemaType.INT), null, null));
        TypeSchema schema = createDefaultRecord();
        schema.setFields(fields);
        String schemaString = schema.toString();
        assertNotNull(schemaString);
        assertEquals(2, schema.getFields().size());
    }

    @Test(expected = NullPointerException.class)
    public void testSchemaWithNullFields() {
        TypeSchema.createRecord("name", "doc", "namespace", false, null);
    }

    @Test
    public void testDefaultRecordWithDuplicateFieldName() {
        String recordName = "name";
        TypeSchema schema = TypeSchema.createRecord(recordName, "doc", "namespace", false);
        List<Field> fields = new ArrayList<>();
        fields.add(new Field("field_name", TypeSchema.create(SchemaType.NULL), null, null));
        fields.add(new Field("field_name", TypeSchema.create(SchemaType.INT), null, null));
        try {
            schema.setFields(fields);
            fail("Should not be able to create a record with duplicate field name.");
        } catch (SchemaRuntimeException are) {
            assertTrue(are.getMessage().contains("Duplicate field field_name in record " + recordName));
        }
    }

}
