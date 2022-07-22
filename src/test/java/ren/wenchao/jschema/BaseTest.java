package ren.wenchao.jschema;

import java.lang.reflect.Type;

public class BaseTest {

    public String getPrettySchemaString(Type type) {
        return TypeSchema.getSchema(type).toString(true);
    }
}
