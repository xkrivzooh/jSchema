package ren.wenchao.jschema;

import java.util.List;

public class FunctionSchemaBuilder {
    private final FunctionSchema functionSchema;

    private FunctionSchemaBuilder() {
        functionSchema = new FunctionSchema();
    }

    public static FunctionSchemaBuilder builder() {
        return new FunctionSchemaBuilder();
    }

    public FunctionSchemaBuilder namespace(String namespace) {
        functionSchema.setNamespace(namespace);
        return this;
    }

    public FunctionSchemaBuilder name(String name) {
        functionSchema.setName(name);
        return this;
    }


    public FunctionSchemaBuilder functionName(String functionName) {
        functionSchema.setFunctionName(functionName);
        return this;
    }

    public FunctionSchemaBuilder doc(String doc) {
        functionSchema.setDoc(doc);
        return this;
    }

    public FunctionSchemaBuilder requestParameter(Parameter parameter) {
        functionSchema.addParameter(parameter);
        return this;
    }

    public FunctionSchemaBuilder requestParameter(String parameterName, String doc, TypeSchema typeSchema) {
        Parameter parameter = new Parameter();
        parameter.setName(parameterName);
        parameter.setDoc(doc);
        parameter.setSchema(typeSchema);
        functionSchema.addParameter(parameter);
        return this;
    }

    public FunctionSchemaBuilder request(List<Parameter> request) {
        functionSchema.setRequest(request);
        return this;
    }

    public FunctionSchemaBuilder response(TypeSchema typeSchema) {
        functionSchema.setResponse(typeSchema);
        return this;
    }

    public FunctionSchema build() {
        return functionSchema;
    }
}
