package tech.intellispaces.reflectionsj.engine;

import tech.intellispaces.actions.Action;
import tech.intellispaces.reflectionsj.system.Injection;

/**
 * The system unit broker.
 */
public interface UnitBroker {

  Action guideAction(int guideOrdinal);

  Injection injection(int ordinal);
}
