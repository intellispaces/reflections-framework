package tech.intellispacesframework.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface ProjectionDefinition {

  /**
   * Class of getter action to get projection target.
   */
  Class<?> provider() default Void.class;
}
