package intellispaces.core.system.action;

import intellispaces.actions.AbstractAction0;
import intellispaces.commons.exception.UnexpectedViolationException;
import intellispaces.core.exception.ConfigurationException;
import intellispaces.core.system.Modules;
import intellispaces.core.system.UnitWrapper;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;

public class InvokeUnitMethodAction<R> extends AbstractAction0<R> {
  private final UnitWrapper unitInstance;
  private final Method unitMethod;

  public InvokeUnitMethodAction(UnitWrapper unitInstance, Method unitMethod) {
    this.unitInstance = unitInstance;
    this.unitMethod = unitMethod;
  }

  public Method getUnitMethod() {
    return unitMethod;
  }

  @Override
  @SuppressWarnings("unchecked")
  public R execute() {
    Object[] arguments = makeMethodArguments();
    try {
      return (R) unitMethod.invoke(unitInstance, arguments);
    } catch (Exception e) {
      throw UnexpectedViolationException.withCauseAndMessage(e, "Can't to invoke unit method {}", unitMethod.getName());
    }
  }

  private Object[] makeMethodArguments() {
    var arguments = new ArrayList<>();
    for (Parameter param : unitMethod.getParameters()) {
      Object projection = Modules.current().getProjection(param.getName(), param.getType());
      if (projection == null) {
        throw ConfigurationException.withMessage("Cannot to resolve parameter '{}' in method '{}' in unit {}",
            param.getName(), unitMethod.getName(), unitMethod.getDeclaringClass().getCanonicalName());
      }
      arguments.add(projection);
    }
    return arguments.toArray();
  }
}
