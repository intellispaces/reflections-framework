package tech.intellispaces.reflections.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import tech.intellispaces.reflections.framework.traverse.TraverseTypes;

/**
 * Specifies that the interface or method is a semantic space chanel.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Channel {

  /**
   * The channel identifier (CID).
   */
  String value();

  /**
   * The channel name.
   */
  String name() default "";

  /**
   * The flag of the default channel between two domains.
   */
  boolean isDefault() default false;

  /**
   * Allowed traverse types.<p/>
   *
   * By default, mapping traverse type is used.
   */
  TraverseTypes[] allowedTraverse() default { TraverseTypes.Mapping };

  /**
   * The default traverse type.<p/>
   *
   * This parameter is used only if parameter allowedTraverse has multiple values.
   */
  TraverseTypes defaultTraverse() default TraverseTypes.Mapping;
}
