package tech.intellispaces.core.system;

import tech.intellispaces.core.guide.Guide;
import tech.intellispaces.core.guide.GuideKind;

import java.util.List;

/**
 * Module guide register.
 */
public interface ModuleGuideRegistry {

  List<Guide<?, ?>> findGuides(GuideKind kind, String tid);

}
