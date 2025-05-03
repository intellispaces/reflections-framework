package tech.intellispaces.reflections.system.projection;

import tech.intellispaces.commons.abstraction.Enumeration;
import tech.intellispaces.reflections.system.ProjectionDefinitionKind;

public enum ProjectionDefinitionKinds implements ProjectionDefinitionKind, Enumeration<ProjectionDefinitionKind> {

  ProjectionDefinitionBasedOnUnitMethod,

  ProjectionDefinitionBasedOnProviderClass
}
