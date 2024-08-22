package intellispaces.core.system;

import intellispaces.core.system.shadow.ShadowUnit;

public interface UnitWrapper {

  void $init(ShadowUnit shadowUnit);

  ShadowUnit $shadowUnit();
}
