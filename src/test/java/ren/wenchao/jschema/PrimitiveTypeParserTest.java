package ren.wenchao.jschema;

import org.junit.Assert;
import org.junit.Test;

public class PrimitiveTypeParserTest extends BaseTest {

    @Test
    public void test1() {
        TypeSchema typeSchema = parse(getPrettySchemaString(int.class));
        Assert.assertTrue(typeSchema instanceof IntSchema);
        Assert.assertEquals("\"int\"", typeSchema.toString());
    }

    @Test
    public void test2() {
        TypeSchema typeSchema = parse(getPrettySchemaString(Integer.class));
        Assert.assertTrue(typeSchema instanceof IntWrapperSchema);
        Assert.assertEquals("\"Integer\"", typeSchema.toString());
    }

    @Test
    public void test3() {
        TypeSchema typeSchema = parse(getPrettySchemaString(short.class));
        Assert.assertTrue(typeSchema instanceof ShortSchema);
        Assert.assertEquals("\"short\"", typeSchema.toString());
    }

    @Test
    public void test4() {
        TypeSchema typeSchema = parse(getPrettySchemaString(Short.class));
        Assert.assertTrue(typeSchema instanceof ShortWrapperSchema);
        Assert.assertEquals("\"Short\"", typeSchema.toString());
    }


    @Test
    public void test5() {
        TypeSchema typeSchema = parse(getPrettySchemaString(long.class));
        Assert.assertTrue(typeSchema instanceof LongSchema);
        Assert.assertEquals("\"long\"", typeSchema.toString());
    }

    @Test
    public void test6() {
        TypeSchema typeSchema = parse(getPrettySchemaString(Long.class));
        Assert.assertTrue(typeSchema instanceof LongWrapperSchema);
        Assert.assertEquals("\"Long\"", typeSchema.toString());
    }

    @Test
    public void test7() {
        TypeSchema typeSchema = parse(getPrettySchemaString(byte.class));
        Assert.assertTrue(typeSchema instanceof ByteSchema);
        Assert.assertEquals("\"byte\"", typeSchema.toString());
    }

    @Test
    public void test8() {
        TypeSchema typeSchema = parse(getPrettySchemaString(Byte.class));
        Assert.assertTrue(typeSchema instanceof ByteWrapperSchema);
        Assert.assertEquals("\"Byte\"", typeSchema.toString());
    }

    @Test
    public void test9() {
        TypeSchema typeSchema = parse(getPrettySchemaString(float.class));
        Assert.assertTrue(typeSchema instanceof FloatSchema);
        Assert.assertEquals("\"float\"", typeSchema.toString());
    }

    @Test
    public void test10() {
        TypeSchema typeSchema = parse(getPrettySchemaString(Float.class));
        Assert.assertTrue(typeSchema instanceof FloatWrapperSchema);
        Assert.assertEquals("\"Float\"", typeSchema.toString());
    }

    @Test
    public void test11() {
        TypeSchema typeSchema = parse(getPrettySchemaString(double.class));
        Assert.assertTrue(typeSchema instanceof DoubleSchema);
        Assert.assertEquals("\"double\"", typeSchema.toString());
    }

    @Test
    public void test12() {
        TypeSchema typeSchema = parse(getPrettySchemaString(Double.class));
        Assert.assertTrue(typeSchema instanceof DoubleWrapperSchema);
        Assert.assertEquals("\"Double\"", typeSchema.toString());
    }


    @Test
    public void test13() {
        TypeSchema typeSchema = parse(getPrettySchemaString(boolean.class));
        Assert.assertTrue(typeSchema instanceof BooleanSchema);
        Assert.assertEquals("\"boolean\"", typeSchema.toString());
    }


    @Test
    public void test14() {
        TypeSchema typeSchema = parse(getPrettySchemaString(Boolean.class));
        Assert.assertTrue(typeSchema instanceof BooleanWrapperSchema);
        Assert.assertEquals("\"Boolean\"", typeSchema.toString());
    }

    @Test
    public void test15() {
        TypeSchema typeSchema = parse(getPrettySchemaString(String.class));
        Assert.assertTrue(typeSchema instanceof StringSchema);
        Assert.assertEquals("\"String\"", typeSchema.toString());
    }

    @Test
    public void test16() {
        TypeSchema typeSchema = parse(TypeSchema.create(SchemaType.NULL).toString());
        Assert.assertTrue(typeSchema instanceof NullSchema);
        Assert.assertEquals("\"null\"", typeSchema.toString());
    }

}
