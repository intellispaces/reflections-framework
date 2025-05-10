package tech.intellispaces.reflectionsframework.object.reference;

import java.util.function.Function;

@FunctionalInterface
public interface DownwardObjectFactory<O> extends Function<MovableObjectHandle<?>, O> {

  O create(MovableObjectHandle<?> overlyingHandle);

  @Override
  default O apply(MovableObjectHandle<?> overlyingHandle) {
    return create(overlyingHandle);
  }
}
