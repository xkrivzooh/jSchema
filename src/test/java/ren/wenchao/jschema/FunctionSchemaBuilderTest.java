package ren.wenchao.jschema;

import org.junit.Assert;
import org.junit.Test;
import ren.wenchao.jschema.constraints.NotNull;

public class FunctionSchemaBuilderTest {


    @Test
    public void test1() {
        Parameter parameter3 = new Parameter();
        parameter3.setName("arg2");
        parameter3.setDoc("arg2Doc");
        parameter3.addProp("arg2K1", "arg2K2");
        parameter3.setSchema(TypeSchema.getSchema(Integer.class));
        parameter3.addConstraint(new NotNull("arg2不能为空"));
        parameter3.setDefaultValue("1");


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
                "  \"namespace\" : \"ren.wenchao.jschema.FunctionSchemaTest\",\n" +
                "  \"name\" : \"A\",\n" +
                "  \"functionName\" : \"f1\",\n" +
                "  \"doc\" : \"doc\",\n" +
                "  \"request\" : {\n" +
                "    \"arg0\" : {\n" +
                "      \"doc\" : \"arg0Doc\",\n" +
                "      \"props\" : { },\n" +
                "      \"constraints\" : { },\n" +
                "      \"type\" : \"int\",\n" +
                "      \"pos\" : \"0\"\n" +
                "    },\n" +
                "    \"arg1\" : {\n" +
                "      \"doc\" : \"arg1Doc\",\n" +
                "      \"props\" : { },\n" +
                "      \"constraints\" : { },\n" +
                "      \"type\" : \"int\",\n" +
                "      \"pos\" : \"1\"\n" +
                "    },\n" +
                "    \"arg2\" : {\n" +
                "      \"doc\" : \"arg2Doc\",\n" +
                "      \"props\" : {\n" +
                "        \"arg2K1\" : \"arg2K2\"\n" +
                "      },\n" +
                "      \"constraints\" : {\n" +
                "        \"NotNull\" : {\n" +
                "          \"message\" : \"arg2不能为空\"\n" +
                "        }\n" +
                "      },\n" +
                "      \"type\" : \"Integer\",\n" +
                "      \"default\" : \"1\",\n" +
                "      \"pos\" : \"2\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"response\" : \"int\"\n" +
                "}", functionSchema.toString(true));

    }
}