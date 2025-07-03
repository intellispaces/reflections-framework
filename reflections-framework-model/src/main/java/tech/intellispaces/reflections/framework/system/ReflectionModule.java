package tech.intellispaces.reflections.framework.system;

import java.util.List;

import tech.intellispaces.core.Guide;

/**
 * The module of the reflection system.
 */
public interface ReflectionModule {

  ReflectionModule start();

  ReflectionModule start(String[] args);

  ReflectionModule stop();

  ReflectionModule upload();

  ReflectionSystem system();

  List<ReflectionUnit> units();

  ReflectionUnit mainUnit();

  /**
   * Returns list of guides that are currently registered in the module.
   */
  List<Guide> guides();

  /**
   * Returns the list of guides of the required type that are currently registered in the module.
   *
   * @param guideClass the requested guide class.
   * @return the list of found guides.
   * @param <G> the requested guide type.
   */
  <G> List<G> guides(Class<G> guideClass);
}
