package tech.intellispaces.reflections.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import tech.intellispaces.reflections.framework.system.InjectionKind;

/**
 * The injection.<p/>
 *
 * The possible injection kinds are listed in the implementation of the interface {@link InjectionKind}.
 * By default, the injection kind is selected based on the type returned by the method.
 */
@Target({ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Inject {

  /**
   * Injection name.
   */
  String value() default "";

  boolean byName() default true;

  boolean byType() default true;
}
