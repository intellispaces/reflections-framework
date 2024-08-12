package tech.intellispaces.core.system;

import tech.intellispaces.commons.exception.UnexpectedViolationException;
import tech.intellispaces.core.exception.ConfigurationException;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;

/**
 * Default system module starter.
 */
public interface ModuleStarterFunctions {

  static void startModule(ModuleDefault module) {
    loadProjections(module);
    invokeStartupMethod(module);
  }

  private static void loadProjections(ModuleDefault module) {
    module.projectionRegistry().load();
  }

  private static void invokeStartupMethod(ModuleDefault module) {
    Unit mainUnit = module.mainUnit();
    if (mainUnit.startupMethod().isPresent()) {
      Method startupMethod = mainUnit.startupMethod().get();
      Object[] arguments = makeMethodArguments(startupMethod, module);
      try {
        startupMethod.invoke(mainUnit.instance(), arguments);
      } catch (Exception e) {
        throw UnexpectedViolationException.withCauseAndMessage(e, "Can't to invoke module startup method {}", startupMethod.getName());
      }
    }
  }

  private static Object[] makeMethodArguments(Method method, ModuleDefault module) {
    var arguments = new ArrayList<>();
    for (Parameter param : method.getParameters()) {
      Object projection = module.projectionRegistry().getProjection(param.getName(), param.getType());
      if (projection == null) {
        throw ConfigurationException.withMessage("Cannot to resolve parameter '{}' in method '{}' in unit {}",
            param.getName(), method.getName(), method.getDeclaringClass().getCanonicalName());
      }
      arguments.add(projection);
    }
    return arguments.toArray();
  }
}
