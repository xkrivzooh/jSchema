/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ren.wenchao.jschema;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

abstract class JsonProperties {

  static {
    Accessor.setAccessor(new Accessor.JsonPropertiesAccessor() {
      @Override
      protected void addProp(JsonProperties props, String name, JsonNode value) {
        props.addProp(name, value);
      }
    });
  }

  public static class Null {
    private Null() {
    }
  }

  /** A value representing a JSON <code>null</code>. */
  public static final Null NULL_VALUE = new Null();

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
    public Set<Map.Entry<String, JsonNode>> entrySet() {
      return new AbstractSet<Map.Entry<String, JsonNode>>() {
        @Override
        public Iterator<Map.Entry<String, JsonNode>> iterator() {
          return new Iterator<Map.Entry<String, JsonNode>>() {
            Iterator<MapEntry<String, JsonNode>> it = propOrder.iterator();

            @Override
            public boolean hasNext() {
              return it.hasNext();
            }

            @Override
            public java.util.Map.Entry<String, JsonNode> next() {
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

  private Set<String> reserved;

  JsonProperties(Set<String> reserved) {
    this.reserved = reserved;
  }

  JsonProperties(Set<String> reserved, Map<String, ?> propMap) {
    this.reserved = reserved;
    for (Entry<String, ?> a : propMap.entrySet()) {
      Object v = a.getValue();
      JsonNode json = null;
      if (v instanceof String) {
        json = TextNode.valueOf((String) v);
      } else if (v instanceof JsonNode) {
        json = (JsonNode) v;
      } else {
        json = JacksonUtils.toJsonNode(v);
      }
      props.put(a.getKey(), json);
    }
  }

  /**
   * Returns the value of the named, string-valued property in this schema.
   * Returns <tt>null</tt> if there is no string-valued property with that name.
   */
  public String getProp(String name) {
    JsonNode value = getJsonProp(name);
    return value != null && value.isTextual() ? value.textValue() : null;
  }

  /**
   * Returns the value of the named property in this schema. Returns <tt>null</tt>
   * if there is no property with that name.
   */
  private JsonNode getJsonProp(String name) {
    return props.get(name);
  }

  /**
   * Returns the value of the named property in this schema. Returns <tt>null</tt>
   * if there is no property with that name.
   */
  public Object getObjectProp(String name) {
    return JacksonUtils.toObject(props.get(name));
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

  public void addProp(String name, Object value) {
    if (value instanceof JsonNode) {
      addProp(name, (JsonNode) value);
    } else {
      addProp(name, JacksonUtils.toJsonNode(value));
    }
  }

  public void putAll(JsonProperties np) {
    for (Map.Entry<? extends String, ? extends JsonNode> e : np.props.entrySet())
      addProp(e.getKey(), e.getValue());
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
   * Adds all the props from the specified json properties.
   *
   * @see #getObjectProps()
   */
  public void addAllProps(JsonProperties properties) {
    for (Entry<String, JsonNode> entry : properties.props.entrySet())
      addProp(entry.getKey(), entry.getValue());
  }

  /** Return the defined properties as an unmodifiable Map. */
  public Map<String, Object> getObjectProps() {
    Map<String, Object> result = new LinkedHashMap<>();
    for (Map.Entry<String, JsonNode> e : props.entrySet())
      result.put(e.getKey(), JacksonUtils.toObject(e.getValue()));
    return Collections.unmodifiableMap(result);
  }

  void writeProps(JsonGenerator gen) throws IOException {
    for (Map.Entry<String, JsonNode> e : props.entrySet())
      gen.writeObjectField(e.getKey(), e.getValue());
  }

  int propsHashCode() {
    return props.hashCode();
  }

  boolean propsEqual(JsonProperties np) {
    return props.equals(np.props);
  }

  public boolean hasProps() {
    return !props.isEmpty();
  }

}
