package ren.wenchao.jschema;

import org.junit.Ignore;
import org.junit.Test;

public class SchemaAnnotationTest extends BaseTest{

    static class A {
        @Schema(value = "long")
        int a;
    }

    @Test
    @Ignore
    public void test1() {
        System.out.println(getPrettySchemaString(A.class));
    }
}
