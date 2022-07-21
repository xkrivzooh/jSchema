package ren.wenchao.jschema;

import java.util.Locale;

public enum SchemaType {
    RECORD, BYTES, NULL,
    // primitive types
    BYTE, SHORT, INT, LONG, FLOAT, DOUBLE, BOOLEAN, CHAR, STRING,

    //collection
    ARRAY, MAP, LIST, SET,
    //key word
    VOID, ENUM
    ;

    private final String name;

    private SchemaType() {
        this.name = this.name().toLowerCase(Locale.ENGLISH);
    }

    public String getName() {
        return name;
    }
}
