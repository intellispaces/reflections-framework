package tech.intellispaces.reflections.framework.settings;

import tech.intellispaces.core.Rid;

record ChannelReferenceImpl(
    ChannelType type,
    Rid channelId,
    String alias
) implements ChannelReference {
}
