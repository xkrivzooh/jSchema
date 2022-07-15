package ren.wenchao.jschema;


import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;
import java.util.Objects;

class Name {
    private final String name;
    private final String space;
    private final String full;

    public Name(String name, String space) {
        if (name == null) { // anonymous
            this.name = this.space = this.full = null;
            return;
        }
        int lastDot = name.lastIndexOf('.');
        if (lastDot < 0) { // unqualified name
            this.name = validateName(name);
        } else { // qualified name
            space = name.substring(0, lastDot); // get space from name
            this.name = validateName(name.substring(lastDot + 1, name.length()));
        }
        if ("".equals(space)) {
            space = null;
        }
        this.space = space;
        this.full = (this.space == null) ? this.name : this.space + "." + this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Name)) return false;
        Name that = (Name) o;
        return Objects.equals(full, that.full);
    }

    @Override
    public int hashCode() {
        return full == null ? 0 : full.hashCode();
    }

    @Override
    public String toString() {
        return full;
    }

    public void writeName(Names names, JsonGenerator gen) throws IOException {
        if (name != null) gen.writeStringField("name", name);
        if (space != null) {
            if (!space.equals(names.space())) gen.writeStringField("namespace", space);
        } else if (names.space() != null) { // null within non-null
            gen.writeStringField("namespace", "");
        }
    }

    public String getQualified(String defaultSpace) {
        return (space == null || space.equals(defaultSpace)) ? name : full;
    }

    private static String validateName(String name) {
        int length = name.length();
        if (length == 0) {
            throw new SchemaParseException("Empty name");
        }
        char first = name.charAt(0);
        if (!(Character.isLetter(first) || first == '_')) {
            throw new SchemaParseException("Illegal initial character: " + name);
        }
        for (int i = 1; i < length; i++) {
            char c = name.charAt(i);
            if (!(Character.isLetterOrDigit(c) || c == '_')) {
                throw new SchemaParseException("Illegal character in: " + name);
            }
        }
        return name;
    }

    public String getName() {
        return name;
    }

    public String getSpace() {
        return space;
    }

    public String getFull() {
        return full;
    }
}