package tech.intellispaces.reflectionsframework.engine;

import tech.intellispaces.actions.Action;
import tech.intellispaces.reflectionsframework.system.Injection;

/**
 * The system unit broker.
 */
public interface UnitBroker {

  Action guideAction(int guideOrdinal);

  Injection injection(int ordinal);
}
