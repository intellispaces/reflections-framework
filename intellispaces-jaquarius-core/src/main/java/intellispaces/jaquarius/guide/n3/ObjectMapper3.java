package intellispaces.jaquarius.guide.n3;

import intellispaces.common.javastatement.method.MethodStatement;
import intellispaces.jaquarius.guide.GuideForm;
import intellispaces.jaquarius.system.ObjectHandleWrapper;

/**
 * Attached to object handle mapper guide.<p/>
 *
 * Attached guide can be used exclusively with this object handle only.
 *
 * @param <S> source object type.
 * @param <Q1> first qualifier type.
 * @param <Q2> second qualifier type.
 * @param <Q3> third qualifier type.
 */
public class ObjectMapper3<S extends ObjectHandleWrapper, T, Q1, Q2, Q3>
    extends ObjectGuide3<S, T, Q1, Q2, Q3>
    implements AbstractMapper3<S, T, Q1, Q2, Q3>
{
  public ObjectMapper3(
      String cid,
      Class<S> objectHandleClass,
      MethodStatement guideMethod,
      int channelIndex,
      GuideForm guideForm
  ) {
    super(cid, objectHandleClass, guideMethod, channelIndex, guideForm);
  }
}
