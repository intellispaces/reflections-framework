package intellispaces.framework.core.guide.n0;

import intellispaces.framework.core.guide.GuideForm;
import intellispaces.framework.core.system.UnitWrapper;

import java.lang.reflect.Method;

public class UnitMapperOfMoving0<S, T> extends UnitGuide0<S, T> implements AbstractMapperOfMoving0<S, T> {

  public UnitMapperOfMoving0(String cid, UnitWrapper unitInstance, Method guideMethod, int guideOrdinal, GuideForm guideForm) {
    super(cid, unitInstance, guideMethod, guideOrdinal, guideForm);
  }
}
