package tech.intellispaces.jaquarius.engine;

import tech.intellispaces.jaquarius.engine.descriptor.ObjectHandleInstance;
import tech.intellispaces.jaquarius.engine.descriptor.ObjectHandleMethod;
import tech.intellispaces.jaquarius.engine.descriptor.ObjectHandleType;
import tech.intellispaces.jaquarius.system.Module;

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
   * @param objectHandleWrapperClass the object handle wrapper class.
   * @param objectHandleClass the object handle class.
   * @param methods object handle methods.
   * @return the registered object handle type.
   * @param <H> the object handle type.
   * @param <W> the object handle wrapper type.
   */
  <H, W extends H> ObjectHandleType registerObjectHandleType(
      Class<W> objectHandleWrapperClass, Class<H> objectHandleClass, ObjectHandleMethod... methods
  );

  /**
   * Registers object handle.
   *
   * @param objectHandleWrapper the object handle wrapper.
   * @param type the object handle type.
   * @return the registered object handle instance.
   * @param <W> the object handle wrapper type.
   */
  <W> ObjectHandleInstance registerObjectHandleInstance(W objectHandleWrapper, ObjectHandleType type);
}
