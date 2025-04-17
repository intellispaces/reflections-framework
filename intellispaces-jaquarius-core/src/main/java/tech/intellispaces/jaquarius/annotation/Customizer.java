package tech.intellispaces.jaquarius.annotation;

import tech.intellispaces.jaquarius.artifact.ArtifactTypes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
