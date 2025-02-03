package tech.intellispaces.jaquarius.system.projection;

import tech.intellispaces.commons.java.reflection.method.MethodStatement;
import tech.intellispaces.jaquarius.system.ProjectionSupplier;

public abstract class AbstractProjectionSupplier implements ProjectionSupplier {
  protected final MethodStatement projectionMethod;

  public AbstractProjectionSupplier(MethodStatement projectionMethod) {
    this.projectionMethod = projectionMethod;
  }
}
