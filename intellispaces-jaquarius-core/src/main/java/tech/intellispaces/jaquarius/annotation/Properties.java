package tech.intellispaces.jaquarius.annotation;

import tech.intellispaces.jaquarius.system.settings.ModulePropertiesProjectionSupplier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The module properties.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ProjectionSupplier(supplier = ModulePropertiesProjectionSupplier.class)
public @interface Properties {

  /**
   * The filename.
   */
  String filename() default "/module.yaml";

  /**
   * The property path.
   */
  String value() default "";
}
