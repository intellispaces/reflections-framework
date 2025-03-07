package tech.intellispaces.jaquarius.system.projection;

import tech.intellispaces.commons.entity.Enumeration;
import tech.intellispaces.jaquarius.system.ProjectionDefinitionKind;

public enum ProjectionDefinitionKinds implements ProjectionDefinitionKind, Enumeration<ProjectionDefinitionKind> {

  ProjectionDefinitionBasedOnUnitMethod,

  ProjectionDefinitionBasedOnProviderClass
}
