package tech.intellispaces.jaquarius.settings;

record ChannelReferenceImpl(
    ChannelType type,
    String channelId,
    String alias
) implements ChannelReference {
}
