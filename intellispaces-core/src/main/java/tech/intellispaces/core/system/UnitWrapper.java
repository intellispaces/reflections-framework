package tech.intellispaces.core.system;

import tech.intellispaces.core.system.shadow.ShadowUnit;

public interface UnitWrapper {

  void $init(ShadowUnit shadowUnit);

  ShadowUnit $shadowUnit();
}
