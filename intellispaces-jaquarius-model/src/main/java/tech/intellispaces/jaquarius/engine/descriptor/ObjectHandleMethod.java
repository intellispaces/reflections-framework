package tech.intellispaces.jaquarius.engine.descriptor;

import tech.intellispaces.action.Action;

import java.util.List;

/**
 * Object handle method descriptor.
 */
public interface ObjectHandleMethod {

  /**
   * The method name.
   */
  String name();

  /**
   * Method parameter classes.
   */
  List<Class<?>> paramClasses();

  /**
   * Related guide action or <code>null</code>.
   */
  Action guideAction();

  /**
   * Related guide method parameter classes.
   */
  List<Class<?>> guideParamClasses();
}
