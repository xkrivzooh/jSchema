package ren.wenchao.jschema;

import org.junit.Assert;
import org.junit.Test;

public class FunctionSchemaBuilderTest {


    @Test
    public void test1() {
        Parameter parameter3 = new Parameter();
        parameter3.setName("arg2");
        parameter3.setDoc("arg2Doc");
        parameter3.addProp("arg2K1", "arg2K2");
        parameter3.setSchema(TypeSchema.getSchema(int.class));

        FunctionSchemaBuilder builder = FunctionSchemaBuilder.builder()
                .namespace("ren.wenchao.jschema.FunctionSchemaTest")
                .name("A")
                .functionName("f1")
                .doc("doc")
                .requestParameter("arg0", "arg0Doc", TypeSchema.getSchema(int.class))
                .requestParameter("arg1", "arg1Doc", TypeSchema.getSchema(int.class))
                .requestParameter(parameter3)
                .response(TypeSchema.getSchema(int.class));
        FunctionSchema functionSchema = builder.build();
        System.out.println(functionSchema.toString(true));
        Assert.assertEquals("{\n" +
                "  \"type\" : \"function\",\n" +
                "  \"namespace\" : null,\n" +
                "  \"name\" : \"A\",\n" +
                "  \"functionName\" : \"f1\",\n" +
                "  \"doc\" : \"doc\",\n" +
                "  \"request\" : {\n" +
                "    \"arg0\" : {\n" +
                "      \"doc\" : \"arg0Doc\",\n" +
                "      \"type\" : \"int\"\n" +
                "    },\n" +
                "    \"arg1\" : {\n" +
                "      \"doc\" : \"arg1Doc\",\n" +
                "      \"type\" : \"int\"\n" +
                "    },\n" +
                "    \"arg2\" : {\n" +
                "      \"doc\" : \"arg2Doc\",\n" +
                "      \"arg2K1\" : \"arg2K2\",\n" +
                "      \"type\" : \"int\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"response\" : \"int\"\n" +
                "}", functionSchema.toString(true));

    }
}