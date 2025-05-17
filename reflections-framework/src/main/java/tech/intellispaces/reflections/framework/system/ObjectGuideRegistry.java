package tech.intellispaces.reflections.framework.system;

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
  private final Map<Class<?>, ReflectionDescription> reflectionDescriptions = new WeakHashMap<>();

  public List<Guide<?, ?>> findGuides(GuideKind kind, Class<?> reflectionClass, String channelId) {
    if (!ReflectionFunctions.isCustomReflectionClass(reflectionClass) || reflectionClass.isInterface()) {
      return List.of();
    }
    ReflectionDescription description = reflectionDescriptions.computeIfAbsent(
            reflectionClass, this::createReflectionDescription);
    return description.findGuides(kind, channelId);
  }

  private ReflectionDescription createReflectionDescription(Class<?> reflectionClass) {
    ReflectionDescription description = new ReflectionDescription(reflectionClass);
    Class<?> actualReflectionClass = ReflectionFunctions.getReflectionClass(reflectionClass);
    if (actualReflectionClass == null) {
      return description;
    }
    List<Guide<?, ?>> objectGuides = GuideFunctions.loadObjectGuides(actualReflectionClass);
    objectGuides.forEach(description::addGuide);
    return description;
  }

  private static final class ReflectionDescription {
    private final Class<?> reflectionClass;
    private final Map<String, List<Guide<?, ?>>> mapperGuides = new HashMap<>();
    private final Map<String, List<Guide<?, ?>>> moverGuides = new HashMap<>();
    private final Map<String, List<Guide<?, ?>>> mapperOfMovingGuides = new HashMap<>();

    ReflectionDescription(Class<?> reflectionClass) {
      this.reflectionClass = reflectionClass;
    }

    Class<?> getReflectionClass() {
      return reflectionClass;
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

      Class<?> domainClass = ReflectionFunctions.getReflectionDomainClass(reflectionClass);
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
