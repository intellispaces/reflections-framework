package tech.intellispaces.reflectionsj.action;

import java.util.ArrayList;

import tech.intellispaces.actions.AbstractAction0;
import tech.intellispaces.actions.Action;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.reflectionsj.exception.ConfigurationExceptions;
import tech.intellispaces.reflectionsj.system.Modules;
import tech.intellispaces.reflectionsj.system.UnitWrapper;
import tech.intellispaces.statementsj.method.MethodParam;
import tech.intellispaces.statementsj.method.MethodStatement;

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
