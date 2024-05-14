package tech.intellispacesframework.core.system;

import tech.intellispacesframework.commons.exception.UnexpectedViolationException;
import tech.intellispacesframework.core.exception.ConfigurationException;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;

/**
 * Default system module starter.
 */
class ModuleDefaultStarter {

  public void startModule(ModuleDefault module) {
    loadProjections(module);
    invokeStartupMethod(module);
    module.setStarted();
  }

  private void loadProjections(ModuleDefault module) {
    module.projectionRegistry().load();
  }

  private void invokeStartupMethod(ModuleDefault module) {
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

  private Object[] makeMethodArguments(Method method, ModuleDefault module) {
    var arguments = new ArrayList<>();
    for (Parameter param : method.getParameters()) {
      Object projection = module.projectionRegistry().projection(param.getName(), param.getType());
      if (projection == null) {
        throw ConfigurationException.withMessage("Cannot to resolve parameter '{}' in method '{}' in unit {}",
            param.getName(), method.getName(), method.getDeclaringClass().getCanonicalName());
      }
      arguments.add(projection);
    }
    return arguments.toArray();
  }
}
