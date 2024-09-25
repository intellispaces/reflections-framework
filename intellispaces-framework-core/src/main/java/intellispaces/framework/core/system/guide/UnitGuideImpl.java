package intellispaces.framework.core.system.guide;

import intellispaces.framework.core.guide.Guide;
import intellispaces.framework.core.system.UnitGuide;

class UnitGuideImpl implements UnitGuide {
  private final Guide<?, ?> guide;

   UnitGuideImpl(Guide<?, ?> guide) {
    this.guide = guide;
  }

  @Override
  public Guide<?, ?> guide() {
    return guide;
  }
}
