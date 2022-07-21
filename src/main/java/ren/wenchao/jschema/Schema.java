package ren.wenchao.jschema;

import java.lang.annotation.*;

/**
 * Declares that a Java type should have a specified  schema, overriding the
 * normally inferred schema. May be used for classes, parameters, fields and
 * method return types.
 * <p>
 * This is useful for slight alterations to the schema that would be
 * automatically inferred. For example, a <code>List&lt;Integer&gt;</code>whose
 * elements may be null might use the annotation
 * 
 * <pre>
 * &#64;Schema("{\"type\":\"array\",\"items\":[\"null\",\"int\"]}")
 * </pre>
 * 
 * since the {@link Nullable} annotation could not be used here.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD })
@Documented
public @interface Schema {
  /** The schema to use for this value. */
  String value();
}
