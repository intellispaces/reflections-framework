package tech.intellispaces.jaquarius.system.kernel;

import tech.intellispaces.jaquarius.exception.ConfigurationExceptions;
import tech.intellispaces.jaquarius.guide.Guide;
import tech.intellispaces.jaquarius.guide.GuideForm;
import tech.intellispaces.jaquarius.guide.GuideKind;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

class GuideRegistryImpl implements GuideRegistry {
  private final ObjectGuideRegistry objectGuideRegistry = new ObjectGuideRegistry();
  private final UnitGuideRegistry unitGuideRegistry = new UnitGuideRegistry();
  private final AutoGuideRegistry autoGuideRegistry = new AutoGuideRegistry();
  private final Map<Class<?>, Object> guideUnits = new WeakHashMap<>();
  private final Map<String, Object> name2guideMap = new HashMap<>();

  @Override
  public <G> G getAutoGuide(Class<G> guideClass) {
    return autoGuideRegistry.getAutoGuide(guideClass);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <G> G getGuide(String name, Class<G> guideClass) {
    G guide = (G) name2guideMap.get(name);
    if (guide != null) {
      if (guideClass != guide.getClass() && !guideClass.isAssignableFrom(guide.getClass())) {
        throw ConfigurationExceptions.withMessage("Guide with name '{0}' but another class " +
            "already registered in module");
      }
      return guide;
    }

    guide = (G) guideUnits.get(guideClass);
    if (guide != null) {
      name2guideMap.put(name, guide);
      return guide;
    }
    return null;
  }

  @Override
  public List<Guide<?, ?>> findGuides(GuideKind kind, Class<?> objectHandleClass, String cid, GuideForm form) {
    var guides = new ArrayList<Guide<?, ?>>();

    List<Guide<?, ?>> objectGuides = objectGuideRegistry.getGuides(kind, objectHandleClass, cid);
    for (Guide<?, ?> guide : objectGuides) {
      if (guide.guideForm() == form) {
        guides.add(guide);
      }
    }

    List<Guide<?, ?>> unitGuides = unitGuideRegistry.findGuides(kind, cid);
    for (Guide<?, ?> guide : unitGuides) {
      if (guide.guideForm() == form) {
        guides.add(guide);
      }
    }
    return guides;
  }

  @Override
  public void addGuideUnit(KernelUnit guideUnit) {
    guideUnits.put(guideUnit.unitClass(), guideUnit.instance());
    guideUnit.guides().forEach(unitGuideRegistry::addGuide);
  }
}
