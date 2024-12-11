package tech.intellispaces.jaquarius.action;

import tech.intellispaces.action.AbstractAction0;
import tech.intellispaces.general.exception.UnexpectedExceptions;
import tech.intellispaces.jaquarius.exception.ConfigurationExceptions;
import tech.intellispaces.jaquarius.system.Modules;
import tech.intellispaces.jaquarius.system.UnitWrapper;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;

public class InvokeUnitMethodAction<R> extends AbstractAction0<R> {
  private final UnitWrapper unit;
  private final Method unitMethod;

  public InvokeUnitMethodAction(UnitWrapper unit, Method unitMethod) {
    this.unit = unit;
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
      return (R) unitMethod.invoke(unit, arguments);
    } catch (Exception e) {
      throw UnexpectedExceptions.withCauseAndMessage(e, "Could not to invoke unit method '{0}'",
          unitMethod.getName());
    }
  }

  private Object[] makeMethodArguments() {
    var arguments = new ArrayList<>();
    for (Parameter param : unitMethod.getParameters()) {
      Object projection = Modules.current().getProjection(param.getName(), param.getType());
      if (projection == null) {
        throw ConfigurationExceptions.withMessage("Cannot to resolve parameter '{0}' in method '{1}' in unit {2}",
            param.getName(), unitMethod.getName(), unitMethod.getDeclaringClass().getCanonicalName());
      }
      arguments.add(projection);
    }
    return arguments.toArray();
  }
}
