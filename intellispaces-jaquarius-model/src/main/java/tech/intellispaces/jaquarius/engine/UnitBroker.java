package tech.intellispaces.jaquarius.engine;

import tech.intellispaces.commons.action.Action;
import tech.intellispaces.jaquarius.system.Injection;

/**
 * The system unit broker.
 */
public interface UnitBroker {

  Action guideAction(int guideOrdinal);

  Injection injection(int ordinal);
}
