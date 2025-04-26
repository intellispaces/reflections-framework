package tech.intellispaces.jaquarius.settings;

record ChannelDescriptionImpl(
    ChannelType type,
    String channelId,
    String alias
) implements ChannelDescription {
}
