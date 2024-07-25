package tech.intellispaces.framework.core.system;

import tech.intellispaces.framework.core.guide.Guide;
import tech.intellispaces.framework.core.guide.GuideFunctions;
import tech.intellispaces.framework.core.guide.GuideKind;
import tech.intellispaces.framework.core.object.ObjectFunctions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class AttachedGuideRegistryImpl implements AttachedGuideRegistry {
  private final Map<Class<?>, HandleDescription> handle2DescriptionMap = new WeakHashMap<>();

  @Override
  public Guide<?, ?> getGuide(Class<?> objectHandleClass, GuideKind kind, String tid) {
    HandleDescription description = handle2DescriptionMap.computeIfAbsent(
        objectHandleClass, this::createHandleDescription);
    return description.getGuide(kind, tid);
  }

  private HandleDescription createHandleDescription(Class<?> objectHandleClass) {
    HandleDescription handleDescription = new HandleDescription(objectHandleClass);
    Class<?> actualObjectHandleClass = ObjectFunctions.defineObjectHandleClass(objectHandleClass);
    if (actualObjectHandleClass == null) {
      return handleDescription;
    }
    List<Guide<?, ?>> attachedGuides = GuideFunctions.loadAttachedGuides(actualObjectHandleClass);
    attachedGuides.forEach(handleDescription::addGuide);
    return handleDescription;
  }

  private final class HandleDescription {
    private final Class<?> objectHandleClass;
    private final Map<String, Guide<?, ?>> mappers = new HashMap<>();
    private final Map<String, Guide<?, ?>> movers = new HashMap<>();

    HandleDescription(Class<?> objectHandleClass) {
      this.objectHandleClass = objectHandleClass;
    }

    Class<?> getObjectHandleClass() {
      return objectHandleClass;
    }

    Guide<?, ?> getGuide(GuideKind kind, String tid) {
      if (kind.isMapper()) {
        return mappers.get(tid);
      } else {
        return movers.get(tid);
      }
    }

    void addGuide(Guide<?, ?> guide) {
      if (guide.kind().isMapper()) {
        mappers.put(guide.tid(), guide);
      } else {
        movers.put(guide.tid(), guide);
      }
    }
  }
}
