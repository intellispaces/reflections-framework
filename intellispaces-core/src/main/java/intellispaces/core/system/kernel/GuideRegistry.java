package intellispaces.core.system.kernel;

import intellispaces.core.guide.Guide;
import intellispaces.core.guide.GuideKind;

import java.util.List;

public interface GuideRegistry {

  <G> G getGuide(String name, Class<G> guideClass);

  List<Guide<?, ?>> findGuides(GuideKind kind, Class<?> objectHandleClass, String tid);

  void addGuideUnit(KernelUnit guideUnit);
}
