package tech.intellispacesframework.core.system;

import tech.intellispacesframework.core.guide.Guide;
import tech.intellispacesframework.core.guide.GuideKind;

import java.util.List;

/**
 * Module guide register.
 */
public interface ModuleGuideRegistry {

  List<Guide<?, ?>> findGuides(GuideKind kind, String tid);

}
