package tech.intellispaces.reflections.system.injection;

import tech.intellispaces.commons.abstraction.Enumeration;
import tech.intellispaces.reflections.system.InjectionKind;

public enum InjectionKinds implements InjectionKind, Enumeration<InjectionKind> {

  Projection,

  SpecificGuide,

  AutoGuide
}
