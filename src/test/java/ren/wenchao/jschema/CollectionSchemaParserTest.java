package ren.wenchao.jschema;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CollectionSchemaParserTest extends BaseTest {

    @Test
    public void testArray1() {
        TypeSchema typeSchema = TypeSchema.createArray(TypeSchema.getSchema(Integer.class));
        System.out.println(typeSchema.toString(true));
        TypeSchema parse = TypeSchemaParser.parse(typeSchema.toString());
        System.out.println(parse.toString(true));
        Assert.assertEquals(typeSchema.toString(), parse.toString());
    }

    @Test
    public void testArray2() {
        TypeSchema typeSchema = TypeSchema.createArray(TypeSchema.getSchema(int.class));
        System.out.println(typeSchema.toString(true));
        TypeSchema parse = TypeSchemaParser.parse(typeSchema.toString());
        System.out.println(parse.toString(true));
        Assert.assertEquals(typeSchema.toString(), parse.toString());
    }

    @Test
    public void testArray3() {
        TypeSchema typeSchema = TypeSchema.createArray(TypeSchema.getSchema(Object.class));
        System.out.println(typeSchema.toString(true));
        String schemaString = typeSchema.toString();
        TypeSchema parse = TypeSchemaParser.parse(schemaString);
        System.out.println(parse.toString(true));
        Assert.assertEquals(schemaString, parse.toString());
    }


    static class A {
        int a;
    }

    @Test
    public void test2() {
        String s = TypeSchemaParser.parse(TypeSchemaParser.parse(TypeSchema.createArray(TypeSchema.getSchema(A.class)).toString()).toString(true)).toString(true);
        Assert.assertEquals("{\n" +
                "  \"type\" : \"array\",\n" +
                "  \"items\" : {\n" +
                "    \"type\" : \"record\",\n" +
                "    \"name\" : \"A\",\n" +
                "    \"namespace\" : \"ren.wenchao.jschema.CollectionSchemaParserTest\",\n" +
                "    \"types\" : { },\n" +
                "    \"fields\" : [ {\n" +
                "      \"name\" : \"a\",\n" +
                "      \"type\" : \"int\"\n" +
                "    } ]\n" +
                "  }\n" +
                "}", s);
    }

    static enum Enum {
        a,b
    }

    static class B {
        int a;
        Integer a1;
        short aa;
        Short aa1;
        byte b;
        Byte b1;
        boolean c;
        Boolean c1;
        float d;
        Float d1;
        double e;
        Double e1;
        char f;
        Character f1;
        String ss;
        long ll;
        Long ll1;
        Object h;
        List<Integer> i;
        Map<Integer, Integer> g;
        Set<Integer> k;
        Object[] objs;
        Enum anEnum;
    }

    @Test
    public void test3() {
        String prettySchemaString = getPrettySchemaString(B.class);
        System.out.println(prettySchemaString);
        String parsed = parse(prettySchemaString).toString(true);
        Assert.assertEquals(prettySchemaString, parsed);
    }


    static class C {
//        int a;
//        Integer a1;
//        short aa;
//        Short aa1;
//        byte b;
//        Byte b1;
//        boolean c;
//        Boolean c1;
//        float d;
//        Float d1;
//        double e;
//        Double e1;
//        char f;
//        Character f1;
//        String ss;
//        long ll;
//        Long ll1;
//        Object h;
//        List<Integer> i;
//        Map<Integer, Integer> g;
//        Set<Integer> k;
        Object[] objs;
//        Enum anEnum;
        B bObject;
    }

    @Test
    public void test4() {
        String prettySchemaString = getPrettySchemaString(C.class);
        String parsed = parse(prettySchemaString).toString(true);
        System.out.println(parsed);
        Assert.assertEquals(prettySchemaString, parsed);
    }

}
