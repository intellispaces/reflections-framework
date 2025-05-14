package tech.intellispaces.reflections.framework.reflection;

import java.util.List;

/**
 * The unmovable object reflection.<p/>
 *
 * The unmovable object reflection being moved cannot move in space.
 *
 * @param <D> the domain type.
 */
public interface UnmovableReflection<D> extends Reflection<D> {

  @Override
  default boolean isMovable() {
    return false;
  }

  @Override
  List<UnmovableReflection<?>> underlyingReflections();

  @Override
  UnmovableReflection<?> overlyingReflection();
}
