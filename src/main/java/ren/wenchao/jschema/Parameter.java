package ren.wenchao.jschema;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import ren.wenchao.jschema.constraints.Constraint;

import java.util.List;
import java.util.Map;

public class Parameter {
    private String name;
    private String doc;
    private final Map<String, String> props = Maps.newHashMap();
    private final List<Constraint> constraints = Lists.newArrayList();
    private TypeSchema schema;
    private String defaultValue;

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

    public List<Constraint> getConstraints() {
        return constraints;
    }

    public void addConstraint(Constraint constraint) {
        constraints.add(constraint);
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

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}