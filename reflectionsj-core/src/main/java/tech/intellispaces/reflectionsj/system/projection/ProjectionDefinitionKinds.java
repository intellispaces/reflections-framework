package tech.intellispaces.reflectionsj.system.projection;

import tech.intellispaces.commons.abstraction.Enumeration;
import tech.intellispaces.reflectionsj.system.ProjectionDefinitionKind;

public enum ProjectionDefinitionKinds implements ProjectionDefinitionKind, Enumeration<ProjectionDefinitionKind> {

  ProjectionDefinitionBasedOnUnitMethod,

  ProjectionDefinitionBasedOnProviderClass
}
