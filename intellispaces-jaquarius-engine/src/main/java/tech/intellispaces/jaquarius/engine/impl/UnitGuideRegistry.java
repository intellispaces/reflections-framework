package tech.intellispaces.jaquarius.engine.impl;

import tech.intellispaces.jaquarius.guide.Guide;
import tech.intellispaces.jaquarius.guide.GuideKind;

import java.util.*;

class UnitGuideRegistry {
  private final Map<String, List<Guide<?, ?>>> mapperGuides = new HashMap<>();
  private final Map<String, List<Guide<?, ?>>> moverGuides = new HashMap<>();
  private final Map<String, List<Guide<?, ?>>> mapperOfMovingGuides = new HashMap<>();

  public void addGuide(Guide<?, ?> guide) {
    if (guide.kind().isMapper()) {
      mapperGuides.computeIfAbsent(guide.cid(), k -> new ArrayList<>()).add(guide);
    } else if (guide.kind().isMover()) {
      moverGuides.computeIfAbsent(guide.cid(), k -> new ArrayList<>()).add(guide);
    } else if (guide.kind().isMapperOfMoving()) {
      mapperOfMovingGuides.computeIfAbsent(guide.cid(), k -> new ArrayList<>()).add(guide);
    }
  }

  public List<Guide<?, ?>> findGuides(GuideKind kind, String cid) {
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
    return Collections.unmodifiableList(guides);
  }
}
