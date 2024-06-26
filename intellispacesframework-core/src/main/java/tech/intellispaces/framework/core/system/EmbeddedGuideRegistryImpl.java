package tech.intellispaces.framework.core.system;

import tech.intellispaces.framework.core.guide.Guide;
import tech.intellispaces.framework.core.guide.GuideFunctions;
import tech.intellispaces.framework.core.guide.GuideKind;
import tech.intellispaces.framework.core.object.ObjectFunctions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class EmbeddedGuideRegistryImpl implements EmbeddedGuideRegistry {
  private final Map<Class<?>, HandleDescription> handle2DescriptionMap = new WeakHashMap<>();

  @Override
  public Guide<?, ?> getGuide(Class<?> objectHandleClass, GuideKind kind, String tid) {
    HandleDescription description = handle2DescriptionMap.computeIfAbsent(
        objectHandleClass, this::createHandleDescription);
    return description.getGuide(kind, tid);
  }

  private HandleDescription createHandleDescription(Class<?> objectHandleClass) {
    HandleDescription handleDescription = new HandleDescription(objectHandleClass);
    Class<?> actualObjectHandleClass = ObjectFunctions.getObjectHandleClass(objectHandleClass);
    if (actualObjectHandleClass == null) {
      return handleDescription;
    }
    List<Guide<?, ?>> embeddedGuides = GuideFunctions.loadEmbeddedGuides(actualObjectHandleClass);
    embeddedGuides.forEach(handleDescription::addGuide);
    return handleDescription;
  }

  private final class HandleDescription {
    private final Class<?> objectHandleClass;
    private final Map<String, Guide<?, ?>> embeddedMappers = new HashMap<>();
    private final Map<String, Guide<?, ?>> embeddedMovers = new HashMap<>();

    HandleDescription(Class<?> objectHandleClass) {
      this.objectHandleClass = objectHandleClass;
    }

    Class<?> getObjectHandleClass() {
      return objectHandleClass;
    }

    Guide<?, ?> getGuide(GuideKind kind, String tid) {
      if (kind.isMapper()) {
        return embeddedMappers.get(tid);
      } else {
        return embeddedMovers.get(tid);
      }
    }

    void addGuide(Guide<?, ?> guide) {
      if (guide.kind().isMapper()) {
        embeddedMappers.put(guide.tid(), guide);
      } else {
        embeddedMovers.put(guide.tid(), guide);
      }
    }
  }
}
