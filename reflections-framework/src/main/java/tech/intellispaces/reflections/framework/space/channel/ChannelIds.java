package tech.intellispaces.reflections.framework.space.channel;

import tech.intellispaces.core.Rid;
import tech.intellispaces.reflections.framework.channel.ChannelFunction0;
import tech.intellispaces.reflections.framework.channel.ChannelFunction1;

public interface ChannelIds {

  static <S, R> Rid get(Class<S> domain, ChannelFunction0<? super S, R> channelFunction) {
    return ChannelFunctions.getChannelId(domain, channelFunction);
  }

  static <S, R, Q> Rid get(
      Class<S> domain, ChannelFunction1<? super S, R, Q> channelFunction, Q qualifierAnyValidValue
  ) {
    return ChannelFunctions.getChannelId(domain, channelFunction, qualifierAnyValidValue);
  }
}
