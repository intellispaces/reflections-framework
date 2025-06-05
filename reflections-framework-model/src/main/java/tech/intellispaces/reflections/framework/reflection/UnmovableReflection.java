package tech.intellispaces.reflections.framework.reflection;

import java.util.List;

/**
 * The unmovable reflection.<p/>
 *
 * The unmovable reflection being moved cannot move in the notion space.
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
