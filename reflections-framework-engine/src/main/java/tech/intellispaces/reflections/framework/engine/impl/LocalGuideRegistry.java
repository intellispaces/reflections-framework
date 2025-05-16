package tech.intellispaces.reflections.framework.engine.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import tech.intellispaces.reflections.framework.engine.GuideRegistry;
import tech.intellispaces.reflections.framework.exception.ConfigurationExceptions;
import tech.intellispaces.reflections.framework.guide.Guide;
import tech.intellispaces.reflections.framework.guide.GuideKind;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;

/**
 * The local guide register implementation.
 */
class LocalGuideRegistry implements GuideRegistry {
  private final ObjectGuideRegistry objectGuideRegistry = new ObjectGuideRegistry();
  private final UnitGuideRegistry unitGuideRegistry = new UnitGuideRegistry();
  private final AutoGuideRegistry autoGuideRegistry = new AutoGuideRegistry();
  private final Map<Class<?>, Object> guideUnits = new WeakHashMap<>();
  private final Map<String, Object> name2guideMap = new HashMap<>();

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
  public List<Guide<?, ?>> findGuides(
      GuideKind kind, Class<?> sourceReflectionClass, String cid, ReflectionForm targetForm
  ) {
    var guides = new ArrayList<Guide<?, ?>>();

    List<Guide<?, ?>> objectGuides = objectGuideRegistry.findGuides(kind, sourceReflectionClass, cid);
    for (Guide<?, ?> guide : objectGuides) {
      if (guide.targetForm() == targetForm) {
        guides.add(guide);
      }
    }

    List<Guide<?, ?>> unitGuides = unitGuideRegistry.findGuides(kind, cid);
    for (Guide<?, ?> guide : unitGuides) {
      if (guide.targetForm() == targetForm) {
        guides.add(guide);
      }
    }
    return guides;
  }

  public void addGuideUnit(UnitImpl guideUnit) {
    guideUnits.put(guideUnit.unitClass(), guideUnit.wrapper());
    guideUnit.guides().forEach(unitGuideRegistry::addGuide);
  }
}
