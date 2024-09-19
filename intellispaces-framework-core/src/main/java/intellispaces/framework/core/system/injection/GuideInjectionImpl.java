package intellispaces.framework.core.system.injection;

import intellispaces.common.base.exception.UnexpectedViolationException;
import intellispaces.framework.core.system.GuideInjection;
import intellispaces.framework.core.system.InjectionType;
import intellispaces.framework.core.system.kernel.SystemFunctions;

class GuideInjectionImpl implements GuideInjection {
  private final Class<?> unitClass;
  private final String name;
  private final Class<?> guideClass;
  private Object guide;

  GuideInjectionImpl(Class<?> unitClass, String name, Class<?> guideClass) {
    this.unitClass = unitClass;
    this.name = name;
    this.guideClass = guideClass;
  }

  @Override
  public InjectionType type() {
    return InjectionTypes.GuideInjection;
  }

  @Override
  public Class<?> unitClass() {
    return unitClass;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public Class<?> guideClass() {
    return guideClass;
  }

  @Override
  public Object value() {
    if (guide == null) {
      guide = SystemFunctions.currentModuleSilently().guideRegistry().getGuide(name, guideClass);
      if (guide == null) {
        throw UnexpectedViolationException.withMessage("Value of guide injection ''{0}'' in unit {1} is not defined",
            name(), unitClass.getCanonicalName());
      }
    }
    return guide;
  }
}
