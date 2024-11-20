package intellispaces.jaquarius.system.injection;

import intellispaces.jaquarius.system.AutoGuideInjection;
import intellispaces.jaquarius.system.InjectionKind;
import intellispaces.jaquarius.system.kernel.KernelFunctions;
import tech.intellispaces.entity.exception.UnexpectedExceptions;

class AutoGuideInjectionImpl implements AutoGuideInjection {
  private final Class<?> unitClass;
  private final String name;
  private final Class<?> guideClass;
  private Object guide;

  AutoGuideInjectionImpl(Class<?> unitClass, String name, Class<?> guideClass) {
    this.unitClass = unitClass;
    this.name = name;
    this.guideClass = guideClass;
  }

  @Override
  public InjectionKind kind() {
    return InjectionKinds.AutoGuideInjection;
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
      guide = KernelFunctions.currentModuleSilently().guideRegistry().getAutoGuide(guideClass);
      if (guide == null) {
        throw UnexpectedExceptions.withMessage("Value of auto guide injection '{0}' in unit {1} is not defined",
            name(), unitClass.getCanonicalName());
      }
    }
    return guide;
  }
}
