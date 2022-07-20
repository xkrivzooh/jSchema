package ren.wenchao.jschema;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        PrimitiveTypeSchemaTest.class,
        ClassTypeSchemaTest.class,
        CollectionTypeSchemaTest.class,
        RecordSchemaTest.class,
        FieldSchemaTest.class
})
public class AllTest {
}
