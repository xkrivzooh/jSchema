package ren.wenchao.jschema;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;
import ren.wenchao.jschema.constraints.Constraint;
import ren.wenchao.jschema.constraints.NotNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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
                "    \"arg0\" : {\n" +
                "      \"doc\" : \"\",\n" +
                "      \"props\" : { },\n" +
                "      \"constraints\" : { },\n" +
                "      \"type\" : \"int\"\n" +
                "    },\n" +
                "    \"arg1\" : {\n" +
                "      \"doc\" : \"\",\n" +
                "      \"props\" : { },\n" +
                "      \"constraints\" : { },\n" +
                "      \"type\" : \"long\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"response\" : \"String\"\n" +
                "}", FunctionSchema.getSchema(f2).toString(true));
    }

    @Test
    public void test3() {
        Parameter parameter1 = new Parameter();
        parameter1.setName("arg1");
        parameter1.setDoc("arg1");
        parameter1.addProp("arg1K1", "arg1K1");
        parameter1.setSchema(TypeSchema.getSchema(Integer.class));
        parameter1.addConstraint(new NotNull("arg1不能为空"));


        FunctionSchemaBuilder builder = FunctionSchemaBuilder.builder()
                .namespace("ren.wenchao.jschema.FunctionSchemaTest")
                .name("A")
                .functionName("f1")
                .doc("doc")
                .requestParameter(parameter1)
                .response(TypeSchema.getSchema(int.class));
        FunctionSchema functionSchema = builder.build();


        List<Object> values = Lists.newArrayList();
        values.add(null);
        List<String> errors = functionSchema.validate(values, false);
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("arg1不能为空", errors.get(0));
//        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//        Validator validator = factory.getValidator();
//        validator.validate()


    }
}