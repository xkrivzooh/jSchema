
package ren.wenchao.jschema;

import java.lang.annotation.*;

/**
 * Declares that null is a valid value for a Java type.
 * May be applied to parameters, fields and methods (to declare the return type).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD })
@Documented
public @interface Nullable {
}
