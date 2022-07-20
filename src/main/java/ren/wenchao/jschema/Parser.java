package ren.wenchao.jschema;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.DoubleNode;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static ren.wenchao.jschema.Primitives.PRIMITIVES;

/**
 * A parser for JSON-format schemas. Each named schema parsed with a parser is
 * added to the names known to the parser so that subsequently parsed schemas
 * may refer to it by name.
 */
public class Parser {
    private Names names = new Names();
    private boolean validate = true;
    private boolean validateDefaults = true;

    /**
     * Adds the provided types to the set of defined, named types known to this
     * parser.
     */
    public Parser addTypes(Map<String, TypeSchema> types) {
        for (TypeSchema s : types.values()) {
            names.add(s);
        }
        return this;
    }

    /**
     * Returns the set of defined, named types known to this parser.
     */
    public Map<String, TypeSchema> getTypes() {
        Map<String, TypeSchema> result = new LinkedHashMap<>();
        for (TypeSchema s : names.values()) {
            result.put(s.getFullName(), s);
        }
        return result;
    }

    /**
     * Enable or disable name validation.
     */
    public Parser setValidate(boolean validate) {
        this.validate = validate;
        return this;
    }

    /**
     * True iff names are validated. True by default.
     */
    public boolean getValidate() {
        return this.validate;
    }

    /**
     * Enable or disable default value validation.
     */
    public Parser setValidateDefaults(boolean validateDefaults) {
        this.validateDefaults = validateDefaults;
        return this;
    }

    /**
     * True iff default values are validated. False by default.
     */
    public boolean getValidateDefaults() {
        return this.validateDefaults;
    }

    /**
     * Parse a schema from the provided file. If named, the schema is added to the
     * names known to this parser.
     */
    public TypeSchema parse(File file) throws IOException {
        return parse(JacksonUtils.getFactory().createParser(file));
    }

    /**
     * Parse a schema from the provided stream. If named, the schema is added to the
     * names known to this parser. The input stream stays open after the parsing.
     */
    public TypeSchema parse(InputStream in) throws IOException {
        return parse(JacksonUtils.getFactory().createParser(in).disable(JsonParser.Feature.AUTO_CLOSE_SOURCE));
    }

    /**
     * Read a schema from one or more json strings
     */
    public TypeSchema parse(String s, String... more) {
        StringBuilder b = new StringBuilder(s);
        for (String part : more)
            b.append(part);
        return parse(b.toString());
    }

    /**
     * Parse a schema from the provided string. If named, the schema is added to the
     * names known to this parser.
     */
    public TypeSchema parse(String s) {
        try {
            return parse(JacksonUtils.getFactory().createParser(s));
        } catch (IOException e) {
            throw new SchemaParseException(e);
        }
    }

    private TypeSchema parse(JsonParser parser) throws IOException {
        boolean saved = NameUtils.validateNames.get();
        boolean savedValidateDefaults = FieldUtils.VALIDATE_DEFAULTS.get();
        try {
            NameUtils.validateNames.set(validate);
            FieldUtils.VALIDATE_DEFAULTS.set(validateDefaults);
            return parse(JacksonUtils.MAPPER.readTree(parser), names);
        } catch (JsonParseException e) {
            throw new SchemaParseException(e);
        } finally {
            parser.close();
            NameUtils.validateNames.set(saved);
            FieldUtils.VALIDATE_DEFAULTS.set(savedValidateDefaults);
        }
    }

