package tech.intellispaces.reflections.framework.system;

import tech.intellispaces.commons.abstraction.Enumeration;

public enum UnitMethodPurposes implements UnitMethodPurpose, Enumeration<UnitMethodPurpose> {

  StartupMethod,

  ShutdownMethod,

  InjectionMethod,

  ProjectionDefinition,

  Guide
}
