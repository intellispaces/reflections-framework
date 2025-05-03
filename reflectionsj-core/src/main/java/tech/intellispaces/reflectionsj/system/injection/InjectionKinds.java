package tech.intellispaces.reflectionsj.system.injection;

import tech.intellispaces.commons.abstraction.Enumeration;
import tech.intellispaces.reflectionsj.system.InjectionKind;

public enum InjectionKinds implements InjectionKind, Enumeration<InjectionKind> {

  Projection,

  SpecificGuide,

  AutoGuide
}
