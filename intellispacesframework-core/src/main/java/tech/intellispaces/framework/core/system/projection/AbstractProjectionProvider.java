package tech.intellispaces.framework.core.system.projection;

import tech.intellispaces.framework.core.system.ProjectionProvider;

import java.lang.reflect.Method;

public abstract class AbstractProjectionProvider implements ProjectionProvider {
  protected final Method projectionMethod;

  AbstractProjectionProvider(Method projectionMethod) {
    this.projectionMethod = projectionMethod;
  }
}
