
package ren.wenchao.jschema;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares that null is a valid value for a Java type. Causes an Avro union
 * with null to be used. May be applied to parameters, fields and methods (to
 * declare the return type).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD })
@Documented
public @interface Nullable {
}
