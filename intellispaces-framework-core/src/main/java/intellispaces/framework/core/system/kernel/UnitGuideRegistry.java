package intellispaces.framework.core.system.kernel;

import intellispaces.framework.core.guide.Guide;
import intellispaces.framework.core.guide.GuideKind;

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
      mapperGuides.computeIfAbsent(guide.cid(), k -> newList(guide));
    } else if (guide.kind().isMover()) {
      moverGuides.computeIfAbsent(guide.cid(), k -> newList(guide));
    } else if (guide.kind().isMapperOfMoving()) {
      mapperOfMovingGuides.computeIfAbsent(guide.cid(), k -> newList(guide));
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

  private List<Guide<?, ?>> newList(Guide<?, ?> guide) {
    List<Guide<?, ?>> list = new ArrayList<>();
    list.add(guide);
    return list;
  }
}
