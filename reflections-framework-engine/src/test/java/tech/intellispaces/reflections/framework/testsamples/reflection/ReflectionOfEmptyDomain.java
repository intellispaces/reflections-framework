package tech.intellispaces.reflections.framework.testsamples.reflection;

import tech.intellispaces.reflections.framework.annotation.Reflection;
import tech.intellispaces.reflections.framework.reflection.AbstractReflection;
import tech.intellispaces.reflections.framework.testsamples.domain.EmptyDomain;

@Reflection(EmptyDomain.class)
public interface ReflectionOfEmptyDomain extends AbstractReflection<EmptyDomain> {
}
