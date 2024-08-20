package tech.intellispaces.core.system.shadow;

import tech.intellispaces.core.guide.Guide;
import tech.intellispaces.core.guide.GuideKind;
import tech.intellispaces.core.system.UnitGuideRegistry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnitGuideRegistryImpl implements UnitGuideRegistry {
  private final Map<String, List<Guide<?, ?>>> mappers = new HashMap<>();
  private final Map<String, List<Guide<?, ?>>> movers = new HashMap<>();

  public void addGuide(Guide<?, ?> guide) {
    if (guide.kind().isMapper()) {
      mappers.computeIfAbsent(guide.tid(), k -> newList(guide));
    } else {
      movers.computeIfAbsent(guide.tid(), k -> newList(guide));
    }
  }

  private List<Guide<?, ?>> newList(Guide<?, ?> guide) {
    List<Guide<?, ?>> list = new ArrayList<>();
    list.add(guide);
    return list;
  }

  @Override
  public List<Guide<?, ?>> findGuides(GuideKind kind, String tid) {
    List<Guide<?, ?>> guides = kind.isMapper() ? mappers.get(tid) : movers.get(tid);
    if (guides == null) {
      return List.of();
    }
    return Collections.unmodifiableList(guides);
  }
}
