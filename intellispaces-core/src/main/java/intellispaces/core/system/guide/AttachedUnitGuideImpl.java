package intellispaces.core.system.guide;

import intellispaces.core.guide.Guide;
import intellispaces.core.system.AttachedUnitGuide;

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
