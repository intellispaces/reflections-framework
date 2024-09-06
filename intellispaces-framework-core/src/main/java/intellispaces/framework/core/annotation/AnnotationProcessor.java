package intellispaces.framework.core.annotation;

import intellispaces.common.annotationprocessor.AnnotatedTypeProcessor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AnnotationProcessor {

  Class<? extends AnnotatedTypeProcessor> value();
}
