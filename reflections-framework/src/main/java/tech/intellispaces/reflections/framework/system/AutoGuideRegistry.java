package tech.intellispaces.reflections.framework.system;

import java.util.Map;
import java.util.WeakHashMap;

import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.commons.object.Objects;
import tech.intellispaces.commons.type.ClassFunctions;
import tech.intellispaces.reflections.framework.naming.NameConventionFunctions;

public class AutoGuideRegistry {
  private final Map<Class<?>, Object> autoGuide = new WeakHashMap<>();

  @SuppressWarnings("unchecked")
  public <G> G getGuide(Class<G> guideType) {
    Object guideInstance = autoGuide.get(guideType);
    if (guideInstance == null) {
      guideInstance = createAutoGuide(guideType);
      autoGuide.put(guideType, guideInstance);
    }
    return (G) guideInstance;
  }

  @SuppressWarnings("unchecked")
  private <G> G createAutoGuide(Class<G> guideType) {
    String autoGuideCanonicalName = NameConventionFunctions.getActionGuideImplementationCanonicalName(guideType.getName());
    Class<G> autoGuideClass = (Class<G>) ClassFunctions.getClass(autoGuideCanonicalName)
        .orElseThrow(() -> UnexpectedExceptions.withMessage("Could not load auto guide class by name {0}",
            autoGuideCanonicalName)
        );
    return Objects.get(autoGuideClass);
  }
}
