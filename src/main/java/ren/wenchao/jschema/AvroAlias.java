package ren.wenchao.jschema;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Adds the given name and space as an alias to the schema. Avro files of this
 * schema can be read into classes named by the alias.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD })
@Repeatable(AvroAliases.class)
public @interface AvroAlias {
  String NULL = "NOT A VALID NAMESPACE";

  String alias();

  String space() default NULL;
}
