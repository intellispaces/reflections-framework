package intellispaces.jaquarius.system.kernel;

import intellispaces.jaquarius.system.Injection;
import tech.intellispaces.action.Action;

import java.util.List;

/**
 * Internal object handle.
 */
public interface InnerObjectHandle {

  Action getMethodAction(int index);

  Action getGuideAction(int index);

  void setMethodActions(Action... actions);

  void setGuideActions(Action... actions);

  Injection injection(int ordinal);

  List<Injection> injections();

  void setInjections(Injection... injections);

  <TD, TH> void addProjection(Class<TD> targetDomain, TH target);

  <TD, TH> TH mapTo(Class<TD> targetDomain);
}
