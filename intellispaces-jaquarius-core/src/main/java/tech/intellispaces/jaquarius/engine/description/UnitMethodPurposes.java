package tech.intellispaces.jaquarius.engine.description;

import tech.intellispaces.commons.entity.Enumeration;

public enum UnitMethodPurposes implements UnitMethodPurpose, Enumeration<UnitMethodPurpose> {

  StartupMethod,

  ShutdownMethod,

  InjectionMethod,

  ProjectionDefinition,

  Guide
}
