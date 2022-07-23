package ren.wenchao.jschema;

import com.fasterxml.jackson.core.JsonGenerator;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import ren.wenchao.jschema.constraints.Constraint;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class FunctionSchema {

    private String namespace;
    private String name;
    private String functionName;
    private String doc;
    private List<Parameter> request = Lists.newArrayList();
    private TypeSchema response;


    public static FunctionSchema getSchema(Method method) {
        Preconditions.checkNotNull(method);

        java.lang.reflect.Parameter[] parameters = method.getParameters();
        List<Parameter> request = Lists.newArrayListWithExpectedSize(parameters.length);
        for (java.lang.reflect.Parameter parameter : parameters) {
            Parameter item = new Parameter();
            item.setName(parameter.getName());
            item.setDoc("");
            item.setSchema(TypeSchema.getSchema(parameter.getType()));
            request.add(item);
        }

        Class<?> returnType = method.getReturnType();
        TypeSchema returnTypeSchema = TypeSchema.getSchema(returnType);

        Class<?> declaringClass = method.getDeclaringClass();

        String name = declaringClass.getSimpleName();
        String space = declaringClass.getPackage() == null ? "" : declaringClass.getPackage().getName();
        if (declaringClass.getEnclosingClass() != null) { // nested class
            space = declaringClass.getEnclosingClass().getName();
        }

        FunctionSchema functionSchema = new FunctionSchema();
        functionSchema.setNamespace(space);
        functionSchema.setName(name);
        functionSchema.setFunctionName(method.getName());
        functionSchema.setRequest(request);
        functionSchema.setResponse(returnTypeSchema);
        return functionSchema;
    }

    public List<String> validate(List<Object> parameterValues, boolean failFast) {
        if (parameterValues == null) {
            if (request.size() == 0) {
                return Lists.newArrayList();
            } else {
                throw new SchemaRuntimeException("function need at last 1 parameter values");
            }
        }

        if (request.size() != parameterValues.size()) {
            throw new SchemaRuntimeException("function parameter size not match given parameter values");
        }

        List<String> errors = Lists.newArrayList();
        for (int i = 0; i < request.size(); i++) {
            Parameter parameter = request.get(i);
            List<Constraint> constraints = parameter.getConstraints();
            if ((constraints != null) && (constraints.size() > 0)) {
                Object value = parameterValues.get(i);
                for (Constraint constraint : constraints) {
                    boolean validate = constraint.validate(value);
                    if (!validate) {
                        errors.add(constraint.validateFieldMessage());
                        if (failFast) {
                            return errors;
                        }
                    }
                }
            }
        }
        return errors;
    }

    @Override
    public String toString() {
        return toString(true);
    }

    public String toString(boolean pretty) {
        try {
            StringWriter writer = new StringWriter();
            JsonGenerator gen = JacksonUtils.getFactory().createGenerator(writer);
            if (pretty) {
                gen.useDefaultPrettyPrinter();
            }
            toJson(gen);
            gen.flush();
            return writer.toString();
        } catch (IOException e) {
            throw new SchemaRuntimeException(e);
        }
    }

    private void toJson(JsonGenerator gen) throws IOException {
        gen.writeStartObject();

        gen.writeStringField("type", "function");
        gen.writeStringField("namespace", this.namespace);
        gen.writeStringField("name", this.name);
        gen.writeStringField("functionName", this.functionName);
        gen.writeStringField("doc", this.doc);

        gen.writeFieldName("request");
        gen.writeStartObject();
        for (Parameter parameter : request) {
            gen.writeFieldName(parameter.getName());
            gen.writeStartObject();

            gen.writeStringField("doc", parameter.getDoc());

            //props
            gen.writeFieldName("props");
            gen.writeStartObject();
            Map<String, String> props = parameter.getProps();
            if (props != null) {
                for (Map.Entry<String, String> entry : props.entrySet()) {
                    gen.writeStringField(entry.getKey(), entry.getValue());
                }
            }
            gen.writeEndObject();

            //Constraint
            gen.writeFieldName("constraints");
            gen.writeStartObject();
            List<Constraint> constraints = parameter.getConstraints();
            for (Constraint constraint : constraints) {
                constraint.toJson(gen);
            }
            gen.writeEndObject();

            //type
            gen.writeFieldName("type");
            TypeSchema schema = parameter.getSchema();
            schema.toJson(new Names(), gen);

            gen.writeEndObject();
        }
        gen.writeEndObject();

        gen.writeFieldName("response");
        response.toJson(new Names(), gen);

        gen.writeEndObject();
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public List<Parameter> getRequest() {
        return request;
    }

    public void setRequest(List<Parameter> request) {
        this.request = request;
    }

    public void addParameter(Parameter parameter) {
        this.request.add(parameter);
    }

    public TypeSchema getResponse() {
        return response;
    }

    public void setResponse(TypeSchema response) {
        this.response = response;
    }
}

