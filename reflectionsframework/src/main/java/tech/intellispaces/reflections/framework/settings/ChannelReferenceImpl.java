package tech.intellispaces.reflections.framework.settings;

record ChannelReferenceImpl(
    ChannelType type,
    String channelId,
    String alias
) implements ChannelReference {
}
