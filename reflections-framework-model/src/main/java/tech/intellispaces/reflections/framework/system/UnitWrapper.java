package tech.intellispaces.reflections.framework.system;

import tech.intellispaces.reflections.framework.engine.UnitBroker;

public interface UnitWrapper {

  UnitType unitType();

  UnitBroker $broker();
}
