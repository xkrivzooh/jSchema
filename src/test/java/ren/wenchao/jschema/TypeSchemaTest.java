package ren.wenchao.jschema;

import org.junit.Test;

import static org.junit.Assert.*;

public class TypeSchemaTest {

    public static class User {
        String name;
        int age;
    }

    @Test
    public void test1() {
        TypeSchema schema = TypeSchema.getSchema(User.class);
        System.out.println(schema.toString(true));
    }
}