package tech.intellispaces.reflections.framework.settings;

import tech.intellispaces.core.Rid;

/**
 * The channel reference point.
 */
public interface ChannelReference {

  /**
   * The channel assignment.
   */
  ChannelAssignment assignment();

  /**
   * The channel RID.
   */
  Rid channelId();

  String alias();
}
