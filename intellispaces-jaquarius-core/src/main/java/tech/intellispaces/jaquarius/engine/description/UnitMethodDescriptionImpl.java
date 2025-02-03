package tech.intellispaces.jaquarius.engine.description;

import tech.intellispaces.commons.action.Action;
import tech.intellispaces.jaquarius.guide.GuideKind;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForm;
import tech.intellispaces.jaquarius.system.InjectionKind;
import tech.intellispaces.jaquarius.system.ProjectionReference;

import java.util.List;

class UnitMethodDescriptionImpl implements UnitMethodDescription {
  private final String name;
  private final List<Class<?>> paramClasses;
  private final String prototypeMethodName;
  private final UnitMethodPurpose purpose;
  private final Action action;
  private final String injectionName;
  private final Class<?> injectionClass;
  private final int injectionOrdinal;
  private final InjectionKind injectionKind;
  private final String projectionName;
  private final Class<?> targetClass;
  private final List<ProjectionReference> requiredProjections;
  private final boolean lazyLoading;
  private final int guideOrdinal;
  private final GuideKind guideKind;
  private final String guideCid;
  private final ObjectReferenceForm guideTargetForm;

  UnitMethodDescriptionImpl(
      String name,
      List<Class<?>> paramClasses,
      String prototypeMethodName,
      UnitMethodPurpose purpose,
      Action action,
      String injectionName,
      Class<?> injectionClass,
      int injectionOrdinal,
      InjectionKind injectionKind,
      String projectionName,
      Class<?> targetClass,
      List<ProjectionReference> requiredProjections,
      boolean lazyLoading,
      int guideOrdinal,
      GuideKind guideKind,
      String guideCid,
      ObjectReferenceForm guideTargetForm
  ) {
    this.name = name;
    this.paramClasses = paramClasses;
    this.prototypeMethodName = prototypeMethodName;
    this.purpose = purpose;
    this.action = action;

    this.injectionName = injectionName;
    this.injectionClass = injectionClass;
    this.injectionOrdinal = injectionOrdinal;
    this.injectionKind = injectionKind;

    this.projectionName = projectionName;
    this.targetClass = targetClass;
    this.requiredProjections = requiredProjections;
    this.lazyLoading = lazyLoading;

    this.guideOrdinal = guideOrdinal;
    this.guideKind = guideKind;
    this.guideCid = guideCid;
    this.guideTargetForm = guideTargetForm;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public List<Class<?>> paramClasses() {
    return paramClasses;
  }

  @Override
  public String prototypeMethodName() {
    return prototypeMethodName;
  }

  @Override
  public UnitMethodPurpose purpose() {
    return purpose;
  }

  @Override
  public Action action() {
    return action;
  }

  @Override
  public String injectionName() {
    return injectionName;
  }

  @Override
  public Class<?> injectionClass() {
    return injectionClass;
  }

  @Override
  public int injectionOrdinal() {
    return injectionOrdinal;
  }

  @Override
  public InjectionKind injectionKind() {
    return injectionKind;
  }

  @Override
  public String projectionName() {
    return projectionName;
  }

  @Override
  public Class<?> targetClass() {
    return targetClass;
  }

  @Override
  public List<ProjectionReference> requiredProjections() {
    return requiredProjections;
  }

  @Override
  public boolean lazyLoading() {
    return lazyLoading;
  }

  @Override
  public int guideOrdinal() {
    return guideOrdinal;
  }

  @Override
  public GuideKind guideKind() {
    return guideKind;
  }

  @Override
  public String guideCid() {
    return guideCid;
  }

  @Override
  public ObjectReferenceForm guideTargetForm() {
    return guideTargetForm;
  }
}
