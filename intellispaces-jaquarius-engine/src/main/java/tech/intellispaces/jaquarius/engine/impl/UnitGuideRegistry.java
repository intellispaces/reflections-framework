package tech.intellispaces.jaquarius.engine.impl;

import tech.intellispaces.jaquarius.guide.Guide;
import tech.intellispaces.jaquarius.guide.GuideKind;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class UnitGuideRegistry {
  private final Map<String, List<Guide<?, ?>>> mapperGuides = new HashMap<>();
  private final Map<String, List<Guide<?, ?>>> moverGuides = new HashMap<>();
  private final Map<String, List<Guide<?, ?>>> mapperOfMovingGuides = new HashMap<>();

  public void addGuide(Guide<?, ?> guide) {
    if (guide.kind().isMapper()) {
      mapperGuides.computeIfAbsent(guide.channelId(), k -> new ArrayList<>()).add(guide);
    } else if (guide.kind().isMover()) {
      moverGuides.computeIfAbsent(guide.channelId(), k -> new ArrayList<>()).add(guide);
    } else if (guide.kind().isMapperOfMoving()) {
      mapperOfMovingGuides.computeIfAbsent(guide.channelId(), k -> new ArrayList<>()).add(guide);
    }
  }

  public List<Guide<?, ?>> findGuides(GuideKind kind, String channelId) {
    List<Guide<?, ?>> guides = null;
    if (kind.isMapper()) {
      guides = mapperGuides.get(channelId);
    } else if (kind.isMover()) {
      guides = moverGuides.get(channelId);
    } else if (kind.isMapperOfMoving()) {
      guides = mapperOfMovingGuides.get(channelId);
    }
    if (guides == null) {
      return List.of();
    }
    return Collections.unmodifiableList(guides);
  }
}
