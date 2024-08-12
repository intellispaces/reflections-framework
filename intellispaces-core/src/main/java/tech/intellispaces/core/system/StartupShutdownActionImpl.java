package tech.intellispaces.core.system;

import tech.intellispaces.actions.AbstractAction0;
import tech.intellispaces.commons.exception.UnexpectedViolationException;
import tech.intellispaces.core.exception.ConfigurationException;
import tech.intellispaces.core.system.action.ShutdownAction;
import tech.intellispaces.core.system.action.StartupAction;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;

public class StartupShutdownActionImpl extends AbstractAction0<Void> implements StartupAction, ShutdownAction {
  private final UnitWrapper unitInstance;
  private final Method method;

  public StartupShutdownActionImpl(UnitWrapper unitInstance, Method method) {
    this.unitInstance = unitInstance;
    this.method = method;
  }

  @Override
  public Method method() {
    return method;
  }

  @Override
  public Void execute() {
    Object[] arguments = makeMethodArguments();
    try {
      method.invoke(unitInstance, arguments);
    } catch (Exception e) {
      throw UnexpectedViolationException.withCauseAndMessage(e, "Can't to invoke unit method {}", method.getName());
    }
    return null;
  }

  private Object[] makeMethodArguments() {
    var arguments = new ArrayList<>();
    for (Parameter param : method.getParameters()) {
      Object projection = Modules.current().getProjection(param.getName(), param.getType());
      if (projection == null) {
        throw ConfigurationException.withMessage("Cannot to resolve parameter '{}' in method '{}' in unit {}",
            param.getName(), method.getName(), method.getDeclaringClass().getCanonicalName());
      }
      arguments.add(projection);
    }
    return arguments.toArray();
  }
}
