package intellispaces.framework.core.system.kernel;

import intellispaces.common.base.type.Type;
import intellispaces.common.javastatement.type.Types;
import intellispaces.framework.core.guide.GuideForm;
import intellispaces.framework.core.guide.GuideForms;
import intellispaces.framework.core.guide.n0.AutoMapper0;
import intellispaces.framework.core.guide.n0.AutoMapperOfMoving0;
import intellispaces.framework.core.guide.n0.AutoMover0;
import intellispaces.framework.core.guide.n0.Mapper0;
import intellispaces.framework.core.guide.n0.MapperOfMoving0;
import intellispaces.framework.core.guide.n0.Mover0;
import intellispaces.framework.core.guide.n1.AutoMapper1;
import intellispaces.framework.core.guide.n1.AutoMapperOfMoving1;
import intellispaces.framework.core.guide.n1.AutoMover1;
import intellispaces.framework.core.guide.n1.Mapper1;
import intellispaces.framework.core.guide.n1.MapperOfMoving1;
import intellispaces.framework.core.guide.n1.Mover1;
import intellispaces.framework.core.guide.n2.AutoMapper2;
import intellispaces.framework.core.guide.n2.AutoMapperOfMoving2;
import intellispaces.framework.core.guide.n2.AutoMover2;
import intellispaces.framework.core.guide.n2.Mapper2;
import intellispaces.framework.core.guide.n2.MapperOfMoving2;
import intellispaces.framework.core.guide.n2.Mover2;
import intellispaces.framework.core.guide.n3.AutoMapper3;
import intellispaces.framework.core.guide.n3.AutoMapperOfMoving3;
import intellispaces.framework.core.guide.n3.AutoMover3;
import intellispaces.framework.core.guide.n3.Mapper3;
import intellispaces.framework.core.guide.n3.MapperOfMoving3;
import intellispaces.framework.core.guide.n3.Mover3;
import intellispaces.framework.core.guide.n4.AutoMapperOfMoving4;
import intellispaces.framework.core.guide.n4.MapperOfMoving4;
import intellispaces.framework.core.object.ObjectFunctions;
import intellispaces.framework.core.space.channel.Channel0;
import intellispaces.framework.core.space.channel.Channel1;
import intellispaces.framework.core.space.channel.Channel2;
import intellispaces.framework.core.space.channel.Channel3;
import intellispaces.framework.core.space.channel.Channel4;
import intellispaces.framework.core.space.channel.ChannelFunctions;
import intellispaces.framework.core.space.channel.MappingChannel;
import intellispaces.framework.core.system.Module;
import intellispaces.framework.core.traverse.plan.DeclarativeTraversePlan;
import intellispaces.framework.core.traverse.plan.TraversePlan;

class ModuleImpl implements Module {
  private final KernelModule kernelModule;

  ModuleImpl(KernelModule kernelModule) {
    this.kernelModule = kernelModule;
  }

  @Override
  public void start() {
    kernelModule.start();
  }

  @Override
  public void start(String[] args) {
    kernelModule.start(args);
  }

