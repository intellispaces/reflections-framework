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

  TraverseTypes[] allowedTraverseTypes() default { TraverseTypes.Mapping };

  /**
   * Default traverse type.<p/>
   *
   * This parameter is used only if parameter allowedTraverseTypes has multiple values.
   */
  TraverseTypes defaultTraverseType() default TraverseTypes.Mapping;

  /**
   * Assigned transition name.
   */
  String name() default "";

  /**
   * Indicates that this transition corresponds to factory.<p/>
   *
   * Relevant only for moving transitions.
   */
  boolean isFactory() default false;
}
