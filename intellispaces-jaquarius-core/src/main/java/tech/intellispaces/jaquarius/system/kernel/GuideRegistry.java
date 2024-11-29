package tech.intellispaces.jaquarius.system.kernel;

import tech.intellispaces.jaquarius.guide.Guide;
import tech.intellispaces.jaquarius.guide.GuideKind;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForm;

import java.util.List;

public interface GuideRegistry {

  <G> G getGuide(String name, Class<G> guideClass);

  <G> G getAutoGuide(Class<G> guideClass);

  List<Guide<?, ?>> findGuides(GuideKind kind, Class<?> objectHandleClass, String cid, ObjectReferenceForm targetForm);

  void addGuideUnit(KernelUnit guideUnit);
}
