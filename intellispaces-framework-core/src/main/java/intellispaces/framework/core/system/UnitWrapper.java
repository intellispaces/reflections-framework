package intellispaces.framework.core.system;

import intellispaces.framework.core.system.kernel.SystemUnit;

public interface UnitWrapper {

  void $init(SystemUnit unit);

  SystemUnit $unit();
}
