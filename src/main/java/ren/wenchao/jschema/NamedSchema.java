package ren.wenchao.jschema;

import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

abstract class NamedSchema extends TypeSchema {
    final NameWrapper name;
    final String doc;
    Set<NameWrapper> aliases;

    public NamedSchema(SchemaType type, NameWrapper name, String doc) {
        super(type);
        this.name = name;
        this.doc = doc;
        if (Primitives.PRIMITIVES.containsKey(name.getFull())) {
            throw new SchemaRuntimeException("Schemas may not be named after primitives: " + name.getFull());
        }
    }

    @Override
    public String getName() {
        return name.getName();
    }

    @Override
    public String getDoc() {
        return doc;
    }

    @Override
    public String getNamespace() {
        return name.getSpace();
    }

    @Override
    public String getFullName() {
        return name.getFull();
    }

    @Override
    public void addAlias(String alias) {
        addAlias(alias, null);
    }

    @Override
    public void addAlias(String name, String space) {
        if (aliases == null) this.aliases = new LinkedHashSet<>();
        if (space == null) space = this.name.getSpace();
        aliases.add(new NameWrapper(name, space));
    }

    @Override
    public Set<String> getAliases() {
        Set<String> result = new LinkedHashSet<>();
        if (aliases != null) for (NameWrapper alias : aliases)
            result.add(alias.getFull());
        return result;
    }

    public boolean writeNameRef(Names names, JsonGenerator gen) throws IOException {
        if (this.equals(names.get(name))) {
            gen.writeString(name.getQualified(names.space()));
            return true;
        } else if (name.getName() != null) {
            names.put(name, this);
        }
        return false;
    }

    public void writeName(Names names, JsonGenerator gen) throws IOException {
        name.writeName(names, gen);
    }

    public boolean equalNames(NamedSchema that) {
        return this.name.equals(that.name);
    }

    @Override
    int computeHash() {
        return super.computeHash() + name.hashCode();
    }

    public void aliasesToJson(JsonGenerator gen) throws IOException {
        if (aliases == null || aliases.size() == 0) return;
        gen.writeFieldName("aliases");
        gen.writeStartArray();
        for (NameWrapper alias : aliases)
            gen.writeString(alias.getQualified(name.getSpace()));
        gen.writeEndArray();
    }

}
