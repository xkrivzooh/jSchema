package ren.wenchao.jschema;

import org.junit.Test;

import static org.junit.Assert.*;

public class TypeSchemaTest {

    @Test
    public void test1() {
        TypeSchema schema = TypeSchema.getSchema(TypeSchema.class);
        System.out.println(schema.toString(true));
    }
}