package tech.intellispaces.core.system;

import java.lang.reflect.Method;

/**
 * Projection provider declared in system unit with annotation @Projection.
 */
public interface UnitProjectionDefinition extends ProjectionDefinition {

  Unit unit();

  Method projectionMethod();
}
