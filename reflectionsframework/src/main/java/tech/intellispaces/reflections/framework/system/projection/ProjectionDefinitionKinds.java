package tech.intellispaces.reflections.framework.system.projection;

import tech.intellispaces.commons.abstraction.Enumeration;
import tech.intellispaces.reflections.framework.system.ProjectionDefinitionKind;

public enum ProjectionDefinitionKinds implements ProjectionDefinitionKind, Enumeration<ProjectionDefinitionKind> {

  ProjectionDefinitionBasedOnUnitMethod,

  ProjectionDefinitionBasedOnProviderClass
}
