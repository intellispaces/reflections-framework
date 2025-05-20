package tech.intellispaces.reflections.framework.system;

import java.util.List;

import tech.intellispaces.reflections.framework.guide.Guide;
import tech.intellispaces.reflections.framework.guide.GuideKind;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;

/**
 * The guide provider.
 */
public interface GuideProvider {

  List<Guide<?, ?>> findGuides(
      String cid, GuideKind kind, Class<?> sourceReflectionClass, ReflectionForm targetForm
  );
}
