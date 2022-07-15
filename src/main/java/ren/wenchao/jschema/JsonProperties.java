package ren.wenchao.jschema;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;

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


    public boolean hasProps() {
        return !props.isEmpty();
    }

    void writeProps(JsonGenerator gen) throws IOException {
        for (Map.Entry<String, JsonNode> e : props.entrySet()) {
            gen.writeObjectField(e.getKey(), e.getValue());
        }
    }


}
