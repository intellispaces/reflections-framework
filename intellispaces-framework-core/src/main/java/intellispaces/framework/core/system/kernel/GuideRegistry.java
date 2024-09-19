package intellispaces.framework.core.system.kernel;

import intellispaces.framework.core.guide.Guide;
import intellispaces.framework.core.guide.GuideKind;

import java.util.List;

public interface GuideRegistry {

  <G> G getGuide(String name, Class<G> guideClass);

  List<Guide<?, ?>> findGuides(GuideKind kind, Class<?> objectHandleClass, String tid);

  void addGuideUnit(SystemUnit guideUnit);
}
