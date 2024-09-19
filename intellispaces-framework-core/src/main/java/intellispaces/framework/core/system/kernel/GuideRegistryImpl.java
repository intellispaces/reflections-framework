package intellispaces.framework.core.system.kernel;

import intellispaces.common.base.exception.UnexpectedViolationException;
import intellispaces.common.base.type.TypeFunctions;
import intellispaces.framework.core.common.NameConventionFunctions;
import intellispaces.framework.core.exception.ConfigurationException;
import intellispaces.framework.core.guide.Guide;
import intellispaces.framework.core.guide.GuideKind;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

class GuideRegistryImpl implements GuideRegistry {
  private final AttachedObjectGuideRegistry attachedObjectGuideRegistry = new AttachedObjectGuideRegistry();
  private final AttachedUnitGuideRegistry attachedUnitGuideRegistry = new AttachedUnitGuideRegistry();
  private final Map<Class<?>, Object> guideUnits = new WeakHashMap<>();
  private final Map<String, Object> name2guideMap = new HashMap<>();

  @Override
  @SuppressWarnings("unchecked")
  public <G> G getGuide(String name, Class<G> guideClass) {
    G guide = (G) name2guideMap.get(name);
    if (guide != null) {
      if (guideClass != guide.getClass() && !guideClass.isAssignableFrom(guide.getClass())) {
        throw ConfigurationException.withMessage("Guide with name ''{0}'' but another class " +
            "already registered in module");
      }
      return guide;
    }

    if (guideClass.isInterface()) {
      guide = createAutoGuide(guideClass);
    } else {
      guide = (G) guideUnits.get(guideClass);
    }
    if (guide != null) {
      name2guideMap.put(name, guide);
      return guide;
    }
    return null;
  }

  @Override
  public List<Guide<?, ?>> findGuides(GuideKind kind, Class<?> objectHandleClass, String tid) {
    var guides = new ArrayList<Guide<?, ?>>();
    Guide<?, ?> guide = attachedObjectGuideRegistry.getGuide(kind, objectHandleClass, tid);
    if (guide != null) {
      guides.add(guide);
    }
    guides.addAll(attachedUnitGuideRegistry.findGuides(kind, tid));
    return guides;
  }

  @Override
  public void addGuideUnit(SystemUnit guideUnit) {
    guideUnits.put(guideUnit.unitClass(), guideUnit.instance());
    guideUnit.guides().forEach(g -> attachedUnitGuideRegistry.addGuide(g.guide()));
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
