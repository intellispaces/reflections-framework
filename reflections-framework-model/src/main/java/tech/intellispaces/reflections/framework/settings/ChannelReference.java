package tech.intellispaces.reflections.framework.settings;

import tech.intellispaces.core.Rid;

/**
 * The channel reference description.
 */
public interface ChannelReference {

  /**
   * The type.
   */
  ChannelType type();

  /**
   * The channel RID.
   */
  Rid channelId();

  String alias();
}
