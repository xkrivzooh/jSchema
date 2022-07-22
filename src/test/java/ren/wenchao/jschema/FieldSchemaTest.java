package ren.wenchao.jschema;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

public class FieldSchemaTest {

    @Test
    public void testIntDefaultValue() {
        Field field = new Field("myField", TypeSchema.create(SchemaType.INT), "doc", 1);
        assertTrue(field.hasDefaultValue());
        assertEquals(1, field.defaultVal());

        field = new Field("myField", TypeSchema.create(SchemaType.INT), "doc", Integer.MIN_VALUE);
        assertTrue(field.hasDefaultValue());
        assertEquals(Integer.MIN_VALUE, field.defaultVal());

        field = new Field("myField", TypeSchema.create(SchemaType.INT), "doc", Integer.MAX_VALUE);
        assertTrue(field.hasDefaultValue());
        assertEquals(Integer.MAX_VALUE, field.defaultVal());

        field = new Field("myField", TypeSchema.create(SchemaType.INT), "doc", null);
        assertFalse(field.hasDefaultValue());
//        assertEquals(Integer.MAX_VALUE, field.defaultVal());
    }

    @Test
    public void testValidLongAsIntDefaultValue() {
        Field field = new Field("myField", TypeSchema.create(SchemaType.INT), "doc", 1L);
        assertTrue(field.hasDefaultValue());
        assertEquals(1, field.defaultVal());

        field = new Field("myField", TypeSchema.create(SchemaType.INT), "doc", Long.valueOf(Integer.MIN_VALUE));
        assertTrue(field.hasDefaultValue());
        assertEquals(Integer.MIN_VALUE, field.defaultVal());

        field = new Field("myField", TypeSchema.create(SchemaType.INT), "doc", Long.valueOf(Integer.MAX_VALUE));
        assertTrue(field.hasDefaultValue());
        assertEquals(Integer.MAX_VALUE, field.defaultVal());
    }

    @Test(expected = SchemaRuntimeException.class)
    public void testInvalidLongAsIntDefaultValue() {
        new Field("myField", TypeSchema.create(SchemaType.INT), "doc", Integer.MAX_VALUE + 1L);
    }

    @Test(expected = SchemaRuntimeException.class)
    public void testDoubleAsIntDefaultValue() {
        new Field("myField", TypeSchema.create(SchemaType.INT), "doc", 1.0);
    }

    @Test
    public void testLongDefaultValue() {
        Field field = new Field("myField", TypeSchema.create(SchemaType.LONG), "doc", 1L);
        assertTrue(field.hasDefaultValue());
        assertEquals(1L, field.defaultVal());

        field = new Field("myField", TypeSchema.create(SchemaType.LONG), "doc", Long.MIN_VALUE);
        assertTrue(field.hasDefaultValue());
        assertEquals(Long.MIN_VALUE, field.defaultVal());

        field = new Field("myField", TypeSchema.create(SchemaType.LONG), "doc", Long.MAX_VALUE);
        assertTrue(field.hasDefaultValue());
        assertEquals(Long.MAX_VALUE, field.defaultVal());
    }

    @Test
    public void testIntAsLongDefaultValue() {
        Field field = new Field("myField", TypeSchema.create(SchemaType.LONG), "doc", 1);
        assertTrue(field.hasDefaultValue());
        assertEquals(1L, field.defaultVal());
    }

    @Test(expected = SchemaRuntimeException.class)
    public void testDoubleAsLongDefaultValue() {
        new Field("myField", TypeSchema.create(SchemaType.LONG), "doc", 1.0);
    }

    @Test
    public void testDoubleDefaultValue() {
        Field field = new Field("myField", TypeSchema.create(SchemaType.DOUBLE), "doc", 1.0);
        assertTrue(field.hasDefaultValue());
        assertEquals(1.0d, field.defaultVal());
    }

    @Test
    public void testIntAsDoubleDefaultValue() {
        Field field = new Field("myField", TypeSchema.create(SchemaType.DOUBLE), "doc", 1);
        assertTrue(field.hasDefaultValue());
        assertEquals(1.0d, field.defaultVal());
    }

    @Test
    public void testLongAsDoubleDefaultValue() {
        Field field = new Field("myField", TypeSchema.create(SchemaType.DOUBLE), "doc", 1L);
        assertTrue(field.hasDefaultValue());
        assertEquals(1.0d, field.defaultVal());
    }

    @Test
    public void testFloatAsDoubleDefaultValue() {
        Field field = new Field("myField", TypeSchema.create(SchemaType.DOUBLE), "doc", 1.0f);
        assertTrue(field.hasDefaultValue());
        assertEquals(1.0d, field.defaultVal());
    }


    @Test
    public void testFloatDefaultValue() {
        Field field = new Field("myField", TypeSchema.create(SchemaType.FLOAT), "doc", 1.0f);
        assertTrue(field.hasDefaultValue());
        assertEquals(1.0f, field.defaultVal());
    }

    @Test
    public void testIntAsFloatDefaultValue() {
        Field field = new Field("myField", TypeSchema.create(SchemaType.FLOAT), "doc", 1);
        assertTrue(field.hasDefaultValue());
        assertEquals(1.0f, field.defaultVal());
    }

    @Test
    public void testLongAsFloatDefaultValue() {
        Field field = new Field("myField", TypeSchema.create(SchemaType.FLOAT), "doc", 1L);
        assertTrue(field.hasDefaultValue());
        assertEquals(1.0f, field.defaultVal());
    }

    @Test
    public void testDoubleAsFloatDefaultValue() {
        Field field = new Field("myField", TypeSchema.create(SchemaType.FLOAT), "doc", 1.0d);
        assertTrue(field.hasDefaultValue());
        assertEquals(1.0f, field.defaultVal());
    }

    @Test(expected = NullPointerException.class)
    public void testEnumSymbolAsNull() {
        TypeSchema.createEnum("myField", "doc", "namespace", Collections.singletonList(null));
    }

    @Test
    public void testSchemaFieldWithoutSchema() {
        new Field("f", null);
    }

    @Test
    public void testQualifiedName() {
        Arrays.stream(SchemaType.values()).forEach((SchemaType t) -> {
            final NameWrapper name = new NameWrapper(t.getName(), "space");
            assertEquals("space." + t.getName(), name.getQualified("space"));
            assertEquals("space." + t.getName(), name.getQualified("otherdefault"));
        });
        final NameWrapper name = new NameWrapper("name", "space");
        assertEquals("space.name", name.getQualified("space"));
        assertEquals("space.name", name.getQualified("otherdefault"));

        final NameWrapper nameInt = new NameWrapper("Int", "space");
        assertEquals("space.Int", nameInt.getQualified("space"));
    }
}
