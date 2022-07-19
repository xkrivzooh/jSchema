package ren.wenchao.jschema;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Sets the avroname for this java field. When reading into this class, a
 * reflectdatumreader looks for a schema field with the avroname.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AvroName {
  String value();
}
