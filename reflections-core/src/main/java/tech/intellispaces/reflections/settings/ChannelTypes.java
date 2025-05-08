package tech.intellispaces.reflections.settings;

import tech.intellispaces.commons.abstraction.Enumeration;

public enum ChannelTypes implements Enumeration<ChannelType>, ChannelType {

  PointToDomain,

  YamlStringToPropertiesSet,

  PropertiesSetToValue,

  PropertiesSetToData;
}
