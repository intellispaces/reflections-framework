package tech.intellispaces.jaquarius.settings;

/**
 * The channel reference.
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
