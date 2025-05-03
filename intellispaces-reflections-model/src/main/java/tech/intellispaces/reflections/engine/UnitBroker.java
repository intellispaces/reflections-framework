package tech.intellispaces.reflections.engine;

import tech.intellispaces.actions.Action;
import tech.intellispaces.reflections.system.Injection;

/**
 * The system unit broker.
 */
public interface UnitBroker {

  Action guideAction(int guideOrdinal);

  Injection injection(int ordinal);
}
