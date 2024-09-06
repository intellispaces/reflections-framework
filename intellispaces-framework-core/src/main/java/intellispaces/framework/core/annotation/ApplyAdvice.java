package intellispaces.framework.core.annotation;

import intellispaces.framework.core.aop.Advice;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApplyAdvice {

  Class<? extends Advice> adviceClass();
}
