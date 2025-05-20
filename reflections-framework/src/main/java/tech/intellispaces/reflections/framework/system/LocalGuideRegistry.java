package tech.intellispaces.reflections.framework.system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tech.intellispaces.reflections.framework.guide.Guide;
import tech.intellispaces.reflections.framework.guide.GuideKind;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;

public class LocalGuideRegistry implements GuideRegistry {
  private final Map<String, List<Guide<?, ?>>> mapperGuides = new HashMap<>();
  private final Map<String, List<Guide<?, ?>>> moverGuides = new HashMap<>();
  private final Map<String, List<Guide<?, ?>>> mapperOfMovingGuides = new HashMap<>();

  @Override
  public void addGuide(Guide<?, ?> guide) {
    if (guide.kind().isMapper()) {
      mapperGuides.computeIfAbsent(guide.channelId(), k -> new ArrayList<>()).add(guide);
    } else if (guide.kind().isMover()) {
      moverGuides.computeIfAbsent(guide.channelId(), k -> new ArrayList<>()).add(guide);
    } else if (guide.kind().isMapperOfMoving()) {
      mapperOfMovingGuides.computeIfAbsent(guide.channelId(), k -> new ArrayList<>()).add(guide);
    }
  }

  @Override
  public List<Guide<?, ?>> findGuides(
      String cid, GuideKind kind, Class<?> sourceReflectionClass, ReflectionForm targetForm
  ) {
    List<Guide<?, ?>> resultGuides = new ArrayList<>();
    List<Guide<?, ?>> guides = findGuides(cid, kind);
    for (Guide<?, ?> guide : guides) {
      if (guide.targetForm() == targetForm) {
        resultGuides.add(guide);
      }
    }
    return Collections.unmodifiableList(resultGuides);
  }

  private List<Guide<?, ?>> findGuides(String cid, GuideKind kind) {
    List<Guide<?, ?>> guides = null;
    if (kind.isMapper()) {
      guides = mapperGuides.get(cid);
    } else if (kind.isMover()) {
      guides = moverGuides.get(cid);
    } else if (kind.isMapperOfMoving()) {
      guides = mapperOfMovingGuides.get(cid);
    }
    if (guides == null) {
      return List.of();
    }
    return guides;
  }
}
