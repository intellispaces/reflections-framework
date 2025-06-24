package tech.intellispaces.reflections.framework.system;

import tech.intellispaces.reflections.framework.guide.SystemGuide;

/**
 * The guide registry.
 */
public interface GuideRegistry extends GuideProvider {

  void addGuide(SystemGuide<?, ?> guide);
}
