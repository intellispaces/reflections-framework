package tech.intellispaces.jaquarius.settings;

record KeyChannelImpl(
    KeyChannelPurpose purpose,
    String channelId,
    String alias
) implements KeyChannel {
}
