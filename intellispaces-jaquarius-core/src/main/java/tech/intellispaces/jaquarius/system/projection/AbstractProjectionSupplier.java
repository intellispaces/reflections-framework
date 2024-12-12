package tech.intellispaces.jaquarius.system.projection;

import tech.intellispaces.jaquarius.system.ProjectionSupplier;
import tech.intellispaces.java.reflection.method.MethodStatement;

public abstract class AbstractProjectionSupplier implements ProjectionSupplier {
  protected final MethodStatement projectionMethod;

  public AbstractProjectionSupplier(MethodStatement projectionMethod) {
    this.projectionMethod = projectionMethod;
  }
}
