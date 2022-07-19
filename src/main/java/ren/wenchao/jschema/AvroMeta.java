package ren.wenchao.jschema;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Adds the given key:Value pair as metadata into the schema, at the
 * corresponding node.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD })
@Repeatable(AvroMeta.AvroMetas.class)
public @interface AvroMeta {
  String key();

  String value();

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ ElementType.TYPE, ElementType.FIELD })
  @interface AvroMetas {
    AvroMeta[] value();
  }
}
