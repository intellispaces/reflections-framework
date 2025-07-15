package tech.intellispaces.reflections.framework.system;

import java.util.List;

import tech.intellispaces.core.Rid;
import tech.intellispaces.reflections.framework.guide.GuideType;
import tech.intellispaces.reflections.framework.guide.SystemGuide;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;

/**
 * The guide provider.
 */
public interface GuideProvider {

  List<SystemGuide<?, ?>> findGuides(
      Rid cid, GuideType guideType, Class<?> sourceClass, ReflectionForm targetForm
  );

  <G> List<G> findGuides(Class<G> guideClass);
}
