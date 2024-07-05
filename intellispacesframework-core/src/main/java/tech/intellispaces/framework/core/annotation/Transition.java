package tech.intellispaces.framework.core.annotation;

import tech.intellispaces.framework.core.traverse.TraverseTypes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies that the interface or method is a space transition.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Transition {

  /**
   * Transition ID.
   */
  String value();

  /**
   * Allowed traverse type.
   */
  TraverseTypes type() default TraverseTypes.Mapping;

  /**
   * Assigned transition name.
   */
  String name() default "";
}
