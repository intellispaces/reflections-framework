package tech.intellispaces.jaquarius.settings;

import tech.intellispaces.commons.entity.Enumeration;

public enum KeyChannelPurposes implements Enumeration<KeyChannelPurpose>, KeyChannelPurpose {

  PointToDomain,

  YamlStringToProperties,

  PropertiesToValue,

  PropertiesToData;
}
