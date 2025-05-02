package tech.intellispaces.reflectionsj.settings;

record ChannelReferenceImpl(
    ChannelType type,
    String channelId,
    String alias
) implements ChannelReference {
}
