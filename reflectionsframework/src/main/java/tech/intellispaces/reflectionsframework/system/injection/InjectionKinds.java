package tech.intellispaces.reflectionsframework.system.injection;

import tech.intellispaces.commons.abstraction.Enumeration;
import tech.intellispaces.reflectionsframework.system.InjectionKind;

public enum InjectionKinds implements InjectionKind, Enumeration<InjectionKind> {

  Projection,

  SpecificGuide,

  AutoGuide
}
