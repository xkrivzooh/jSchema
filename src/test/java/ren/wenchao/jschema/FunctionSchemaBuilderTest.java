package ren.wenchao.jschema;

import org.junit.Assert;
import org.junit.Test;

public class FunctionSchemaBuilderTest {


    @Test
    public void test1() {
        FunctionSchema functionSchema = FunctionSchemaBuilder.builder()
                .namespace("ren.wenchao.jschema.FunctionSchemaTest")
                .name("A")
                .functionName("f1")
                .doc("doc")
                .requestParameter("arg0", "arg0Doc", TypeSchema.getSchema(int.class))
                .requestParameter("arg1", "arg1Doc", TypeSchema.getSchema(int.class))
                .response(TypeSchema.getSchema(int.class))
                .build();
        System.out.println(functionSchema.toString(true));
        Assert.assertEquals("{\n" +
                "  \"type\" : \"function\",\n" +
                "  \"namespace\" : null,\n" +
                "  \"name\" : \"A\",\n" +
                "  \"functionName\" : \"f1\",\n" +
                "  \"doc\" : \"doc\",\n" +
                "  \"request\" : {\n" +
                "    \"arg0\" : \"int\",\n" +
                "    \"arg1\" : \"int\"\n" +
                "  },\n" +
                "  \"response\" : \"int\"\n" +
                "}", functionSchema.toString(true));

    }
}