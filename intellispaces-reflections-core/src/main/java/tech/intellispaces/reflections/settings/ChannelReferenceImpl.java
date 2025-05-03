package tech.intellispaces.reflections.settings;

record ChannelReferenceImpl(
    ChannelType type,
    String channelId,
    String alias
) implements ChannelReference {
}
