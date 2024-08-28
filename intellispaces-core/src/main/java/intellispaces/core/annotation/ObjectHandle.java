package intellispaces.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ObjectHandle {

  /**
   * Object handle class simple name.
   */
  String value() default "";

  /**
   * Object handle domain class.
   */
  Class<?> domain() default Void.class;
}
