package intellispaces.jaquarius.system.kernel;

import intellispaces.common.javastatement.method.MethodStatement;
import intellispaces.jaquarius.system.UnitGuide;

public interface KernelUnitGuide<S, R> extends UnitGuide<S, R> {

  MethodStatement guideMethod();

  int guideOrdinal();
}
