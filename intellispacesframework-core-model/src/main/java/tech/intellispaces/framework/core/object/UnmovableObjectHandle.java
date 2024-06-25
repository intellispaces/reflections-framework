package tech.intellispaces.framework.core.object;

/**
 * Unmovable object handle.<p/>
 *
 * Unmovable object handle does not allow moving a related object.
 *
 * @param <D> object domain type.
 */
public interface UnmovableObjectHandle<D> extends ObjectHandle<D> {

  @Override
  default boolean isMovable() {
    return false;
  }
}
