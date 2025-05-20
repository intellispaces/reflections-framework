package tech.intellispaces.reflections.framework.system;

import tech.intellispaces.reflections.framework.guide.Guide;

/**
 * The guide registry.
 */
public interface GuideRegistry extends GuideProvider {

  void addGuide(Guide<?, ?> guide);
}
