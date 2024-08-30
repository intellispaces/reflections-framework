package intellispaces.core.system.guide;

import intellispaces.core.guide.Guide;
import intellispaces.core.system.AttachedUnitGuide;

public interface AttachedUnitGuides {

  static AttachedUnitGuide get(Guide<?, ?> guide) {
    return new AttachedUnitGuideImpl(guide);
  }
}
