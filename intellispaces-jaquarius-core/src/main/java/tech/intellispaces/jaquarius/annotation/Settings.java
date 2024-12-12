package tech.intellispaces.jaquarius.annotation;

import tech.intellispaces.jaquarius.properties.ModuleSettingsSupplier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ProjectionSupplier(supplier = ModuleSettingsSupplier.class)
public @interface Settings {

  /**
   * The path to settings file.
   */
  String value() default "";
}
