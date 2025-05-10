package tech.intellispaces.reflectionsframework.settings;

record ChannelReferenceImpl(
    ChannelType type,
    String channelId,
    String alias
) implements ChannelReference {
}
