package tech.intellispaces.reflections.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import tech.intellispaces.reflections.framework.artifact.ArtifactTypes;

/**
 * The generated artifact customizer.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Customizer {

  /**
   * The origin artifact.
   */
  Class<?> origin() default Void.class;

  /**
   * The target artifact type.
   * <p>
   * See class {@link ArtifactTypes}.
   */
  ArtifactTypes target();
}
