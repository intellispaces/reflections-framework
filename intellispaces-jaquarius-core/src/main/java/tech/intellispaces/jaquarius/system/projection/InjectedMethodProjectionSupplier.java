package tech.intellispaces.jaquarius.system.projection;

import tech.intellispaces.jaquarius.system.ProjectionSupplier;
import tech.intellispaces.reflection.method.MethodStatement;

public abstract class InjectedMethodProjectionSupplier implements ProjectionSupplier {
  protected final MethodStatement injectedMethod;

  public InjectedMethodProjectionSupplier(MethodStatement injectedMethod) {
    this.injectedMethod = injectedMethod;
  }
}
