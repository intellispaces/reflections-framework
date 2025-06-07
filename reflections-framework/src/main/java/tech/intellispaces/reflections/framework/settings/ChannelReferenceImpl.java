package tech.intellispaces.reflections.framework.settings;

import tech.intellispaces.core.Rid;

record ChannelReferenceImpl(
    ChannelAssignment assignment,
    Rid channelId,
    String alias
) implements ChannelReference {
}
