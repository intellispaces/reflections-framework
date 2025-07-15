package tech.intellispaces.reflections.framework.system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import tech.intellispaces.actions.Action;
import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.commons.type.ClassFunctions;
import tech.intellispaces.core.Rid;
import tech.intellispaces.core.Rids;
import tech.intellispaces.javareflection.customtype.CustomTypes;
import tech.intellispaces.javareflection.method.MethodStatement;
import tech.intellispaces.reflections.framework.annotation.Mapper;
import tech.intellispaces.reflections.framework.annotation.MapperOfMoving;
import tech.intellispaces.reflections.framework.annotation.Mover;
import tech.intellispaces.reflections.framework.guide.GuideFunctions;
import tech.intellispaces.reflections.framework.guide.GuideKind;
import tech.intellispaces.reflections.framework.guide.GuideKinds;
import tech.intellispaces.reflections.framework.guide.GuideType;
import tech.intellispaces.reflections.framework.guide.SystemGuide;
import tech.intellispaces.reflections.framework.naming.NameConventionFunctions;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.space.channel.ChannelFunctions;

import static tech.intellispaces.reflections.framework.guide.GuideFunctions.getChannelId;

public class LocalGuideRegistry implements GuideRegistry {
  private final Map<Rid, List<SystemGuide<?, ?>>> mapperGuides = new HashMap<>();
  private final Map<Rid, List<SystemGuide<?, ?>>> moverGuides = new HashMap<>();
  private final Map<Rid, List<SystemGuide<?, ?>>> mapperOfMovingGuides = new HashMap<>();

  @Override
  public void addGuide(SystemGuide<?, ?> guide) {
    if (guide.kind().isMapper()) {
      mapperGuides.computeIfAbsent(guide.channelId(), k -> new ArrayList<>()).add(guide);
    } else if (guide.kind().isMover()) {
      moverGuides.computeIfAbsent(guide.channelId(), k -> new ArrayList<>()).add(guide);
    } else if (guide.kind().isMapperOfMoving()) {
      mapperOfMovingGuides.computeIfAbsent(guide.channelId(), k -> new ArrayList<>()).add(guide);
    }
  }

  @Override
  public List<SystemGuide<?, ?>> findGuides(
      Rid cid, GuideType kind, Class<?> sourceClass, ReflectionForm targetForm
  ) {
    List<SystemGuide<?, ?>> resultGuides = new ArrayList<>();
    List<SystemGuide<?, ?>> guides = findGuides(cid, kind);
    for (SystemGuide<?, ?> guide : guides) {
      if (isSuitableGuide(guide, sourceClass, targetForm)) {
        resultGuides.add(guide);
      }
    }
    return Collections.unmodifiableList(resultGuides);
  }

  @Override
  public <G> List<G> findGuides(Class<G> guideClass) {
    List<MethodStatement> guideMethods = guideMethods(guideClass);
    if (guideMethods.isEmpty()) {
      return List.of();
    }
    if (guideMethods.size() > 1) {
      throw NotImplementedExceptions.withCode("prDmpDuK");
    }

    MethodStatement guideMethod = guideMethods.get(0);
    Rid cid = getChannelId(guideMethod);
    GuideKind guideKind = GuideFunctions.getGuideKind(guideMethod);
    List<SystemGuide<?, ?>> guides = findGuides(cid, guideKind);
    return guides.stream()
        .map(g -> tryToCastGuide(g, guideClass))
        .filter(Objects::nonNull)
        .toList();
  }

  List<MethodStatement> guideMethods(Class<?> guideClass) {
    return CustomTypes.of(guideClass).declaredMethods().stream()
        .filter(GuideFunctions::isGuideMethod)
        .toList();
  }

  boolean isSuitableGuide(
      SystemGuide<?, ?> guide, Class<?> sourceClass, ReflectionForm targetForm
  ) {
    return ClassFunctions.isCompatibleClasses(guide.sourceClass(), sourceClass)
        && (guide.targetForm() == targetForm);
  }

  private List<SystemGuide<?, ?>> findGuides(Rid cid, GuideType guideType) {
    List<SystemGuide<?, ?>> guides = null;
    if (guideType.isMapper()) {
      guides = mapperGuides.get(cid);
    } else if (guideType.isMover()) {
      guides = moverGuides.get(cid);
    } else if (guideType.isMapperOfMoving()) {
      guides = mapperOfMovingGuides.get(cid);
    }
    if (guides == null) {
      return List.of();
    }
    return guides;
  }

  private List<SystemGuide<?, ?>> findGuides(Rid cid, GuideKind guideKind) {
    List<SystemGuide<?, ?>> guides = null;
    if (guideKind.isMapper()) {
      guides = mapperGuides.get(cid);
    } else if (guideKind.isMover()) {
      guides = moverGuides.get(cid);
    } else if (guideKind.isMapperOfMoving()) {
      guides = mapperOfMovingGuides.get(cid);
    }
    if (guides == null) {
      return List.of();
    }
    return guides;
  }

  @SuppressWarnings("unchecked")
  <G> @Nullable G tryToCastGuide(SystemGuide<?, ?> guide, Class<G> guideClass) {
    if (guide instanceof UnitGuide<?,?> unitGuide) {
      String actionGuideImplClassName = NameConventionFunctions.getActionGuideImplementationCanonicalName(
          guideClass.getCanonicalName()
      );
      Optional<Class<?>> actionGuideImplClass = ClassFunctions.getClass(actionGuideImplClassName);
      if (actionGuideImplClass.isEmpty()) {
        return null;
      }
      return (G) tech.intellispaces.commons.object.Objects.get(actionGuideImplClass.get(), Action.class, unitGuide.asAction());
    }
    return null;
  }
}
