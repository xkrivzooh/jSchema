package ren.wenchao.jschema;

import java.lang.annotation.*;

/**
 * Declares that a class or field should be represented by an schema string. It's
 * {@link Object#toString()} method will be used to convert it to a string, and
 * its single String parameter constructor will be used to create instances.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD })
@Documented
public @interface Stringable {
}