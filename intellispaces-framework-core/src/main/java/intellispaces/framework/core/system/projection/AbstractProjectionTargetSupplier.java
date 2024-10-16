package intellispaces.framework.core.system.projection;

import intellispaces.framework.core.system.ProjectionTargetSupplier;

import java.lang.reflect.Method;

public abstract class AbstractProjectionTargetSupplier implements ProjectionTargetSupplier {
  protected final Method projectionMethod;

  AbstractProjectionTargetSupplier(Method projectionMethod) {
    this.projectionMethod = projectionMethod;
  }
}
