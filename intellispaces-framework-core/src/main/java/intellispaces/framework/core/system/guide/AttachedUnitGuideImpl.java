package intellispaces.framework.core.system.guide;

import intellispaces.framework.core.guide.Guide;
import intellispaces.framework.core.system.AttachedUnitGuide;

class AttachedUnitGuideImpl implements AttachedUnitGuide {
  private final Guide<?, ?> guide;

   AttachedUnitGuideImpl(Guide<?, ?> guide) {
    this.guide = guide;
  }

  @Override
  public Guide<?, ?> guide() {
    return guide;
  }
}
