package intellispaces.framework.core.annotation;

import intellispaces.framework.core.system.injection.InjectionKinds;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The injection.<p/>
 *
 * The possible injection kinds are listed in the class {@link  InjectionKinds}.
 * By default, the injection kind is selected based on the type returned by the method.
 */
@Target({ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Inject {

  /**
   * Injection name.
   */
  String value() default "";
}
