package tech.intellispaces.reflections.system;

public interface AutoGuideInjection extends Injection {

  /**
   * Unit class.
   */
  Class<?> unitClass();

  /**
   * Guide name.
   */
  String name();

  /**
   * Guide class.
   */
  Class<?> guideClass();
}
