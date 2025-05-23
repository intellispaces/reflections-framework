package tech.intellispaces.reflections.framework.settings;

import tech.intellispaces.commons.abstraction.Enumeration;

public enum ChannelTypes implements Enumeration<ChannelType>, ChannelType {

  PointToDomain,

  YamlStringToPropertiesSet,

  PropertiesSetToValue,

  PropertiesSetToData;
}
