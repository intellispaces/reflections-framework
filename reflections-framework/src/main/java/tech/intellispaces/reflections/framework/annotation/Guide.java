package tech.intellispaces.reflections.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies that this unit is a guide.<p/>
 *
 * Interface marked annotation @Guide is guide type.
 * Class marked annotation @Guide is guide implementation.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Guide {

  String name() default "";
}
