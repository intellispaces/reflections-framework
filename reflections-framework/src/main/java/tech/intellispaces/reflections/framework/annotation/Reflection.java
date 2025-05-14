package tech.intellispaces.reflections.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The object reflection.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Reflection {

  /**
   * The reflection domain.
   */
  Class<?> value();

  /**
   * The reflection class simple name.
   */
  String name() default "";
}
