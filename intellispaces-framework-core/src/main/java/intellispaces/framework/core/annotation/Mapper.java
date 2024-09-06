package intellispaces.framework.core.annotation;

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
   * Class of the transition implemented by this guide.
   */
  Class<?> value() default Void.class;
}
