package intellispaces.framework.core.system.guide;

import intellispaces.framework.core.guide.Guide;
import intellispaces.framework.core.system.UnitGuide;

public interface UnitGuides {

  static UnitGuide get(Guide<?, ?> guide) {
    return new UnitGuideImpl(guide);
  }
}
