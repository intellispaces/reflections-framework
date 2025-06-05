package tech.intellispaces.reflections.framework.annotationprocessor.sample;

import tech.intellispaces.reflections.framework.annotation.Reflection;
import tech.intellispaces.reflections.framework.reflection.TypedReflection;

@Reflection(EmptyDomain.class)
public interface ReflectionOfEmptyDomain extends TypedReflection<EmptyDomain> {
}
