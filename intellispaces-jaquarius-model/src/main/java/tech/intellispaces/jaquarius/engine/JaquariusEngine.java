package tech.intellispaces.jaquarius.engine;

import tech.intellispaces.jaquarius.engine.description.ObjectHandleMethodDescription;
import tech.intellispaces.jaquarius.engine.description.ObjectHandleTypeDescription;
import tech.intellispaces.jaquarius.engine.description.UnitMethodDescription;
import tech.intellispaces.jaquarius.system.Module;
import tech.intellispaces.jaquarius.system.UnitWrapper;

import java.util.List;

/**
 * Jaquarius engine API.
 */
public interface JaquariusEngine {

  /**
   * Creates system module and load into current application.<p/>
   *
   * Only one module can be loaded in application.
   * If the module is already loaded in application, it will be unloaded.
   *
   * @param unitClasses module unit classes.
   * @param args command line arguments.
   */
  Module createModule(List<Class<?>> unitClasses, String[] args);

  /**
   * Registers object handle type.
   *
   * @param wrapperClass the object handle wrapper class.
   * @param objectHandleClass the object handle class.
   * @param methods object handle wrapper method descriptions.
   * @return the registered object handle type.
   *
   * @param <H> the object handle type.
   * @param <W> the object handle wrapper type.
   */
  <H, W extends H> ObjectHandleTypeDescription registerObjectHandleType(
      Class<W> wrapperClass, Class<H> objectHandleClass, ObjectHandleMethodDescription... methods
  );

  /**
   * Registers object handle.
   *
   * @param objectHandleWrapper the object handle wrapper.
   * @param type the object handle type.
   * @return the object handle broker.
   *
   * @param <W> the object handle wrapper type.
   */
  <W> ObjectHandleBroker registerObjectHandle(W objectHandleWrapper, ObjectHandleTypeDescription type);

  /**
   * Registers module unit.
   *
   * @param unitWrapper the unit wrapper instance.
   * @param unitClass the unit class.
   * @param methods unit wrapper method descriptions.
   * @return the unit broker.
   *
   * @param <U> the unit type.
   * @param <W> the unit wrapper type.
   */
  <U, W extends UnitWrapper> UnitBroker registerUnit(W unitWrapper, Class<U> unitClass, UnitMethodDescription... methods);
}
