package intellispaces.framework.core.system.kernel;

import intellispaces.framework.core.guide.Guide;

import java.lang.reflect.Method;

public interface KernelUnitGuide<S, R> extends Guide<S, R> {

  Method guideMethod();

  int guideOrdinal();
}
