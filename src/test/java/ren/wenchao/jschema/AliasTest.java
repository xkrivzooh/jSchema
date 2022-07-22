package ren.wenchao.jschema;

import org.junit.Assert;
import org.junit.Test;

public class AliasTest {

    @Alias(alias = "a", space = "b")
    private static class AliasA {
    }

    @Alias(alias = "a", space = "")
    private static class AliasB {
    }

    @Alias(alias = "a")
    private static class AliasC {
    }

    @Test
    public void test1() {
        System.out.println(TypeSchema.getSchema(AliasA.class).toString(true));
        Assert.assertEquals("{\n" +
                "  \"type\" : \"record\",\n" +
                "  \"name\" : \"AliasA\",\n" +
                "  \"namespace\" : \"ren.wenchao.jschema.AliasTest\",\n" +
                "  \"types\" : { },\n" +
                "  \"fields\" : [ ],\n" +
                "  \"aliases\" : [ \"b.a\" ]\n" +
                "}", TypeSchema.getSchema(AliasA.class).toString(true));
        System.out.println(TypeSchema.getSchema(AliasB.class).toString(true));
        Assert.assertEquals("{\n" +
                "  \"type\" : \"record\",\n" +
                "  \"name\" : \"AliasB\",\n" +
                "  \"namespace\" : \"ren.wenchao.jschema.AliasTest\",\n" +
                "  \"types\" : { },\n" +
                "  \"fields\" : [ ],\n" +
                "  \"aliases\" : [ \"a\" ]\n" +
                "}", TypeSchema.getSchema(AliasB.class).toString(true));
        System.out.println(TypeSchema.getSchema(AliasC.class).toString(true));
        Assert.assertEquals("{\n" +
                "  \"type\" : \"record\",\n" +
                "  \"name\" : \"AliasC\",\n" +
                "  \"namespace\" : \"ren.wenchao.jschema.AliasTest\",\n" +
                "  \"types\" : { },\n" +
                "  \"fields\" : [ ],\n" +
                "  \"aliases\" : [ \"ren.wenchao.jschema.AliasTest.a\" ]\n" +
                "}", TypeSchema.getSchema(AliasC.class).toString(true));
    }


    @Alias(alias = "alias1", space = "space1")
    @Alias(alias = "alias2", space = "space2")
    private static class A {
    }

    @Test
    public void test2() {
        System.out.println(TypeSchema.getSchema(A.class).toString(true));
        Assert.assertEquals("{\n" +
                "  \"type\" : \"record\",\n" +
                "  \"name\" : \"A\",\n" +
                "  \"namespace\" : \"ren.wenchao.jschema.AliasTest\",\n" +
                "  \"types\" : { },\n" +
                "  \"fields\" : [ ],\n" +
                "  \"aliases\" : [ \"space1.alias1\", \"space2.alias2\" ]\n" +
                "}", TypeSchema.getSchema(A.class).toString(true));
    }

    private static class ClassWithAliasOnField {
        @Alias(alias = "aliasName")
        int primitiveField;
    }

    private static class ClassWithMultipleAliasesOnField {
        @Alias(alias = "alias1")
        @Alias(alias = "alias2")
        int primitiveField;
    }


    @Test
    public void test3() {
        System.out.println(TypeSchema.getSchema(ClassWithAliasOnField.class).toString(true));
        Assert.assertEquals("{\n" +
                "  \"type\" : \"record\",\n" +
                "  \"name\" : \"ClassWithAliasOnField\",\n" +
                "  \"namespace\" : \"ren.wenchao.jschema.AliasTest\",\n" +
                "  \"types\" : { },\n" +
                "  \"fields\" : [ {\n" +
                "    \"name\" : \"primitiveField\",\n" +
                "    \"type\" : \"int\",\n" +
                "    \"aliases\" : [ \"aliasName\" ]\n" +
                "  } ]\n" +
                "}", TypeSchema.getSchema(ClassWithAliasOnField.class).toString(true));

        System.out.println(TypeSchema.getSchema(ClassWithMultipleAliasesOnField.class).toString(true));
        Assert.assertEquals("{\n" +
                "  \"type\" : \"record\",\n" +
                "  \"name\" : \"ClassWithMultipleAliasesOnField\",\n" +
                "  \"namespace\" : \"ren.wenchao.jschema.AliasTest\",\n" +
                "  \"types\" : { },\n" +
                "  \"fields\" : [ {\n" +
                "    \"name\" : \"primitiveField\",\n" +
                "    \"type\" : \"int\",\n" +
                "    \"aliases\" : [ \"alias1\", \"alias2\" ]\n" +
                "  } ]\n" +
                "}", TypeSchema.getSchema(ClassWithMultipleAliasesOnField.class).toString(true));
    }

    private static class ClassWithAliasAndNamespaceOnField {
        @Alias(alias = "aliasName", space = "forbidden.space.entry")
        int primitiveField;
    }

    @Test(expected = SchemaRuntimeException.class)
    public void test4() {
        TypeSchema.getSchema(ClassWithAliasAndNamespaceOnField.class).toString(true);
    }
}
