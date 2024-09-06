package intellispaces.framework.core.system.guide;

import intellispaces.framework.core.guide.Guide;
import intellispaces.framework.core.system.AttachedUnitGuide;

public interface AttachedUnitGuides {

  static AttachedUnitGuide get(Guide<?, ?> guide) {
    return new AttachedUnitGuideImpl(guide);
  }
}
