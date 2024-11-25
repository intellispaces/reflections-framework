package tech.intellispaces.jaquarius.system.kernel;

import tech.intellispaces.jaquarius.system.UnitGuide;
import tech.intellispaces.java.reflection.method.MethodStatement;

public interface KernelUnitGuide<S, R> extends UnitGuide<S, R> {

  MethodStatement guideMethod();

  int guideOrdinal();
}
