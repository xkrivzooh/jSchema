package ren.wenchao.jschema;

import org.junit.Assert;
import org.junit.Test;
import ren.wenchao.jschema.constraints.NotNull;

public class FunctionSchemaParserTest {

    @Test
    public void test_parse() {
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
        String source = functionSchema.toString(true);
        System.out.println(source);

        String target = FunctionSchemaParser.parse(source).toString(true);
        Assert.assertEquals(source, target);

    }
}