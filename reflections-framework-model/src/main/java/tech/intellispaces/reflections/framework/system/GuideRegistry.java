package tech.intellispaces.reflections.framework.system;

import java.util.List;

import tech.intellispaces.reflections.framework.guide.Guide;
import tech.intellispaces.reflections.framework.guide.GuideKind;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;

/**
 * The guide registry.
 */
public interface GuideRegistry {

  void addGuide(Guide<?, ?> guide);

  List<Guide<?, ?>> findGuides(String cid, GuideKind kind);

  List<Guide<?, ?>> findGuides(
      String cid, GuideKind kind, Class<?> sourceReflectionClass, ReflectionForm targetReflectionForm
  );
}
