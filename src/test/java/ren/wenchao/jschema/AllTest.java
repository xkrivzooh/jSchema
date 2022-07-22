package ren.wenchao.jschema;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        PrimitiveTypeSchemaTest.class,
        ClassTypeSchemaTest.class,
        ArraySchemaTest.class,
        RecordSchemaTest.class,
        FieldSchemaTest.class,
        MapSchemaTest.class,
        ListSchemaTest.class,
        SetSchemaTest.class,
        NullableTest.class,
        EnumSchemaTest.class,
        AliasTest.class
})
public class AllTest {
}
