package tech.intellispaces.reflectionsj.system.projection;

import tech.intellispaces.reflectionsj.system.ProjectionSupplier;
import tech.intellispaces.statementsj.method.MethodStatement;

public abstract class InjectedMethodProjectionSupplier implements ProjectionSupplier {
  protected final MethodStatement injectedMethod;

  public InjectedMethodProjectionSupplier(MethodStatement injectedMethod) {
    this.injectedMethod = injectedMethod;
  }
}
