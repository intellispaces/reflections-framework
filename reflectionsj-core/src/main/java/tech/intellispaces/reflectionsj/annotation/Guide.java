package tech.intellispaces.reflectionsj.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies that this class is a unit that represents a guide.<p/>
 *
 * Interface marked annotation @Guide is guide interface. Class marked annotation @Guide is guide implementation.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Guide {
}
