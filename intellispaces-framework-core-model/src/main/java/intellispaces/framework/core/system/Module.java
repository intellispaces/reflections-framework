package intellispaces.framework.core.system;

import intellispaces.common.base.type.Type;
import intellispaces.framework.core.guide.GuideForm;
import intellispaces.framework.core.guide.n0.Mapper0;
import intellispaces.framework.core.guide.n0.MapperOfMoving0;
import intellispaces.framework.core.guide.n0.Mover0;
import intellispaces.framework.core.guide.n1.Mapper1;
import intellispaces.framework.core.guide.n1.MapperOfMoving1;
import intellispaces.framework.core.guide.n1.Mover1;
import intellispaces.framework.core.guide.n2.Mapper2;
import intellispaces.framework.core.guide.n2.MapperOfMoving2;
import intellispaces.framework.core.guide.n2.Mover2;
import intellispaces.framework.core.guide.n3.Mapper3;
import intellispaces.framework.core.guide.n3.MapperOfMoving3;
import intellispaces.framework.core.guide.n3.Mover3;
import intellispaces.framework.core.guide.n4.MapperOfMoving4;
import intellispaces.framework.core.space.channel.Channel0;
import intellispaces.framework.core.space.channel.Channel1;
import intellispaces.framework.core.space.channel.Channel2;
import intellispaces.framework.core.space.channel.Channel3;
import intellispaces.framework.core.space.channel.Channel4;
import intellispaces.framework.core.space.channel.MappingChannel;

/**
 * System module.
 */
public interface Module {

  void start();

  void start(String[] args);

  void stop();

  <S, T> T mapThruChannel0(S source, String cid);

  <S, T, C extends Channel0 & MappingChannel> T mapThruChannel0(S source, Class<C> channelClass);

  <S, T, Q> T mapThruChannel1(S source, String cid, Q qualifier);

  <S, T, Q, C extends Channel1 & MappingChannel> T mapThruChannel1(S source, Class<C> channelClass, Q qualifier);

  <S, R> R moveThruChannel0(S source, String cid);

  <S, R, Q> R moveThruChannel1(S source, String cid, Q qualifier);

  <S, T> Mapper0<S, T> autoMapperThruChannel0(Type<S> sourceType, String cid, GuideForm guideForm);

  <S, T, Q> Mapper1<S, T, Q> autoMapperThruChannel1(Type<S> sourceType, String cid, GuideForm guideForm);

  <S, T, Q1, Q2> Mapper2<S, T, Q1, Q2> autoMapperThruChannel2(Type<S> sourceType, String cid, GuideForm guideForm);

  <S, T, Q1, Q2, Q3> Mapper3<S, T, Q1, Q2, Q3> autoMapperThruChannel3(Type<S> sourceType, String cid, GuideForm guideForm);

  <S> Mover0<S> autoMoverThruChannel0(Type<S> sourceType, String cid, GuideForm guideForm);

  <S, Q> Mover1<S, Q> autoMoverThruChannel1(Class<S> sourceClass, String cid, GuideForm guideForm);

  <S, Q> Mover1<S, Q> autoMoverThruChannel1(Type<S> sourceType, String cid, GuideForm guideForm);

  <S, Q1, Q2> Mover2<S, Q1, Q2> autoMoverThruChannel2(Type<S> sourceType, String cid, GuideForm guideForm);

  <S, Q1, Q2, Q3> Mover3<S, Q1, Q2, Q3> autoMoverThruChannel3(Type<S> sourceType, String cid, GuideForm guideForm);

  <S, T> MapperOfMoving0<S, T> autoMapperOfMovingThruChannel0(Type<S> sourceType, String cid, GuideForm guideForm);

  <S, T, Q> MapperOfMoving1<S, T, Q> autoMapperOfMovingThruChannel1(Type<S> sourceType, String cid, GuideForm guideForm);

  <S, T, Q1, Q2> MapperOfMoving2<S, T, Q1, Q2> autoMapperOfMovingThruChannel2(Type<S> sourceType, String cid, GuideForm guideForm);

  <S, T, Q1, Q2, Q3> MapperOfMoving3<S, T, Q1, Q2, Q3> autoMapperOfMovingThruChannel3(Type<S> sourceType, String cid, GuideForm guideForm);

  <S, T, Q1, Q2, Q3, Q4> MapperOfMoving4<S, T, Q1, Q2, Q3, Q4> autoMapperOfMovingThruChannel4(Type<S> sourceType, String cid, GuideForm guideForm);

  <S, T> Mapper0<S, T> autoMapperThruChannel0(Type<S> sourceType, Class<? extends Channel0> channelClass, GuideForm guideForm);

  <S, T, Q> Mapper1<S, T, Q> autoMapperThruChannel1(Type<S> sourceType, Class<? extends Channel1> channelClass, GuideForm guideForm);

  <S, T, Q1, Q2> Mapper2<S, T, Q1, Q2> autoMapperThruChannel2(Type<S> sourceType, Class<? extends Channel2> channelClass, GuideForm guideForm);

  <S, T, Q1, Q2, Q3> Mapper3<S, T, Q1, Q2, Q3> autoMapperThruChannel3(Type<S> sourceType, Class<? extends Channel3> channelClass, GuideForm guideForm);

  <S> Mover0<S> autoMoverThruChannel0(Type<S> sourceType, Class<? extends Channel0> channelClass, GuideForm guideForm);

  <S, Q> Mover1<S, Q> autoMoverThruChannel1(Type<S> sourceType, Class<? extends Channel1> channelClass, GuideForm guideForm);

  <S, Q1, Q2> Mover2<S, Q1, Q2> autoMoverThruChannel2(Type<S> sourceType, Class<? extends Channel2> channelClass, GuideForm guideForm);

  <S, Q1, Q2, Q3> Mover3<S, Q1, Q2, Q3> autoMoverThruChannel3(Type<S> sourceType, Class<? extends Channel3> channelClass, GuideForm guideForm);

  <S, T> MapperOfMoving0<S, T> autoMapperOfMovingThruChannel0(Type<S> sourceType, Class<? extends Channel0> channelClass, GuideForm guideForm);

  <S, T, Q> MapperOfMoving1<S, T, Q> autoMapperOfMovingThruChannel1(Type<S> sourceType, Class<? extends Channel1> channelClass, GuideForm guideForm);

  <S, T, Q1, Q2> MapperOfMoving2<S, T, Q1, Q2> autoMapperOfMovingThruChannel2(Type<S> sourceType, Class<? extends Channel2> channelClass, GuideForm guideForm);

  <S, T, Q1, Q2, Q3> MapperOfMoving3<S, T, Q1, Q2, Q3> autoMapperOfMovingThruChannel3(Type<S> sourceType, Class<? extends Channel3> channelClass, GuideForm guideForm);

  <S, T, Q1, Q2, Q3, Q4> MapperOfMoving4<S, T, Q1, Q2, Q3, Q4> autoMapperOfMovingThruChannel4(Type<S> sourceType, Class<? extends Channel4> channelClass, GuideForm guideForm);
}
