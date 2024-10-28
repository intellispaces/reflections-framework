package intellispaces.jaquarius.system.kernel;

import intellispaces.jaquarius.system.UnitGuide;

import java.lang.reflect.Method;

public interface KernelUnitGuide<S, R> extends UnitGuide<S, R> {

  Method guideMethod();

  int guideOrdinal();
}
