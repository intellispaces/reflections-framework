package tech.intellispaces.framework.core.system;

import tech.intellispaces.framework.core.guide.GuideKind;
import tech.intellispaces.framework.core.guide.Guide;

import java.util.List;

/**
 * Module guide register.
 */
public interface ModuleGuideRegistry {

  List<Guide<?, ?>> findGuides(GuideKind kind, String tid);

}
