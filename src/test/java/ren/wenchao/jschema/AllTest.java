package ren.wenchao.jschema;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        SchemaTypeTest.class,
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
        AliasTest.class,
        DocTest.class,
        IgnoreTest.class,
        MetaTest.class,
        NameTest.class,
        StringableTest.class,
        PrimitiveTypeParserTest.class
})
public class AllTest {
}
