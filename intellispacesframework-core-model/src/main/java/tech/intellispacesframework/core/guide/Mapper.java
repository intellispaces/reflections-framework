package tech.intellispacesframework.core.guide;

/**
 * Mapper guide.
 *
 * <p>Mapper guide maps source object to another related object.
 *
 * <p>Mapper guide does not change the source object.
 *
 * @param <S> source object type.
 * @param <T> target object type.
 */
public interface Mapper<S, T> extends Guide<S, T> {

  @Override
  default boolean isMapper() {
    return true;
  }

  @Override
  default boolean isMover() {
    return false;
  }
}
