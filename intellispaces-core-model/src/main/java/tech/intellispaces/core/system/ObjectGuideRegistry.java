package tech.intellispaces.core.system;

import tech.intellispaces.core.guide.Guide;
import tech.intellispaces.core.guide.GuideKind;

/**
 * Attached to object handle guide register.
 */
public interface ObjectGuideRegistry {

  Guide<?, ?> getGuide(Class<?> objectHandleClass, GuideKind kind, String tid);
}
