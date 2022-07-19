package ren.wenchao.jschema;

class SchemaParseException extends SchemaRuntimeException{
    public SchemaParseException() {
        super();
    }

    public SchemaParseException(String message) {
        super(message);
    }

    public SchemaParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public SchemaParseException(Throwable cause) {
        super(cause);
    }

    protected SchemaParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
