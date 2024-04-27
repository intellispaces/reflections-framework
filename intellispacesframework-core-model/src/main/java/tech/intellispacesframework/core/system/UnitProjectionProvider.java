package tech.intellispacesframework.core.system;

import java.lang.reflect.Method;

/**
 * Projection provider declared in system unit with annotation @Projection.
 */
public interface UnitProjectionProvider extends ProjectionProvider {

  SystemUnit unit();

  Method providerMethod();
}
