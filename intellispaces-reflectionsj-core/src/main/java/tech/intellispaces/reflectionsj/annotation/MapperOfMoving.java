package tech.intellispaces.reflectionsj.annotation;

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
   * Channel class.
   */
  Class<?> value() default Void.class;
}
