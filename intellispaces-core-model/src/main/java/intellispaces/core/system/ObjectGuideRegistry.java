package intellispaces.core.system;

import intellispaces.core.guide.Guide;
import intellispaces.core.guide.GuideKind;

/**
 * Attached to object handle guide register.
 */
public interface ObjectGuideRegistry {

  Guide<?, ?> getGuide(Class<?> objectHandleClass, GuideKind kind, String tid);
}
