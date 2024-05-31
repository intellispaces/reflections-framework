package tech.intellispacesframework.core.system;

import tech.intellispacesframework.core.guide.Guide;
import tech.intellispacesframework.core.guide.GuideKind;

/**
 * Module guide register.
 */
public interface EmbeddedGuideRegistry {

  Guide<?, ?> getGuide(Class<?> objectHandleClass, GuideKind kind, String tid);

}
