package tech.intellispaces.reflections.framework.settings;

import tech.intellispaces.commons.abstraction.Enumeration;

public enum ChannelAssignments implements Enumeration<ChannelAssignment>, ChannelAssignment {

  PointToDomain,

  YamlStringToPropertiesSet,

  PropertiesSetToValue,

  PropertiesSetToData;
}
