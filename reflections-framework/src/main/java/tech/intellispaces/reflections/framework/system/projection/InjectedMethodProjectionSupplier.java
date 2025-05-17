package tech.intellispaces.reflections.framework.system.projection;

import tech.intellispaces.jstatements.method.MethodStatement;
import tech.intellispaces.reflections.framework.system.ProjectionSupplier;

public abstract class InjectedMethodProjectionSupplier implements ProjectionSupplier {
  protected final MethodStatement injectedMethod;

  public InjectedMethodProjectionSupplier(MethodStatement injectedMethod) {
    this.injectedMethod = injectedMethod;
  }
}
