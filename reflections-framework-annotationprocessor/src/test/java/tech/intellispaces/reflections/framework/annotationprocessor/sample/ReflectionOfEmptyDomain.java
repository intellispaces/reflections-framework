package tech.intellispaces.reflections.framework.annotationprocessor.sample;

import tech.intellispaces.reflections.framework.annotation.Reflection;
import tech.intellispaces.reflections.framework.reflection.SystemReflection;

@Reflection(domainClass = EmptyDomain.class)
public interface ReflectionOfEmptyDomain extends SystemReflection {
}
