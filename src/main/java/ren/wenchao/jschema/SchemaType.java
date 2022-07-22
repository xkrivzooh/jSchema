package ren.wenchao.jschema;

import java.util.HashMap;
import java.util.Map;

public enum SchemaType {
    // primitive types
    BYTE, SHORT, INT, LONG, FLOAT, DOUBLE, BOOLEAN, CHAR, STRING,

    //wrapper types
    BYTE_WRAPPER,
    SHORT_WRAPPER,
    INT_WRAPPER,
    LONG_WRAPPER,
    FLOAT_WRAPPER,
    DOUBLE_WRAPPER,
    BOOLEAN_WRAPPER,
    CHAR_WRAPPER,

    //collection
    ARRAY, MAP, LIST, SET,
    //key word
    RECORD, BYTES, NULL, VOID, ENUM;

    private final String name;

    private SchemaType() {
        Map<String, String> nameMapping = new HashMap<>();
        nameMapping.put("BYTE", "byte");
        nameMapping.put("BYTE_WRAPPER", "Byte");
        nameMapping.put("BYTES", "bytes");

        nameMapping.put("SHORT", "short");
        nameMapping.put("SHORT_WRAPPER", "Short");
        nameMapping.put("INT", "int");
        nameMapping.put("INT_WRAPPER", "Integer");
        nameMapping.put("LONG", "long");
        nameMapping.put("LONG_WRAPPER", "Long");
        nameMapping.put("FLOAT", "float");
        nameMapping.put("FLOAT_WRAPPER", "Float");
        nameMapping.put("DOUBLE", "double");
        nameMapping.put("DOUBLE_WRAPPER", "Double");
        nameMapping.put("BOOLEAN", "boolean");
        nameMapping.put("BOOLEAN_WRAPPER", "Boolean");
        nameMapping.put("CHAR", "char");
        nameMapping.put("CHAR_WRAPPER", "Character");
        nameMapping.put("STRING", "String");

        nameMapping.put("ARRAY", "array");//no better name
        nameMapping.put("MAP", "Map");
        nameMapping.put("LIST", "List");
        nameMapping.put("SET", "Set");

        nameMapping.put("RECORD", "class");
        nameMapping.put("NULL", "null");
        nameMapping.put("VOID", "void");
        nameMapping.put("ENUM", "enum");

        this.name = nameMapping.get(this.name());
    }

    public String getName() {
        return name;
    }

}
