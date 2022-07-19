package ren.wenchao.jschema;

class SchemaRuntimeException extends RuntimeException{
    public SchemaRuntimeException() {
        super();
    }

    public SchemaRuntimeException(String message) {
        super(message);
    }

    public SchemaRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public SchemaRuntimeException(Throwable cause) {
        super(cause);
    }

    protected SchemaRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
