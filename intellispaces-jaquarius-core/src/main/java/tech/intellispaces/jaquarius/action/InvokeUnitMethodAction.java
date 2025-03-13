package tech.intellispaces.jaquarius.action;

import tech.intellispaces.commons.action.AbstractAction0;
import tech.intellispaces.commons.action.Action;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.commons.reflection.method.MethodParam;
import tech.intellispaces.commons.reflection.method.MethodStatement;
import tech.intellispaces.jaquarius.exception.ConfigurationExceptions;
import tech.intellispaces.jaquarius.system.Modules;
import tech.intellispaces.jaquarius.system.UnitWrapper;

import java.util.ArrayList;

public class InvokeUnitMethodAction<R> extends AbstractAction0<R> {
  private final UnitWrapper unit;
  private final MethodStatement method;
  private final Action methodAction;

  public InvokeUnitMethodAction(UnitWrapper unit, MethodStatement method, Action methodAction) {
    this.unit = unit;
    this.method = method;
    this.methodAction = methodAction;
  }

  public UnitWrapper unit() {
    return unit;
  }

  public MethodStatement method() {
    return method;
  }

  @Override
  @SuppressWarnings("unchecked")
  public R execute() {
    Object[] params = makeActionParams();
    try {
      return (R) methodAction.execute(params);
    } catch (Exception e) {
      throw UnexpectedExceptions.withCauseAndMessage(e, "Could not to invoke unit method '{0}' action",
          method.name());
    }
  }

  private Object[] makeActionParams() {
    var params = new ArrayList<>();

    for (MethodParam param : method.params()) {
      String projectionName = param.name();
      Class<?> projectionTargerClass = param.type().asCustomTypeReferenceOrElseThrow().targetClass();
      Object projection = Modules.current().getProjection(projectionName, projectionTargerClass);
      if (projection == null) {
        throw ConfigurationExceptions.withMessage("Cannot to resolve parameter '{0}' in method '{1}' in unit {2}",
            projectionName, method.name(), method.owner().canonicalName());
      }
      params.add(projection);
    }
    return params.toArray();
  }
}
