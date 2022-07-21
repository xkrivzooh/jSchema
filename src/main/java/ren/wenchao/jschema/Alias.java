package ren.wenchao.jschema;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Adds the given name and space as an alias to the schema.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD })
@Repeatable(Aliases.class)
public @interface Alias {
  String NULL = "NOT A VALID NAMESPACE";

  String alias();

  String space() default NULL;
}
