package ren.wenchao.jschema;

class NameUtils {

    private static ThreadLocal<Boolean> validateNames = ThreadLocal.withInitial(() -> true);

    static String validateName(String name) {
        if (!validateNames.get())
            return name; // not validating names
        int length = name.length();
        if (length == 0)
            throw new SchemaParseException("Empty name");
        char first = name.charAt(0);
        if (!(Character.isLetter(first) || first == '_'))
            throw new SchemaParseException("Illegal initial character: " + name);
        for (int i = 1; i < length; i++) {
            char c = name.charAt(i);
            if (!(Character.isLetterOrDigit(c) || c == '_'))
                throw new SchemaParseException("Illegal character in: " + name);
        }
        return name;
    }

}
