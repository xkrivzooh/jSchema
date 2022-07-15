package ren.wenchao.jschema;

import java.util.Set;

abstract class NamedSchema extends TypeSchema {
    final Name name;
    final String doc;
    Set<Name> aliases;

    public NamedSchema(SchemaType type, Name name, String doc) {
        super(type);
        this.name = name;
        this.doc = doc;
        if (Primitives.PRIMITIVES.containsKey(name.getFull())) {
            throw new SchemaRuntimeException("Schemas may not be named after primitives: " + name.getFull());
        }
    }

//    @Override
//    public String getName() {
//        return name.name;
//    }
//
//    @Override
//    public String getDoc() {
//        return doc;
//    }
//
//    @Override
//    public String getNamespace() {
//        return name.space;
//    }
//
//    @Override
//    public String getFullName() {
//        return name.full;
//    }

//    @Override
//    public void addAlias(String alias) {
//        addAlias(alias, null);
//    }

//    @Override
//    public void addAlias(String name, String space) {
//        if (aliases == null) this.aliases = new LinkedHashSet<>();
//        if (space == null) space = this.name.space;
//        aliases.add(new Name(name, space));
//    }
//
//    @Override
//    public Set<String> getAliases() {
//        Set<String> result = new LinkedHashSet<>();
//        if (aliases != null) for (Name alias : aliases)
//            result.add(alias.full);
//        return result;
//    }

//    public boolean writeNameRef(Names names, JsonGenerator gen) throws IOException {
//        if (this.equals(names.get(name))) {
//            gen.writeString(name.getQualified(names.space()));
//            return true;
//        } else if (name.name != null) {
//            names.put(name, this);
//        }
//        return false;
//    }

//    public void writeName(Names names, JsonGenerator gen) throws IOException {
//        name.writeName(names, gen);
//    }

//    public boolean equalNames(NamedSchema that) {
//        return this.name.equals(that.name);
//    }

//    @Override
//    int computeHash() {
//        return super.computeHash() + name.hashCode();
//    }
//
//    public void aliasesToJson(JsonGenerator gen) throws IOException {
//        if (aliases == null || aliases.size() == 0) return;
//        gen.writeFieldName("aliases");
//        gen.writeStartArray();
//        for (Name alias : aliases)
//            gen.writeString(alias.getQualified(name.space));
//        gen.writeEndArray();
//    }

}
