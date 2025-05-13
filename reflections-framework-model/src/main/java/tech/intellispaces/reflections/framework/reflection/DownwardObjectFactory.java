package tech.intellispaces.reflections.framework.reflection;

import java.util.function.Function;

@FunctionalInterface
public interface DownwardObjectFactory<O> extends Function<MovableReflection<?>, O> {

  O create(MovableReflection<?> overlyingHandle);

  @Override
  default O apply(MovableReflection<?> overlyingHandle) {
    return create(overlyingHandle);
  }
}
