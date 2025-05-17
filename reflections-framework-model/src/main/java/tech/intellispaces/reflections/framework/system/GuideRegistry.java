package tech.intellispaces.reflections.framework.system;

import java.util.List;

import tech.intellispaces.reflections.framework.guide.Guide;
import tech.intellispaces.reflections.framework.guide.GuideKind;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;

/**
 * The guide registry.
 */
public interface GuideRegistry {

  <G> G getGuide(String name, Class<G> guideClass);

  List<Guide<?, ?>> findGuides(GuideKind kind, Class<?> sourceReflectionClass, String cid, ReflectionForm targetForm);
}
