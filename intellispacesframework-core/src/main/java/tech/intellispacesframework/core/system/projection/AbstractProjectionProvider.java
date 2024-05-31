package tech.intellispacesframework.core.system.projection;

import tech.intellispacesframework.core.system.ProjectionProvider;

import java.lang.reflect.Method;

public abstract class AbstractProjectionProvider implements ProjectionProvider {
  protected final Method projectionMethod;

  AbstractProjectionProvider(Method projectionMethod) {
    this.projectionMethod = projectionMethod;
  }
}
