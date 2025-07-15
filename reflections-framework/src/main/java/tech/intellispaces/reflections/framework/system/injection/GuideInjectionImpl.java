package tech.intellispaces.reflections.framework.system.injection;

import java.util.List;

import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.reflections.framework.system.GuideInjection;
import tech.intellispaces.reflections.framework.system.InjectionKind;
import tech.intellispaces.reflections.framework.system.Modules;

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
  public InjectionKind kind() {
    return InjectionKinds.SpecificGuide;
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
      guide = Modules.current().getProjection(name, guideClass);
      if (guide == null) {
        List<?> guides = Modules.current().guides(guideClass);
        if (!guides.isEmpty()) {
          guide = guides.get(0);
        }
      }
      if (guide == null) {
        throw UnexpectedExceptions.withMessage("Target of the guide injection '{0}' in unit {1} is not defined",
            name(), unitClass.getCanonicalName());
      }
    }
    return guide;
  }
}
