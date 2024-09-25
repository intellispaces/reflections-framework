package intellispaces.framework.core.system.kernel;

import intellispaces.framework.core.system.UnitGuide;

import java.lang.reflect.Method;

public interface KernelUnitGuide<S, R> extends UnitGuide<S, R> {

  Method guideMethod();

  int guideOrdinal();
}
