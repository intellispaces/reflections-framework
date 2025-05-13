package tech.intellispaces.reflections.framework.engine.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import tech.intellispaces.reflections.framework.guide.Guide;
import tech.intellispaces.reflections.framework.guide.GuideFunctions;
import tech.intellispaces.reflections.framework.guide.GuideKind;
import tech.intellispaces.reflections.framework.reflection.ReflectionFunctions;
import tech.intellispaces.reflections.framework.space.channel.ChannelFunctions;

class ObjectGuideRegistry {
  private final Map<Class<?>, HandleDescription> handleDescriptions = new WeakHashMap<>();

  public List<Guide<?, ?>> findGuides(GuideKind kind, Class<?> objectHandleClass, String channelId) {
    if (!ReflectionFunctions.isCustomObjectFormClass(objectHandleClass) || objectHandleClass.isInterface()) {
      return List.of();
    }
    HandleDescription description = handleDescriptions.computeIfAbsent(
        objectHandleClass, this::createHandleDescription);
    return description.findGuides(kind, channelId);
  }

  private HandleDescription createHandleDescription(Class<?> objectHandleClass) {
    HandleDescription handleDescription = new HandleDescription(objectHandleClass);
    Class<?> actualObjectHandleClass = ReflectionFunctions.getObjectHandleClass(objectHandleClass);
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

    List<Guide<?, ?>> findGuides(GuideKind kind, String cid) {
      List<Guide<?, ?>> guides = null;
      if (kind.isMapper()) {
        guides = mapperGuides.get(cid);
      } else if (kind.isMover()) {
        guides = moverGuides.get(cid);
      } else if (kind.isMapperOfMoving()) {
        guides = mapperOfMovingGuides.get(cid);
      }
      if (guides != null) {
        return guides;
      }

      Class<?> domainClass = ReflectionFunctions.getDomainClassOfObjectHandle(objectHandleClass);
      String originDomainChannelId = ChannelFunctions.getOriginDomainChannelId(domainClass, cid);
      if (originDomainChannelId == null) {
        return List.of();
      }
      if (kind.isMapper()) {
        guides = mapperGuides.get(originDomainChannelId);
      } else if (kind.isMover()) {
        guides = moverGuides.get(originDomainChannelId);
      } else if (kind.isMapperOfMoving()) {
        guides = mapperOfMovingGuides.get(originDomainChannelId);
      }
      return guides != null ? guides : List.of();
    }

    void addGuide(Guide<?, ?> guide) {
      if (guide.kind().isMapper()) {
        mapperGuides.computeIfAbsent(guide.channelId(), k -> new ArrayList<>()).add(guide);
      } else if (guide.kind().isMover()) {
        moverGuides.computeIfAbsent(guide.channelId(), k -> new ArrayList<>()).add(guide);
      } else if (guide.kind().isMapperOfMoving()) {
        mapperOfMovingGuides.computeIfAbsent(guide.channelId(), k -> new ArrayList<>()).add(guide);
      }
    }
  }
}
