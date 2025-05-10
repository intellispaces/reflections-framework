package tech.intellispaces.reflections.framework.system.injection;

import tech.intellispaces.commons.abstraction.Enumeration;
import tech.intellispaces.reflections.framework.system.InjectionKind;

public enum InjectionKinds implements InjectionKind, Enumeration<InjectionKind> {

  Projection,

  SpecificGuide,

  AutoGuide
}
