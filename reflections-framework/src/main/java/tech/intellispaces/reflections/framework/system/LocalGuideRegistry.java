package tech.intellispaces.reflections.framework.system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tech.intellispaces.commons.type.ClassFunctions;
import tech.intellispaces.core.Rid;
import tech.intellispaces.reflections.framework.guide.GuideType;
import tech.intellispaces.reflections.framework.guide.SystemGuide;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;

public class LocalGuideRegistry implements GuideRegistry {
  private final Map<Rid, List<SystemGuide<?, ?>>> mapperGuides = new HashMap<>();
  private final Map<Rid, List<SystemGuide<?, ?>>> moverGuides = new HashMap<>();
  private final Map<Rid, List<SystemGuide<?, ?>>> mapperOfMovingGuides = new HashMap<>();

  @Override
  public void addGuide(SystemGuide<?, ?> guide) {
    if (guide.kind().isMapper()) {
      mapperGuides.computeIfAbsent(guide.channelId(), k -> new ArrayList<>()).add(guide);
    } else if (guide.kind().isMover()) {
      moverGuides.computeIfAbsent(guide.channelId(), k -> new ArrayList<>()).add(guide);
    } else if (guide.kind().isMapperOfMoving()) {
      mapperOfMovingGuides.computeIfAbsent(guide.channelId(), k -> new ArrayList<>()).add(guide);
    }
  }

  @Override
  public List<SystemGuide<?, ?>> findGuides(
      Rid cid, GuideType kind, Class<?> sourceClass, ReflectionForm targetForm
  ) {
    List<SystemGuide<?, ?>> resultGuides = new ArrayList<>();
    List<SystemGuide<?, ?>> guides = findGuides(cid, kind);
    for (SystemGuide<?, ?> guide : guides) {
      if (isSuitableGuide(guide, sourceClass, targetForm)) {
        resultGuides.add(guide);
      }
    }
    return Collections.unmodifiableList(resultGuides);
  }

  boolean isSuitableGuide(
      SystemGuide<?, ?> guide, Class<?> sourceClass, ReflectionForm targetForm
  ) {
    return ClassFunctions.isCompatibleClasses(guide.sourceClass(), sourceClass)
        && (guide.targetForm() == targetForm);
  }

  private List<SystemGuide<?, ?>> findGuides(Rid cid, GuideType guideType) {
    List<SystemGuide<?, ?>> guides = null;
    if (guideType.isMapper()) {
      guides = mapperGuides.get(cid);
    } else if (guideType.isMover()) {
      guides = moverGuides.get(cid);
    } else if (guideType.isMapperOfMoving()) {
      guides = mapperOfMovingGuides.get(cid);
    }
    if (guides == null) {
      return List.of();
    }
    return guides;
  }
}
