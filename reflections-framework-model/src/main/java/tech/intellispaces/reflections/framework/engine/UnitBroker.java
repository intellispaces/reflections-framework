package tech.intellispaces.reflections.framework.engine;

import tech.intellispaces.actions.Action;
import tech.intellispaces.reflections.framework.system.Injection;

/**
 * The system unit broker.
 */
public interface UnitBroker {

  Action guideAction(int guideOrdinal);

  Injection injection(int ordinal);
}
