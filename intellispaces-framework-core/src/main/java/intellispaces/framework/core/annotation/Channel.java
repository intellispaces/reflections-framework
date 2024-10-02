package intellispaces.framework.core.annotation;

import intellispaces.framework.core.traverse.TraverseTypes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies that the interface or method is a semantic space chanel.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Channel {

  /**
   * Channel identifier.
   */
  String value();

  /**
   * Channel name.
   */
  String name() default "";

  /**
   * Allowed traverse types.
   */
  TraverseTypes[] allowedTraverse() default { TraverseTypes.Mapping };

  /**
   * Default traverse type.<p/>
   *
   * This parameter is used only if parameter allowedTraverse has multiple values.
   */
  TraverseTypes defaultTraverse() default TraverseTypes.Mapping;
}
