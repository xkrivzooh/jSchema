package ren.wenchao.jschema;

public class TypeSchemaParser {

    public static TypeSchema parse(String schemaString) {
        return new Parser().parse(schemaString);
    }
}
