package ren.wenchao.jschema;

import java.util.Locale;

public enum SchemaType {
    // primitive types
    BYTE, SHORT, INT, LONG, FLOAT, DOUBLE, BOOLEAN, CHAR, STRING,

    //collection
    ARRAY, MAP, LIST, SET,
    //key word
    RECORD, BYTES, NULL, VOID, ENUM;

    private final String name;

    private SchemaType() {
        this.name = this.name().toLowerCase(Locale.ENGLISH);
    }

    public String getName() {
        return name;
    }
}
