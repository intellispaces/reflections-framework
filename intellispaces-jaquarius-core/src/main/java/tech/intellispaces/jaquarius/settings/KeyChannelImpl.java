package tech.intellispaces.jaquarius.settings;

record KeyChannelImpl(
    KeyChannelPurpose purpose,
    String cid,
    String alias
) implements KeyChannel {
}
