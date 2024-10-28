package intellispaces.jaquarius.engine;

import com.google.auto.service.AutoService;
import intellispaces.jaquarius.system.Module;
import intellispaces.jaquarius.system.kernel.KernelFunctions;
import intellispaces.jaquarius.system.kernel.KernelModule;
import intellispaces.jaquarius.system.kernel.ModuleLoader;

import java.util.List;

@AutoService(JaquariusEngine.class)
public class DefaultJaquariusEngine implements JaquariusEngine {

  @Override
  public Module loadModule(List<Class<?>> unitClasses, String[] args) {
    return ModuleLoader.loadModule(unitClasses, args);
  }

  @Override
  public Module getCurrentModule() {
    KernelModule km = KernelFunctions.currentModuleSilently();
    return km != null ? km.module() : null;
  }
}
