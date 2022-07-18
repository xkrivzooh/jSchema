package ren.wenchao.jschema;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

public abstract class JsonProperties {
    private Set<String> reserved;

    JsonProperties(Set<String> reserved) {
        this.reserved = reserved;
    }


    // use a ConcurrentHashMap for speed and thread safety, but keep a Queue of the
    // entries to maintain order
    // the queue is always updated after the main map and is thus is potentially a
    // subset of the map.
    // By making props private, we can control access and only implement/override
    // the methods
    // we need. We don't ever remove anything so we don't need to implement the
    // clear/remove functionality.
    // Also, we only ever ADD to the collection, never changing a value, so
    // putWithAbsent is the
    // only modifier
    private ConcurrentMap<String, JsonNode> props = new ConcurrentHashMap<String, JsonNode>() {
        private static final long serialVersionUID = 1L;
        private Queue<MapEntry<String, JsonNode>> propOrder = new ConcurrentLinkedQueue<>();

        @Override
        public JsonNode putIfAbsent(String key, JsonNode value) {
            JsonNode r = super.putIfAbsent(key, value);
            if (r == null) {
                propOrder.add(new MapEntry<>(key, value));
            }
            return r;
        }

        @Override
        public JsonNode put(String key, JsonNode value) {
            return putIfAbsent(key, value);
        }

        @Override
        public Set<Entry<String, JsonNode>> entrySet() {
            return new AbstractSet<Entry<String, JsonNode>>() {
                @Override
                public Iterator<Entry<String, JsonNode>> iterator() {
                    return new Iterator<Entry<String, JsonNode>>() {
                        Iterator<MapEntry<String, JsonNode>> it = propOrder.iterator();

                        @Override
                        public boolean hasNext() {
                            return it.hasNext();
                        }

                        @Override
                        public Entry<String, JsonNode> next() {
                            return it.next();
                        }
                    };
                }

                @Override
                public int size() {
                    return propOrder.size();
                }
            };
        }
    };

    /**
     * Adds a property with the given name <tt>name</tt> and value <tt>value</tt>.
     * Neither <tt>name</tt> nor <tt>value</tt> can be <tt>null</tt>. It is illegal
     * to add a property if another with the same name but different value already
     * exists in this schema.
     *
     * @param name  The name of the property to add
     * @param value The value for the property to add
     */
    private void addProp(String name, JsonNode value) {
        if (reserved.contains(name))
            throw new SchemaRuntimeException("Can't set reserved property: " + name);

        if (value == null)
            throw new SchemaRuntimeException("Can't set a property to null: " + name);

        JsonNode old = props.putIfAbsent(name, value);
        if (old != null && !old.equals(value)) {
            throw new SchemaRuntimeException("Can't overwrite property: " + name);
        }
    }


    /**
     * Adds a property with the given name <tt>name</tt> and value <tt>value</tt>.
     * Neither <tt>name</tt> nor <tt>value</tt> can be <tt>null</tt>. It is illegal
     * to add a property if another with the same name but different value already
     * exists in this schema.
     *
     * @param name  The name of the property to add
     * @param value The value for the property to add
     */
    public void addProp(String name, String value) {
        addProp(name, TextNode.valueOf(value));
    }

    public boolean hasProps() {
        return !props.isEmpty();
    }

    void writeProps(JsonGenerator gen) throws IOException {
        for (Map.Entry<String, JsonNode> e : props.entrySet()) {
            gen.writeObjectField(e.getKey(), e.getValue());
        }
    }

    boolean propsEqual(JsonProperties np) {
        return props.equals(np.props);
    }

    int propsHashCode() {
        return props.hashCode();
    }

}
