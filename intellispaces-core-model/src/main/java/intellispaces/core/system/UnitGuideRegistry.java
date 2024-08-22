package intellispaces.core.system;

import intellispaces.core.guide.Guide;
import intellispaces.core.guide.GuideKind;

import java.util.List;

/**
 * Unit guide register.
 */
public interface UnitGuideRegistry {

  List<Guide<?, ?>> findGuides(GuideKind kind, String tid);
}
