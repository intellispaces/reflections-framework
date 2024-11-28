package tech.intellispaces.jaquarius.system.kernel;

import tech.intellispaces.jaquarius.guide.Guide;
import tech.intellispaces.jaquarius.guide.GuideFunctions;
import tech.intellispaces.jaquarius.guide.GuideKind;
import tech.intellispaces.jaquarius.object.ObjectHandleFunctions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

class ObjectGuideRegistry {
  private final Map<Class<?>, HandleDescription> handleDescriptions = new WeakHashMap<>();

  public List<Guide<?, ?>> getGuides(GuideKind kind, Class<?> objectHandleClass, String cid) {
    if (!ObjectHandleFunctions.isCustomObjectHandleClass(objectHandleClass) || objectHandleClass.isInterface()) {
      return List.of();
    }
    HandleDescription description = handleDescriptions.computeIfAbsent(
        objectHandleClass, this::createHandleDescription);
    return description.getGuides(kind, cid);
  }

  private HandleDescription createHandleDescription(Class<?> objectHandleClass) {
    HandleDescription handleDescription = new HandleDescription(objectHandleClass);
    Class<?> actualObjectHandleClass = ObjectHandleFunctions.getObjectHandleClass(objectHandleClass);
    if (actualObjectHandleClass == null) {
      return handleDescription;
    }
    List<Guide<?, ?>> objectGuides = GuideFunctions.loadObjectGuides(actualObjectHandleClass);
    objectGuides.forEach(handleDescription::addGuide);
    return handleDescription;
  }

  private final class HandleDescription {
    private final Class<?> objectHandleClass;
    private final Map<String, List<Guide<?, ?>>> mapperGuides = new HashMap<>();
    private final Map<String, List<Guide<?, ?>>> moverGuides = new HashMap<>();
    private final Map<String, List<Guide<?, ?>>> mapperOfMovingGuides = new HashMap<>();

    HandleDescription(Class<?> objectHandleClass) {
      this.objectHandleClass = objectHandleClass;
    }

    Class<?> getObjectHandleClass() {
      return objectHandleClass;
    }

    List<Guide<?, ?>> getGuides(GuideKind kind, String cid) {
      List<Guide<?, ?>> guides = null;
      if (kind.isMapper()) {
        guides = mapperGuides.get(cid);
      } else if (kind.isMover()) {
        guides = moverGuides.get(cid);
      } else if (kind.isMapperOfMoving()) {
        guides = mapperOfMovingGuides.get(cid);
      }
      return guides != null ? guides : List.of();
    }

    void addGuide(Guide<?, ?> guide) {
      if (guide.kind().isMapper()) {
        mapperGuides.computeIfAbsent(guide.cid(), k -> new ArrayList<>()).add(guide);
      } else if (guide.kind().isMover()) {
        moverGuides.computeIfAbsent(guide.cid(), k -> new ArrayList<>()).add(guide);
      } else if (guide.kind().isMapperOfMoving()) {
        mapperOfMovingGuides.computeIfAbsent(guide.cid(), k -> new ArrayList<>()).add(guide);
      }
    }
  }
}
