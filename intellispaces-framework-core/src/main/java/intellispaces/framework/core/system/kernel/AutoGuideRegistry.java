package intellispaces.framework.core.system.kernel;

import intellispaces.common.base.exception.UnexpectedViolationException;
import intellispaces.common.base.type.TypeFunctions;
import intellispaces.framework.core.common.NameConventionFunctions;

import java.util.Map;
import java.util.WeakHashMap;

class AutoGuideRegistry {
  private final Map<Class<?>, Object> autoGuide = new WeakHashMap<>();

  @SuppressWarnings("unchecked")
  public <G> G getAutoGuide(Class<G> guideClass) {
    Object guide = autoGuide.get(guideClass);
    if (guide == null) {
      guide = createAutoGuide(guideClass);
      autoGuide.put(guideClass, guide);
    }
    return (G) guide;
  }

  @SuppressWarnings("unchecked")
  private <G> G createAutoGuide(Class<G> guideClass) {
    String autoGuideCanonicalName = NameConventionFunctions.getAutoGuiderCanonicalName(guideClass.getName());
    Class<G> autoGuideClass = (Class<G>) TypeFunctions.getClass(autoGuideCanonicalName)
        .orElseThrow(() -> UnexpectedViolationException.withMessage("Could not load auto guide class by name {0}",
            autoGuideCanonicalName)
        );
    return TypeFunctions.newInstance(autoGuideClass);
  }
}
