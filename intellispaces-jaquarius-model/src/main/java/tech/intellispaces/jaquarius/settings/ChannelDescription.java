package tech.intellispaces.jaquarius.settings;

/**
 * The channel description.
 */
public interface ChannelDescription {

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
