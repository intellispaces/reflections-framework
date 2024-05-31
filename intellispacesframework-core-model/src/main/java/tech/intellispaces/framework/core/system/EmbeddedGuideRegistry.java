package tech.intellispaces.framework.core.system;

import tech.intellispaces.framework.core.guide.GuideKind;
import tech.intellispaces.framework.core.guide.Guide;

/**
 * Module guide register.
 */
public interface EmbeddedGuideRegistry {

  Guide<?, ?> getGuide(Class<?> objectHandleClass, GuideKind kind, String tid);

}
