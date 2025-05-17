package tech.intellispaces.reflections.framework.samples.object;

import tech.intellispaces.reflections.framework.annotation.Reflection;
import tech.intellispaces.reflections.framework.reflection.AbstractReflection;
import tech.intellispaces.reflections.framework.samples.domain.EmptyDomain;

@Reflection(EmptyDomain.class)
public interface ReflectionOfEmptyDomain extends AbstractReflection<EmptyDomain> {
}
