package tech.intellispaces.jaquarius.annotation;

import tech.intellispaces.jaquarius.annotation.processor.data.DataAnnotationProcessor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation describes a domain of data objects completely defined by a data set and located in memory.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@AnnotationProcessor(DataAnnotationProcessor.class)
public @interface Data {
}
