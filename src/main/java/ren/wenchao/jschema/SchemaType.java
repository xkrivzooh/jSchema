package ren.wenchao.jschema;

import java.util.Locale;

public enum SchemaType {
    RECORD, ENUM, ARRAY, MAP, UNION, FIXED, STRING, BYTES, BYTE, SHORT, INT, LONG, FLOAT, DOUBLE, BOOLEAN, CHAR, CHARACTER, NULL;
    private final String name;

    private SchemaType() {
        this.name = this.name().toLowerCase(Locale.ENGLISH);
    }

    public String getName() {
        return name;
    }
}
