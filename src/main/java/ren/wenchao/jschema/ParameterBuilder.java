package ren.wenchao.jschema;

import ren.wenchao.jschema.constraints.Constraint;

import java.util.List;
import java.util.Map;

public class ParameterBuilder {

    private final Parameter parameter;

    private ParameterBuilder() {
        parameter = new Parameter();
    }

    public static ParameterBuilder builder() {
        return new ParameterBuilder();
    }

    public ParameterBuilder name(String name) {
        parameter.setName(name);
        return this;
    }

    public ParameterBuilder doc(String doc) {
        parameter.setDoc(doc);
        return this;
    }

    public ParameterBuilder props(Map<String, String> props) {
        parameter.getProps().putAll(props);
        return this;
    }

    public ParameterBuilder prop(String key, String value) {
        parameter.addProp(key, value);
        return this;
    }

    public ParameterBuilder addConstraint(Constraint constraint) {
        parameter.addConstraint(constraint);
        return this;
    }

    public ParameterBuilder addConstraints(List<Constraint> constraints) {
        parameter.getConstraints().addAll(constraints);
        return this;
    }

    public ParameterBuilder schema(TypeSchema typeSchema) {
        parameter.setSchema(typeSchema);
        return this;
    }

    public Parameter build() {
        return parameter;
    }
}
