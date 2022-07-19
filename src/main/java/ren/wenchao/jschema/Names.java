package ren.wenchao.jschema;

import java.util.LinkedHashMap;

class Names extends LinkedHashMap<NameWrapper, TypeSchema> {
    private static final long serialVersionUID = 1L;
    private String space; // default namespace

    public Names() {
    }

    public Names(String space) {
        this.space = space;
    }

    public String space() {
        return space;
    }

    public void space(String space) {
        this.space = space;
    }

    public TypeSchema get(String o) {
        SchemaType primitive = Primitives.PRIMITIVES.get(o);
        if (primitive != null) {
            return TypeSchema.create(primitive);
        }
        NameWrapper name = new NameWrapper(o, space);
        if (!containsKey(name)) {
            // if not in default try anonymous
            name = new NameWrapper(o, "");
        }
        return super.get(name);
    }

    public boolean contains(TypeSchema schema) {
        return get(((NamedSchema) schema).name) != null;
    }

    public void add(TypeSchema schema) {
        put(((NamedSchema) schema).name, schema);
    }

    @Override
    public TypeSchema put(NameWrapper name, TypeSchema schema) {
        if (containsKey(name)) throw new SchemaParseException("Can't redefine: " + name);
        return super.put(name, schema);
    }
}