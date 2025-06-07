package tech.intellispaces.reflections.framework.testsamples.reflection;

import tech.intellispaces.reflections.framework.annotation.Reflection;
import tech.intellispaces.reflections.framework.reflection.SystemReflection;
import tech.intellispaces.reflections.framework.testsamples.domain.EmptyDomain;

@Reflection(domainClass = EmptyDomain.class)
public interface ReflectionOfEmptyDomain extends SystemReflection {
}