    static TypeSchema parse(JsonNode schema, Names names) {
        if (schema == null) {
            throw new SchemaParseException("Cannot parse <null> schema");
        }
        if (schema.isTextual()) { // name
            TypeSchema result = names.get(schema.textValue());
            if (result == null)
                throw new SchemaParseException("Undefined name: " + schema);
            return result;
        } else if (schema.isObject()) {
            TypeSchema result;
            String type = getRequiredText(schema, "type", "No type");
            NameWrapper name = null;
            String savedSpace = names.space();
            String doc = null;
            if (type.equals("record") || type.equals("error") || type.equals("enum") || type.equals("fixed")) {
                String space = getOptionalText(schema, "namespace");
                doc = getOptionalText(schema, "doc");
                if (space == null)
                    space = names.space();
                name = new NameWrapper(getRequiredText(schema, "name", "No name in schema"), space);
                names.space(name.getSpace()); // set default namespace
            }
            if (PRIMITIVES.containsKey(type)) { // primitive
                result = TypeSchema.create(PRIMITIVES.get(type));
            } else if (type.equals("record") || type.equals("error")) { // record
                List<Field> fields = new ArrayList<>();
                result = new RecordSchema(name, doc, type.equals("error"));
                if (name != null)
                    names.add(result);
                JsonNode fieldsNode = schema.get("fields");
                if (fieldsNode == null || !fieldsNode.isArray())
                    throw new SchemaParseException("Record has no fields: " + schema);
                for (JsonNode field : fieldsNode) {
                    String fieldName = getRequiredText(field, "name", "No field name");
                    String fieldDoc = getOptionalText(field, "doc");
                    JsonNode fieldTypeNode = field.get("type");
                    if (fieldTypeNode == null)
                        throw new SchemaParseException("No field type: " + field);
                    if (fieldTypeNode.isTextual() && names.get(fieldTypeNode.textValue()) == null)
                        throw new SchemaParseException(fieldTypeNode + " is not a defined name." + " The type of the \"" + fieldName
                                + "\" field must be" + " a defined name or a {\"type\": ...} expression.");
                    TypeSchema fieldSchema = parse(fieldTypeNode, names);
                    Field.Order order = Field.Order.ASCENDING;
                    JsonNode orderNode = field.get("order");
                    if (orderNode != null)
                        order = Field.Order.valueOf(orderNode.textValue().toUpperCase(Locale.ENGLISH));
                    JsonNode defaultValue = field.get("default");
                    if (defaultValue != null
                            && (SchemaType.FLOAT.equals(fieldSchema.getType()) || SchemaType.DOUBLE.equals(fieldSchema.getType()))
                            && defaultValue.isTextual())
                        defaultValue = new DoubleNode(Double.valueOf(defaultValue.textValue()));
                    Field f = new Field(fieldName, fieldSchema, fieldDoc, defaultValue, true, order);
                    Iterator<String> i = field.fieldNames();
                    while (i.hasNext()) { // add field props
                        String prop = i.next();
                        if (!TypeSchema.FIELD_RESERVED.contains(prop))
                            f.addProp(prop, field.get(prop));
                    }
                    f.setAliases(parseAliases(field));
                    fields.add(f);
                }
                result.setFields(fields);
            } else if (type.equals("enum")) { // enum
                JsonNode symbolsNode = schema.get("symbols");
                if (symbolsNode == null || !symbolsNode.isArray())
                    throw new SchemaParseException("Enum has no symbols: " + schema);
                LockableArrayList<String> symbols = new LockableArrayList<>(symbolsNode.size());
                for (JsonNode n : symbolsNode)
                    symbols.add(n.textValue());
                JsonNode enumDefault = schema.get("default");
                String defaultSymbol = null;
                if (enumDefault != null)
                    defaultSymbol = enumDefault.textValue();
                result = new EnumSchema(name, doc, symbols, defaultSymbol);
                if (name != null)
                    names.add(result);
            } else if (type.equals("array")) { // array
                JsonNode itemsNode = schema.get("items");
                if (itemsNode == null)
                    throw new SchemaParseException("Array has no items type: " + schema);
                result = new ArraySchema(parse(itemsNode, names));
            } else if (type.equals("map")) { // map
                JsonNode keysNode = schema.get("keys");
                if (keysNode == null)
                    throw new SchemaParseException("Map has no keys type: " + schema);
                JsonNode valuesNode = schema.get("values");
                if (valuesNode == null)
                    throw new SchemaParseException("Map has no values type: " + schema);
                result = new MapSchema(parse(keysNode, names), parse(valuesNode, names));
            }
            //todo 不支持fixed
//            else if (type.equals("fixed")) { // fixed
//                JsonNode sizeNode = schema.get("size");
//                if (sizeNode == null || !sizeNode.isInt())
//                    throw new SchemaParseException("Invalid or no size: " + schema);
//                result = new FixedSchema(name, doc, sizeNode.intValue());
//                if (name != null)
//                    names.add(result);
//            }
            else { // For unions with self reference
                NameWrapper nameFromType = new NameWrapper(type, names.space());
                if (names.containsKey(nameFromType)) {
                    return names.get(nameFromType);
                }
                throw new SchemaParseException("Type not supported: " + type);
            }
            Iterator<String> i = schema.fieldNames();

            Set reserved = TypeSchema.SCHEMA_RESERVED;
            if (type.equals("enum")) {
                reserved = TypeSchema.ENUM_RESERVED;
            }
            while (i.hasNext()) { // add properties
                String prop = i.next();
                if (!reserved.contains(prop)) // ignore reserved
                    result.addProp(prop, schema.get(prop));
            }
//             //parse logical type if present
//            result.logicalType = LogicalTypes.fromSchemaIgnoreInvalid(result);
            names.space(savedSpace); // restore space
            if (result instanceof NamedSchema) {
                Set<String> aliases = parseAliases(schema);
                if (aliases != null) // add aliases
                    for (String alias : aliases)
                        result.addAlias(alias);
            }
            return result;
        } else if (schema.isArray()) { // union
            LockableArrayList<TypeSchema> types = new LockableArrayList<>(schema.size());
            for (JsonNode typeNode : schema)
                types.add(parse(typeNode, names));
            return new UnionSchema(types);
        } else {
            throw new SchemaParseException("Schema not yet supported: " + schema);
        }
    }

    /**
     * Extracts text value associated to key from the container JsonNode, and throws
     * {@link SchemaParseException} if it doesn't exist.
     *
     * @param container Container where to find key.
     * @param key       Key to look for in container.
     * @param error     String to prepend to the SchemaParseException.
     */
    private static String getRequiredText(JsonNode container, String key, String error) {
        String out = getOptionalText(container, key);
        if (null == out) {
            throw new SchemaParseException(error + ": " + container);
        }
        return out;
    }

    /**
     * Extracts text value associated to key from the container JsonNode.
     */
    private static String getOptionalText(JsonNode container, String key) {
        JsonNode jsonNode = container.get(key);
        return jsonNode != null ? jsonNode.textValue() : null;
    }

    private static Set<String> parseAliases(JsonNode node) {
        JsonNode aliasesNode = node.get("aliases");
        if (aliasesNode == null)
            return null;
        if (!aliasesNode.isArray())
            throw new SchemaParseException("aliases not an array: " + node);
        Set<String> aliases = new LinkedHashSet<>();
        for (JsonNode aliasNode : aliasesNode) {
            if (!aliasNode.isTextual())
                throw new SchemaParseException("alias not a string: " + aliasNode);
            aliases.add(aliasNode.textValue());
        }
        return aliases;
    }


}
