package tech.intellispaces.reflections.framework.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.commons.text.StringFunctions;
import tech.intellispaces.reflections.framework.exception.ConfigurationExceptions;
import tech.intellispaces.reflections.framework.guide.Guide;
import tech.intellispaces.reflections.framework.guide.GuideKind;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;

/**
 * The guide manager.
 */
public class GuideManager implements GuideRegistry {
  private final GuideRegistry guideRegistry;
  private final AutoGuideRegistry autoGuideRegistry;
  private final LocalReflectionGuideRegistry reflectionGuideRegistry;
  private final Map<Class<?>, Object> guideUnits = new WeakHashMap<>();
  private final Map<String, Object> name2guideMap = new HashMap<>();

  public GuideManager() {
    this.guideRegistry = new LocalGuideRegistry();
    this.autoGuideRegistry = new AutoGuideRegistry();
    this.reflectionGuideRegistry = new LocalReflectionGuideRegistry();
  }

  public GuideManager(
      GuideRegistry guideRegistry,
      LocalReflectionGuideRegistry reflectionGuideRegistry
  ) {
    this.guideRegistry = guideRegistry;
    this.autoGuideRegistry = new AutoGuideRegistry();
    this.reflectionGuideRegistry = reflectionGuideRegistry;
  }

  public <G> G getAutoGuide(Class<G> guideType) {
    return autoGuideRegistry.getAutoGuide(guideType);
  }

  @SuppressWarnings("unchecked")
  public <G> G getGuide(String name, Class<G> guideClass) {
    if (StringFunctions.isNotBlank(name) && guideClass != null) {
      G guide = (G) name2guideMap.get(name);
      if (guide != null) {
        if (guideClass != guide.getClass() && !guideClass.isAssignableFrom(guide.getClass())) {
          throw ConfigurationExceptions.withMessage("Guide with name '{0}' but another class already registered");
        }
      }
      return guide;
    }
    if (StringFunctions.isNullOrBlank(name) && guideClass != null) {
      return (G) guideUnits.get(guideClass);
    }
    return null;
  }

  public void addGuide(Class<?> guideClass, Object guideInstance) {
    guideUnits.put(guideClass, guideInstance);
  }

  @Override
  public void addGuide(Guide<?, ?> guide) {
    guideRegistry.addGuide(guide);
  }

  @Override
  public List<Guide<?, ?>> findGuides(
      String cid, GuideKind kind, Class<?> sourceReflectionClass, ReflectionForm targetForm
  ) {
    var guides = new ArrayList<Guide<?, ?>>();

    List<Guide<?, ?>> objectGuides = reflectionGuideRegistry.findGuides(kind, sourceReflectionClass, cid);
    for (Guide<?, ?> guide : objectGuides) {
      if (guide.targetForm() == targetForm) {
        guides.add(guide);
      }
    }

    List<Guide<?, ?>> unitGuides = guideRegistry.findGuides(cid, kind);
    for (Guide<?, ?> guide : unitGuides) {
      if (guide.targetForm() == targetForm) {
        guides.add(guide);
      }
    }
    return guides;
  }

  @Override
  public List<Guide<?, ?>> findGuides(String cid, GuideKind kind) {
    throw NotImplementedExceptions.withCode("LI62RA");
  }
}
