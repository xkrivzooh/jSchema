package ren.wenchao.jschema;

import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class EnumSchema extends NamedSchema {
    private final List<String> symbols;
    private final Map<String, Integer> ordinals;
    private final String enumDefault;

    public EnumSchema(NameWrapper name, String doc, LockableArrayList<String> symbols, String enumDefault) {
        super(SchemaType.ENUM, name, doc);
        this.symbols = symbols.lock();
        this.ordinals = new HashMap<>(Math.multiplyExact(2, symbols.size()));
        this.enumDefault = enumDefault;
        int i = 0;
        for (String symbol : symbols) {
            if (ordinals.put(NameUtils.validateName(symbol), i++) != null) {
                throw new SchemaParseException("Duplicate enum symbol: " + symbol);
            }
        }
        if (enumDefault != null && !symbols.contains(enumDefault)) {
            throw new SchemaParseException(
                    "The Enum Default: " + enumDefault + " is not in the enum symbol set: " + symbols);
        }
    }

    @Override
    public List<String> getEnumSymbols() {
        return symbols;
    }

    @Override
    public boolean hasEnumSymbol(String symbol) {
        return ordinals.containsKey(symbol);
    }

    @Override
    public int getEnumOrdinal(String symbol) {
        return ordinals.get(symbol);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof EnumSchema)) {
            return false;
        }
        EnumSchema that = (EnumSchema) o;
        return equalCachedHash(that) && equalNames(that) && symbols.equals(that.symbols) && propsEqual(that);
    }

    @Override
    public String getEnumDefault() {
        return enumDefault;
    }

    @Override
    int computeHash() {
        return super.computeHash() + symbols.hashCode();
    }

    @Override
    void toJson(Names names, JsonGenerator gen) throws IOException {
        if (writeNameRef(names, gen)) {
            return;
        }
        gen.writeStartObject();
        gen.writeStringField("type", "enum");
        writeName(names, gen);
        if (getDoc() != null) {
            gen.writeStringField("doc", getDoc());
        }
        gen.writeArrayFieldStart("symbols");
        for (String symbol : symbols) {
            gen.writeString(symbol);
        }
        gen.writeEndArray();
        if (getEnumDefault() != null) {
            gen.writeStringField("default", getEnumDefault());
        }
        writeProps(gen);
        aliasesToJson(gen);
        gen.writeEndObject();
    }
}
