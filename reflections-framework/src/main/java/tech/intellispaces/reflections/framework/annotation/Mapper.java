package tech.intellispaces.reflections.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies that the method is a mapper guide.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Mapper {

  /**
   * ReflectionChannel class.
   */
  Class<?> value() default Void.class;

  String cid() default "";
}
