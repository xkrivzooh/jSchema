package ren.wenchao.jschema;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;

public class FunctionSchemaTest {

    static class A {

        boolean f1() {
            return true;
        }

        String f2(int a, long c) {
            return "";
        }
    }

    @Test
    public void test1() throws NoSuchMethodException {
        Method f1 = A.class.getDeclaredMethod("f1");
        System.out.println(FunctionSchema.getSchema(f1).toString(true));
        Assert.assertEquals("{\n" +
                "  \"type\" : \"function\",\n" +
                "  \"namespace\" : \"ren.wenchao.jschema.FunctionSchemaTest\",\n" +
                "  \"name\" : \"A\",\n" +
                "  \"functionName\" : \"f1\",\n" +
                "  \"doc\" : null,\n" +
                "  \"request\" : { },\n" +
                "  \"response\" : \"boolean\"\n" +
                "}", FunctionSchema.getSchema(f1).toString(true));
    }

    @Test
    public void test2() throws NoSuchMethodException {
        Method f2 = A.class.getDeclaredMethod("f2", int.class, long.class);
        System.out.println(FunctionSchema.getSchema(f2).toString(true));
        Assert.assertEquals("{\n" +
                "  \"type\" : \"function\",\n" +
                "  \"namespace\" : \"ren.wenchao.jschema.FunctionSchemaTest\",\n" +
                "  \"name\" : \"A\",\n" +
                "  \"functionName\" : \"f2\",\n" +
                "  \"doc\" : null,\n" +
                "  \"request\" : {\n" +
                "    \"arg0\" : \"int\",\n" +
                "    \"arg1\" : \"long\"\n" +
                "  },\n" +
                "  \"response\" : \"String\"\n" +
                "}", FunctionSchema.getSchema(f2).toString(true));
    }
}