package tech.intellispaces.reflections.framework.reflection;

/**
 * The qualified reflection identifier (QRID).
 */
public interface QualifiedReflectionIdentifier {

  /**
   * Domain identifier.
   */
  String did();

  /**
   * Reflection identifier.
   */
  String rid();
}
