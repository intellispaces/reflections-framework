package tech.intellispaces.reflections.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies that the method is a mapper related to specific moving.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MapperOfMoving {

  /**
   * ReflectionChannel class.
   */
  Class<?> value() default Void.class;
}
