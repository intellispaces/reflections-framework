package tech.intellispaces.reflections.framework.action;

import java.util.ArrayList;

import tech.intellispaces.actions.AbstractAction0;
import tech.intellispaces.actions.Action;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.javareflection.method.MethodParam;
import tech.intellispaces.javareflection.method.MethodStatement;
import tech.intellispaces.reflections.framework.exception.ConfigurationExceptions;
import tech.intellispaces.reflections.framework.system.Modules;
import tech.intellispaces.reflections.framework.system.UnitWrapper;

public class InvokeUnitMethodAction<R> extends AbstractAction0<R> {
  private final UnitWrapper unitInstance;
  private final MethodStatement method;
  private final Action methodAction;

  public InvokeUnitMethodAction(UnitWrapper unitInstance, MethodStatement method, Action methodAction) {
    this.unitInstance = unitInstance;
    this.method = method;
    this.methodAction = methodAction;
  }

  public UnitWrapper unit() {
    return unitInstance;
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
    params.add(unitInstance);
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
