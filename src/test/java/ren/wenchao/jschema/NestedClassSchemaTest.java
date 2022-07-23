package ren.wenchao.jschema;

import org.junit.Test;

public class NestedClassSchemaTest extends BaseTest {

    static class A {
        Object aObject;
    }

    static class B {
        Object bObject;
        A a;
    }

    @Test
    public void test1() {
        System.out.println(getPrettySchemaString(B.class));
    }
}
