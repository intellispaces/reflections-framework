package tech.intellispaces.jaquarius.engine.descriptor;

import tech.intellispaces.action.Action;
import tech.intellispaces.jaquarius.system.InjectionKind;
import tech.intellispaces.jaquarius.system.ProjectionReference;

import java.util.List;

class UnitMethodDescriptorImpl implements UnitMethodDescriptor {
  private final String name;
  private final List<Class<?>> paramClasses;
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

  UnitMethodDescriptorImpl(
      String name,
      List<Class<?>> paramClasses,
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
      int guideOrdinal
  ) {
    this.name = name;
    this.paramClasses = paramClasses;
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
}
