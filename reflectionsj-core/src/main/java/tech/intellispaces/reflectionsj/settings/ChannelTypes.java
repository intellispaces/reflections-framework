package tech.intellispaces.reflectionsj.settings;

import tech.intellispaces.commons.abstraction.Enumeration;

public enum ChannelTypes implements Enumeration<ChannelType>, ChannelType {

  PointToDomain,

  YamlStringToPropertiesSet,

  PropertiesSetToValue,

  PropertiesSetToData;
}
