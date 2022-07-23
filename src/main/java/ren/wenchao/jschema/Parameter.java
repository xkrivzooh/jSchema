package ren.wenchao.jschema;

import com.google.common.collect.Maps;

import java.util.Map;

public class Parameter {
    private String name;
    private String doc;
    private Map<String, String> props = Maps.newHashMap();
    private TypeSchema schema;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public TypeSchema getSchema() {
        return schema;
    }

    public void setSchema(TypeSchema schema) {
        this.schema = schema;
    }

    public void addProp(String key, String value) {
        props.put(key, value);
    }

    public String getProp(String key) {
        return props.get(key);
    }

    public Map<String, String> getProps() {
        return props;
    }
}