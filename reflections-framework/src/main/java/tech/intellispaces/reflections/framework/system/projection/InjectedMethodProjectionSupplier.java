package tech.intellispaces.reflections.framework.system.projection;

import tech.intellispaces.reflections.framework.system.ProjectionSupplier;
import tech.intellispaces.jstatements.method.MethodStatement;

public abstract class InjectedMethodProjectionSupplier implements ProjectionSupplier {
  protected final MethodStatement injectedMethod;

  public InjectedMethodProjectionSupplier(MethodStatement injectedMethod) {
    this.injectedMethod = injectedMethod;
  }
}
