package tech.intellispaces.jaquarius.annotation;

import tech.intellispaces.jaquarius.properties.ModulePropertiesTargetSupplier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ProjectionDefinition(provider = ModulePropertiesTargetSupplier.class)
public @interface Properties {

  /**
   * The property path.
   */
  String value() default "";
}
