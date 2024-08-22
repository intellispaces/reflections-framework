package intellispaces.core.system.projection;

import intellispaces.core.system.ProjectionProvider;

import java.lang.reflect.Method;

public abstract class AbstractProjectionProvider implements ProjectionProvider {
  protected final Method projectionMethod;

  AbstractProjectionProvider(Method projectionMethod) {
    this.projectionMethod = projectionMethod;
  }
}
