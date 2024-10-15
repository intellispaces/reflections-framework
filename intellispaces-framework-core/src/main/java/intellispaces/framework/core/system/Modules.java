package intellispaces.framework.core.system;

import intellispaces.common.base.exception.UnexpectedViolationException;
import intellispaces.framework.core.system.kernel.KernelFunctions;
import intellispaces.framework.core.system.kernel.KernelModule;

public interface Modules {

  static Module current() {
    Module module = currentSilently();
    if (module == null) {
      throw UnexpectedViolationException.withMessage("Current module is not defined. " +
          "It is possible that the module is not loaded yet");
    }
    return module;
  }

  static Module currentSilently() {
    KernelModule kernelModule = KernelFunctions.currentModuleSilently();
    return kernelModule != null ? kernelModule.module() : null;
  }
}
