package intellispaces.framework.core.system.kernel;

import intellispaces.framework.core.guide.Guide;
import intellispaces.framework.core.guide.GuideForm;
import intellispaces.framework.core.guide.GuideKind;

import java.util.List;

public interface GuideRegistry {

  <G> G getGuide(String name, Class<G> guideClass);

  <G> G getAutoGuide(Class<G> guideClass);

  List<Guide<?, ?>> findGuides(GuideKind kind, Class<?> objectHandleClass, String cid, GuideForm guideForm);

  void addGuideUnit(KernelUnit guideUnit);
}
