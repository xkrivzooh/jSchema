package ren.wenchao.jschema;

public class Parameter {
    private String name;
    private String doc;
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
}