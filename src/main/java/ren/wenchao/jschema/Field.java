package ren.wenchao.jschema;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;

import java.util.*;

import static ren.wenchao.jschema.FieldUtils.validateDefault;
import static ren.wenchao.jschema.NameUtils.validateName;

/**
 * A field within a record.
 */
class Field extends JsonProperties {

    private static final Set<String> FIELD_RESERVED = Collections
            .unmodifiableSet(new HashSet<>(Arrays.asList("default", "doc", "name", "order", "type", "aliases")));

    static {
        Accessor.setAccessor(new Accessor.FieldAccessor() {
            @Override
            protected JsonNode defaultValue(Field field) {
                return field.defaultValue();
            }

            @Override
            protected Field createField(String name, TypeSchema schema, String doc, JsonNode defaultValue) {
                return new Field(name, schema, doc, defaultValue, true, Order.ASCENDING);
            }

            @Override
            protected Field createField(String name, TypeSchema schema, String doc, JsonNode defaultValue, boolean validate, Order order) {
                return new Field(name, schema, doc, defaultValue, validate, order);
            }
        });
    }

    /**
     * How values of this field should be ordered when sorting records.
     */
    public enum Order {
        ASCENDING, DESCENDING, IGNORE;
        private final String name;

        private Order() {
            this.name = this.name().toLowerCase(Locale.ENGLISH);
        }
    }

    ;

    /**
     * For Schema unions with a "null" type as the first entry, this can be used to
     * specify that the default for the union is null.
     */
    public static final Object NULL_DEFAULT_VALUE = new Object();

    private final String name; // name of the field.
    private int position = -1;
    private final TypeSchema schema;
    private final String doc;
    private final JsonNode defaultValue;
    private final Order order;
    private Set<String> aliases;

    Field(String name, TypeSchema schema, String doc, JsonNode defaultValue, boolean validateDefault, Order order) {
        super(FIELD_RESERVED);
        this.name = validateName(name);
        this.schema = schema;
        this.doc = doc;
        this.defaultValue = validateDefault ? validateDefault(name, schema, defaultValue) : defaultValue;
        this.order = Objects.requireNonNull(order, "Order cannot be null");
    }

    /**
     * Constructs a new Field instance with the same {@code name}, {@code doc},
     * {@code defaultValue}, and {@code order} as {@code field} has with changing
     * the schema to the specified one. It also copies all the {@code props} and
     * {@code aliases}.
     */
    public Field(Field field, TypeSchema schema) {
        this(field.name, schema, field.doc, field.defaultValue, true, field.order);
        putAll(field);
        if (field.aliases != null) aliases = new LinkedHashSet<>(field.aliases);
    }

    /**
     *
     */
    public Field(String name, TypeSchema schema) {
        this(name, schema, (String) null, (JsonNode) null, true, Order.ASCENDING);
    }

    /**
     *
     */
    public Field(String name, TypeSchema schema, String doc) {
        this(name, schema, doc, (JsonNode) null, true, Order.ASCENDING);
    }

    /**
     * @param defaultValue the default value for this field specified using the
     *                     mapping in {@link JsonProperties}
     */
    public Field(String name, TypeSchema schema, String doc, Object defaultValue) {
        this(name, schema, doc, defaultValue == NULL_DEFAULT_VALUE ? NullNode.getInstance() : JacksonUtils.toJsonNode(defaultValue), true, Order.ASCENDING);
    }

    /**
     * @param defaultValue the default value for this field specified using the
     *                     mapping in {@link JsonProperties}
     */
    public Field(String name, TypeSchema schema, String doc, Object defaultValue, Order order) {
        this(name, schema, doc, defaultValue == NULL_DEFAULT_VALUE ? NullNode.getInstance() : JacksonUtils.toJsonNode(defaultValue), true, Objects.requireNonNull(order));
    }

    public String name() {
        return name;
    }

    ;

    /**
     * The position of this field within the record.
     */
    public int pos() {
        return position;
    }

    /**
     * This field's {@link TypeSchema}.
     */
    public TypeSchema schema() {
        return schema;
    }

    /**
     * Field's documentation within the record, if set. May return null.
     */
    public String doc() {
        return doc;
    }

    /**
     * @return true if this Field has a default value set. Can be used to determine
     * if a "null" return from defaultVal() is due to that being the default
     * value or just not set.
     */
    public boolean hasDefaultValue() {
        return defaultValue != null;
    }

    JsonNode defaultValue() {
        return defaultValue;
    }

    /**
     * @return the default value for this field specified using the mapping in
     * {@link JsonProperties}
     */
    public Object defaultVal() {
        return JacksonUtils.toObject(defaultValue, schema);
    }

    public Order order() {
        return order;
    }

    public void addAlias(String alias) {
        if (aliases == null) this.aliases = new LinkedHashSet<>();
        aliases.add(alias);
    }

    /**
     * Return the defined aliases as an unmodifiable Set.
     */
    public Set<String> aliases() {
        if (aliases == null) return Collections.emptySet();
        return Collections.unmodifiableSet(aliases);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (!(other instanceof Field)) return false;
        Field that = (Field) other;
        return (name.equals(that.name)) && (schema.equals(that.schema)) && defaultValueEquals(that.defaultValue) && (order == that.order) && propsEqual(that);
    }

    @Override
    public int hashCode() {
        return name.hashCode() + schema.computeHash();
    }

    private boolean defaultValueEquals(JsonNode thatDefaultValue) {
        if (defaultValue == null) return thatDefaultValue == null;
        if (thatDefaultValue == null) return false;
        if (Double.isNaN(defaultValue.doubleValue())) return Double.isNaN(thatDefaultValue.doubleValue());
        return defaultValue.equals(thatDefaultValue);
    }

    @Override
    public String toString() {
        return name + " type:" + schema.getType() + " pos:" + position;
    }
}
