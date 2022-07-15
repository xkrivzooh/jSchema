package ren.wenchao.jschema;


import java.util.HashMap;
import java.util.Map;

class Primitives {

    static final Map<String, SchemaType> PRIMITIVES = new HashMap<>();

    static {
        PRIMITIVES.put("string", SchemaType.STRING);
        PRIMITIVES.put("bytes", SchemaType.BYTES);
        PRIMITIVES.put("int", SchemaType.INT);
        PRIMITIVES.put("long", SchemaType.LONG);
        PRIMITIVES.put("float", SchemaType.FLOAT);
        PRIMITIVES.put("double", SchemaType.DOUBLE);
        PRIMITIVES.put("boolean", SchemaType.BOOLEAN);
        PRIMITIVES.put("null", SchemaType.NULL);
    }
}
