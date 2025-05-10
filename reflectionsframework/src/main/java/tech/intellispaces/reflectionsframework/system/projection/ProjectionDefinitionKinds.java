package tech.intellispaces.reflectionsframework.system.projection;

import tech.intellispaces.commons.abstraction.Enumeration;
import tech.intellispaces.reflectionsframework.system.ProjectionDefinitionKind;

public enum ProjectionDefinitionKinds implements ProjectionDefinitionKind, Enumeration<ProjectionDefinitionKind> {

  ProjectionDefinitionBasedOnUnitMethod,

  ProjectionDefinitionBasedOnProviderClass
}
