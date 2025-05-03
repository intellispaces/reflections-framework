package tech.intellispaces.reflectionsj.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import tech.intellispaces.reflectionsj.system.settings.ModulePropertiesProjectionSupplier;

/**
 * The module properties.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ProjectionSupplier(supplier = ModulePropertiesProjectionSupplier.class)
public @interface Properties {

  /**
   * The property name.
   */
  String value() default "";

  /**
   * The properties file path.
   */
  String path() default "/module.yaml";
}
