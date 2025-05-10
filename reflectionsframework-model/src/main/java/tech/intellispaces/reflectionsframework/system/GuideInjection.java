package tech.intellispaces.reflectionsframework.system;

public interface GuideInjection extends Injection {

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
