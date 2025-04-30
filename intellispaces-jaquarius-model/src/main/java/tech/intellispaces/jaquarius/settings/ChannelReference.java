package tech.intellispaces.jaquarius.settings;

/**
 * The channel reference description.
 */
public interface ChannelReference {

  /**
   * The type.
   */
  ChannelType type();

  /**
   * The channel CID.
   */
  String channelId();

  String alias();
}
