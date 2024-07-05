package tech.intellispaces.framework.core.system;

import tech.intellispaces.framework.core.guide.Guide;
import tech.intellispaces.framework.core.guide.GuideKind;

/**
 * Attached guide register.
 */
public interface AttachedGuideRegistry {

  Guide<?, ?> getGuide(Class<?> objectHandleClass, GuideKind kind, String tid);

}
