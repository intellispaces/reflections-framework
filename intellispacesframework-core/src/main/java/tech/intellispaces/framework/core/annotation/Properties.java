package tech.intellispaces.framework.core.annotation;

import tech.intellispaces.framework.core.system.projection.ModulePropertiesProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ProjectionDefinition(provider = ModulePropertiesProvider.class)
public @interface Properties {

  /**
   * Property path.
   */
  String value() default "";
}
