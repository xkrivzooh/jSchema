package ren.wenchao.jschema;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares that a Java type should be represented by an Avro union schema. May
 * be used for base classes or interfaces whose instantiable subclasses can be
 * listed in the parameters to the @Union annotation. If applied to method
 * parameters this determines the reflected message parameter type. If applied
 * to a method, this determines its return type. A null schema may be specified
 * with {@link java.lang.Void}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD })
@Documented
public @interface Union {
  /** The instantiable classes that compose this union. */
  Class[] value();
}