  @Override
  public void stop() {
    kernelModule.stop();
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S, T> T mapThruChannel0(S source, String cid) {
    DeclarativeTraversePlan traversePlan = kernelModule.traverseAnalyzer().buildMapObjectHandleThruChannel0Plan(
        ObjectFunctions.defineObjectHandleClass(source.getClass()), cid, GuideForms.Main);
    return (T) traversePlan.execute(source, kernelModule.traverseExecutor());
  }

  @Override
  public <S, T, C extends Channel0 & MappingChannel> T mapThruChannel0(S source, Class<C> channelClass) {
    return mapThruChannel0(source, ChannelFunctions.getChannelId(channelClass));
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S, T, Q> T mapThruChannel1(S source, String cid, Q qualifier) {
    DeclarativeTraversePlan traversePlan = kernelModule.traverseAnalyzer().buildMapObjectHandleThruChannel1Plan(
        ObjectFunctions.defineObjectHandleClass(source.getClass()), cid, GuideForms.Main);
    return (T) traversePlan.execute(source, qualifier, kernelModule.traverseExecutor());
  }

  @Override
  public <S, T, Q, C extends Channel1 & MappingChannel> T mapThruChannel1(S source, Class<C> channelClass, Q qualifier) {
    return mapThruChannel1(source, ChannelFunctions.getChannelId(channelClass), qualifier);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S, R> R moveThruChannel0(S source, String cid) {
    TraversePlan traversePlan = kernelModule.traverseAnalyzer().buildMoveObjectHandleThruChannel0Plan(
        ObjectFunctions.defineObjectHandleClass(source.getClass()), cid, GuideForms.Main);
    return (R) traversePlan.execute(source, kernelModule.traverseExecutor());
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S, R, Q> R moveThruChannel1(S source, String cid, Q qualifier) {
    TraversePlan traversePlan = kernelModule.traverseAnalyzer().buildMoveObjectHandleThruChannel1Plan(
        ObjectFunctions.defineObjectHandleClass(source.getClass()), cid, GuideForms.Main);
    return (R) traversePlan.execute(source, qualifier, kernelModule.traverseExecutor());
  }

  @Override
  public <S, T> Mapper0<S, T> autoMapperThruChannel0(Type<S> sourceType, String cid, GuideForm guideForm) {
    TraversePlan traversePlan = kernelModule.traverseAnalyzer().buildMapObjectHandleThruChannel0Plan(
        sourceType.baseClass(), cid, guideForm
    );
    return new AutoMapper0<>(cid, traversePlan, guideForm, kernelModule.traverseExecutor());
  }

  @Override
  public <S, T, Q> Mapper1<S, T, Q> autoMapperThruChannel1(Type<S> sourceType, String cid, GuideForm guideForm) {
    TraversePlan traversePlan = kernelModule.traverseAnalyzer().buildMapObjectHandleThruChannel1Plan(
        sourceType.baseClass(), cid, guideForm
    );
    return new AutoMapper1<>(cid, traversePlan, guideForm, kernelModule.traverseExecutor());
  }

  @Override
  public <S, T, Q1, Q2> Mapper2<S, T, Q1, Q2> autoMapperThruChannel2(Type<S> sourceType, String cid, GuideForm guideForm) {
    TraversePlan traversePlan = kernelModule.traverseAnalyzer().buildMapObjectHandleThruChannel2Plan(
        sourceType.baseClass(), cid, guideForm
    );
    return new AutoMapper2<>(cid, traversePlan, guideForm, kernelModule.traverseExecutor());
  }

  @Override
  public <S, T, Q1, Q2, Q3> Mapper3<S, T, Q1, Q2, Q3> autoMapperThruChannel3(Type<S> sourceType, String cid, GuideForm guideForm) {
    TraversePlan traversePlan = kernelModule.traverseAnalyzer().buildMapObjectHandleThruChannel3Plan(
        sourceType.baseClass(), cid, guideForm
    );
    return new AutoMapper3<>(cid, traversePlan, guideForm, kernelModule.traverseExecutor());
  }

  @Override
  public <S> Mover0<S> autoMoverThruChannel0(Type<S> sourceType, String cid, GuideForm guideForm) {
    TraversePlan traversePlan = kernelModule.traverseAnalyzer().buildMoveObjectHandleThruChannel0Plan(
        sourceType.baseClass(), cid, guideForm
    );
    return new AutoMover0<>(cid, traversePlan, guideForm, kernelModule.traverseExecutor());
  }

  @Override
  public <S, Q> Mover1<S, Q> autoMoverThruChannel1(Class<S> sourceClass, String cid, GuideForm guideForm) {
    return autoMoverThruChannel1(Types.of(sourceClass), cid, guideForm);
  }

  @Override
  public <S, Q> Mover1<S, Q> autoMoverThruChannel1(Type<S> sourceType, String cid, GuideForm guideForm) {
    TraversePlan traversePlan = kernelModule.traverseAnalyzer().buildMoveObjectHandleThruChannel1Plan(
        sourceType.baseClass(), cid, guideForm
    );
    return new AutoMover1<>(cid, traversePlan, guideForm, kernelModule.traverseExecutor());
  }

  @Override
  public <S, Q1, Q2> Mover2<S, Q1, Q2> autoMoverThruChannel2(Type<S> sourceType, String cid, GuideForm guideForm) {
    TraversePlan traversePlan = kernelModule.traverseAnalyzer().buildMoveObjectHandleThruChannel2Plan(
        sourceType.baseClass(), cid, guideForm
    );
    return new AutoMover2<>(cid, traversePlan, guideForm, kernelModule.traverseExecutor());
  }

  @Override
  public <S, Q1, Q2, Q3> Mover3<S, Q1, Q2, Q3> autoMoverThruChannel3(Type<S> sourceType, String cid, GuideForm guideForm) {
    TraversePlan traversePlan = kernelModule.traverseAnalyzer().buildMoveObjectHandleThruChannel3Plan(
        sourceType.baseClass(), cid, guideForm
    );
    return new AutoMover3<>(cid, traversePlan, guideForm, kernelModule.traverseExecutor());
  }

  @Override
  public <S, T> MapperOfMoving0<S, T> autoMapperOfMovingThruChannel0(Type<S> sourceType, String cid, GuideForm guideForm) {
    TraversePlan traversePlan = kernelModule.traverseAnalyzer().buildMapOfMovingObjectHandleThruChannel0Plan(
        sourceType.baseClass(), cid, guideForm
    );
    return new AutoMapperOfMoving0<>(cid, traversePlan, guideForm, kernelModule.traverseExecutor());
  }

  @Override
  public <S, T, Q> MapperOfMoving1<S, T, Q> autoMapperOfMovingThruChannel1(Type<S> sourceType, String cid, GuideForm guideForm) {
    TraversePlan traversePlan = kernelModule.traverseAnalyzer().buildMapOfMovingObjectHandleThruChannel1Plan(
        sourceType.baseClass(), cid, guideForm
    );
    return new AutoMapperOfMoving1<>(cid, traversePlan, guideForm, kernelModule.traverseExecutor());
  }

  @Override
  public <S, T, Q1, Q2> MapperOfMoving2<S, T, Q1, Q2> autoMapperOfMovingThruChannel2(Type<S> sourceType, String cid, GuideForm guideForm) {
    TraversePlan traversePlan = kernelModule.traverseAnalyzer().buildMapOfMovingObjectHandleThruChannel2Plan(
        sourceType.baseClass(), cid, guideForm
    );
    return new AutoMapperOfMoving2<>(cid, traversePlan, guideForm, kernelModule.traverseExecutor());
  }

  @Override
  public <S, T, Q1, Q2, Q3> MapperOfMoving3<S, T, Q1, Q2, Q3> autoMapperOfMovingThruChannel3(
      Type<S> sourceType, String cid, GuideForm guideForm
  ) {
    TraversePlan traversePlan = kernelModule.traverseAnalyzer().buildMapOfMovingObjectHandleThruChannel3Plan(
        sourceType.baseClass(), cid, guideForm
    );
    return new AutoMapperOfMoving3<>(cid, traversePlan, guideForm, kernelModule.traverseExecutor());
  }

  @Override
  public <S, T, Q1, Q2, Q3, Q4> MapperOfMoving4<S, T, Q1, Q2, Q3, Q4> autoMapperOfMovingThruChannel4(
      Type<S> sourceType, String cid, GuideForm guideForm
  ) {
    TraversePlan traversePlan = kernelModule.traverseAnalyzer().buildMapOfMovingObjectHandleThruChannel4Plan(
        sourceType.baseClass(), cid, guideForm
    );
    return new AutoMapperOfMoving4<>(cid, traversePlan, guideForm, kernelModule.traverseExecutor());
  }

  @Override
  public <S, T> Mapper0<S, T> autoMapperThruChannel0(
      Type<S> sourceType, Class<? extends Channel0> channelClass, GuideForm guideForm
  ) {
    return autoMapperThruChannel0(sourceType, ChannelFunctions.getChannelId(channelClass), guideForm);
  }

  @Override
  public <S, T, Q> Mapper1<S, T, Q> autoMapperThruChannel1(
      Type<S> sourceType, Class<? extends Channel1> channelClass, GuideForm guideForm
  ) {
    return autoMapperThruChannel1(sourceType, ChannelFunctions.getChannelId(channelClass), guideForm);
  }

  @Override
  public <S, T, Q1, Q2> Mapper2<S, T, Q1, Q2> autoMapperThruChannel2(
      Type<S> sourceType, Class<? extends Channel2> channelClass, GuideForm guideForm
  ) {
    return autoMapperThruChannel2(sourceType, ChannelFunctions.getChannelId(channelClass), guideForm);
  }

  @Override
  public <S, T, Q1, Q2, Q3> Mapper3<S, T, Q1, Q2, Q3> autoMapperThruChannel3(
      Type<S> sourceType, Class<? extends Channel3> channelClass, GuideForm guideForm
  ) {
    return autoMapperThruChannel3(sourceType, ChannelFunctions.getChannelId(channelClass), guideForm);
  }

  @Override
  public <S> Mover0<S> autoMoverThruChannel0(
      Type<S> sourceType, Class<? extends Channel0> channelClass, GuideForm guideForm
  ) {
    return autoMoverThruChannel0(sourceType, ChannelFunctions.getChannelId(channelClass), guideForm);
  }

  @Override
  public <S, Q> Mover1<S, Q> autoMoverThruChannel1(
      Type<S> sourceType, Class<? extends Channel1> channelClass, GuideForm guideForm
  ) {
    return autoMoverThruChannel1(sourceType, ChannelFunctions.getChannelId(channelClass), guideForm);
  }

  @Override
  public <S, Q1, Q2> Mover2<S, Q1, Q2> autoMoverThruChannel2(
      Type<S> sourceType, Class<? extends Channel2> channelClass, GuideForm guideForm
  ) {
    return autoMoverThruChannel2(sourceType, ChannelFunctions.getChannelId(channelClass), guideForm);
  }

  @Override
  public <S, Q1, Q2, Q3> Mover3<S, Q1, Q2, Q3> autoMoverThruChannel3(
      Type<S> sourceType, Class<? extends Channel3> channelClass, GuideForm guideForm
  ) {
    return autoMoverThruChannel3(sourceType, ChannelFunctions.getChannelId(channelClass), guideForm);
  }

  @Override
  public <S, T> MapperOfMoving0<S, T> autoMapperOfMovingThruChannel0(
      Type<S> sourceType, Class<? extends Channel0> channelClass, GuideForm guideForm
  ) {
    return autoMapperOfMovingThruChannel0(sourceType, ChannelFunctions.getChannelId(channelClass), guideForm);
  }

  @Override
  public <S, T, Q> MapperOfMoving1<S, T, Q> autoMapperOfMovingThruChannel1(
      Type<S> sourceType, Class<? extends Channel1> channelClass, GuideForm guideForm
  ) {
    return autoMapperOfMovingThruChannel1(sourceType, ChannelFunctions.getChannelId(channelClass), guideForm);
  }

  @Override
  public <S, T, Q1, Q2> MapperOfMoving2<S, T, Q1, Q2> autoMapperOfMovingThruChannel2(
      Type<S> sourceType, Class<? extends Channel2> channelClass, GuideForm guideForm
  ) {
    return autoMapperOfMovingThruChannel2(sourceType, ChannelFunctions.getChannelId(channelClass), guideForm);
  }

  @Override
  public <S, T, Q1, Q2, Q3> MapperOfMoving3<S, T, Q1, Q2, Q3> autoMapperOfMovingThruChannel3(
      Type<S> sourceType, Class<? extends Channel3> channelClass, GuideForm guideForm
  ) {
    return autoMapperOfMovingThruChannel3(sourceType, ChannelFunctions.getChannelId(channelClass), guideForm);
  }

  @Override
  public <S, T, Q1, Q2, Q3, Q4> MapperOfMoving4<S, T, Q1, Q2, Q3, Q4> autoMapperOfMovingThruChannel4(
      Type<S> sourceType, Class<? extends Channel4> channelClass, GuideForm guideForm
  ) {
    return autoMapperOfMovingThruChannel4(sourceType, ChannelFunctions.getChannelId(channelClass), guideForm);
  }
}
