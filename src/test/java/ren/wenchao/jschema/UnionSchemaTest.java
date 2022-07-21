package ren.wenchao.jschema;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;

public class UnionSchemaTest {

    @Test
    public void test1() {
        Assert.assertEquals("[ \"int\", \"string\" ]", TypeSchema.createUnion(Lists.newArrayList(
                TypeSchema.getSchema(int.class),
                TypeSchema.getSchema(String.class)
        )).toString(true));
    }


//    @Test
//    public void test2() {
//        Assert.assertEquals("[ \"int\", \"int\" ]", TypeSchema.createUnion(Lists.newArrayList(
//                TypeSchema.getSchema(int.class),
//                TypeSchema.getSchema(int.class)
//        )).toString(true));
//    }
}
