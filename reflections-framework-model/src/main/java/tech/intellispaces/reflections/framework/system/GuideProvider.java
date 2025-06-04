package tech.intellispaces.reflections.framework.system;

import java.util.List;

import tech.intellispaces.core.Rid;
import tech.intellispaces.reflections.framework.guide.Guide;
import tech.intellispaces.reflections.framework.guide.GuideKind;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;

/**
 * The guide provider.
 */
public interface GuideProvider {

  List<Guide<?, ?>> findGuides(
      Rid cid, GuideKind kind, Class<?> sourceClass, ReflectionForm targetForm
  );
}
