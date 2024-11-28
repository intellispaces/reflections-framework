package tech.intellispaces.jaquarius.engine;

import com.google.auto.service.AutoService;
import tech.intellispaces.jaquarius.engine.descriptor.ObjectHandleInstance;
import tech.intellispaces.jaquarius.engine.descriptor.ObjectHandleMethod;
import tech.intellispaces.jaquarius.engine.descriptor.ObjectHandleType;
import tech.intellispaces.jaquarius.system.Module;
import tech.intellispaces.jaquarius.system.ObjectHandleWrapper;
import tech.intellispaces.jaquarius.system.kernel.KernelFunctions;
import tech.intellispaces.jaquarius.system.kernel.ModuleLoader;

import java.util.Arrays;
import java.util.List;

@AutoService(JaquariusEngine.class)
public class JaquariusEngineImpl implements JaquariusEngine {

  @Override
  public Module createModule(List<Class<?>> unitClasses, String[] args) {
    return ModuleLoader.loadModule(unitClasses, args);
  }

  @Override
  public ObjectHandleType registerObjectHandleType(
      Class<?> objectHandleClass, ObjectHandleMethod... methods
  ) {
    return new ObjectHandleTypeImpl(objectHandleClass, Arrays.asList(methods));
  }

  @Override
  public <H> ObjectHandleInstance registerObjectHandleInstance(
      Class<H> objectHandleClass, H objectHandle, ObjectHandleType type
  ) {
    KernelFunctions.currentModule().objectRegistry().add((ObjectHandleWrapper) objectHandle);
    return null;
  }
}
