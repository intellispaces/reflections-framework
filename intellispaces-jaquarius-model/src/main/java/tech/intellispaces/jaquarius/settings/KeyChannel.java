package tech.intellispaces.jaquarius.settings;

/**
 * The key channel.
 */
public interface KeyChannel {

  /**
   * The key channel purpose.
   */
  KeyChannelPurpose purpose();

  /**
   * The key channel CID.
   */
  String cid();

  String alias();
}
