package ren.wenchao.jschema;

import com.google.common.collect.Maps;
import org.junit.Test;

import static org.junit.Assert.*;

public class CollectionTypeSchemaTest {

    @Test
    public void test_array_schema_create() {
        TypeSchema schema = TypeSchema.createSchema(int.class, Maps.newHashMap());
        System.out.println(schema);
        schema.addProp("k", "v");
        System.out.println(schema);
    }

}
