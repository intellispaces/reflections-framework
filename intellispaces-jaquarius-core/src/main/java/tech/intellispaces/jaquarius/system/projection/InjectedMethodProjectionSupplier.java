package tech.intellispaces.jaquarius.system.projection;

import tech.intellispaces.commons.reflection.method.MethodStatement;
import tech.intellispaces.jaquarius.system.ProjectionSupplier;

public abstract class InjectedMethodProjectionSupplier implements ProjectionSupplier {
  protected final MethodStatement injectedMethod;

  public InjectedMethodProjectionSupplier(MethodStatement injectedMethod) {
    this.injectedMethod = injectedMethod;
  }
}
