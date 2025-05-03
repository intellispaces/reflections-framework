package tech.intellispaces.reflectionsj.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import tech.intellispaces.reflectionsj.artifact.ArtifactTypes;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ArtifactGeneration {

  /**
   * The origin artifact.
   */
  Class<?> origin() default Void.class;

  /**
   * The target artifact type.
   * <p>
   * See class {@link ArtifactTypes}.
   */
  ArtifactTypes target() default ArtifactTypes.NotSpecified;

  /**
   * The flag that enables or disables target artifact generation.
   */
  boolean enable() default true;
}
