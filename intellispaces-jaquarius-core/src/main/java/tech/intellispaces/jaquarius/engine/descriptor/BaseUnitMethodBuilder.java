package tech.intellispaces.jaquarius.engine.descriptor;

import tech.intellispaces.action.Action;
import tech.intellispaces.jaquarius.system.InjectionKind;

import java.util.List;

class BaseUnitMethodBuilder<B extends BaseUnitMethodBuilder<B>> {
  private final String name;
  protected Action action;
  private UnitMethodPurpose purpose;
  private String injectionName;
  private Class<?> injectionClass;
  private int injectionOrdinal;
  private InjectionKind injectionKind;
  private String projectionName;
  private Class<?> targetClass;
  private boolean lazyLoading;
  private int guideOrdinal;

  BaseUnitMethodBuilder(String name) {
    this.name = name;
  }

  @SuppressWarnings("unchecked")
  public B purpose(UnitMethodPurpose purpose) {
    this.purpose = purpose;
    return (B) this;
  }

  @SuppressWarnings("unchecked")
  public B injectionName(String injectionName) {
    this.injectionName = injectionName;
    return (B) this;
  }

  @SuppressWarnings("unchecked")
  public B injectionClass(Class<?> injectionClass) {
    this.injectionClass = injectionClass;
    return (B) this;
  }

  @SuppressWarnings("unchecked")
  public B injectionOrdinal(int ordinal) {
    this.injectionOrdinal = ordinal;
    return (B) this;
  }

  @SuppressWarnings("unchecked")
  public B injectionKind(InjectionKind injectionKind) {
    this.injectionKind = injectionKind;
    return (B) this;
  }

  @SuppressWarnings("unchecked")
  public B projectionName(String projectionName) {
    this.projectionName = projectionName;
    return (B) this;
  }

  @SuppressWarnings("unchecked")
  public B targetClass(Class<?> projectionTargetClass) {
    this.targetClass = projectionTargetClass;
    return (B) this;
  }

  @SuppressWarnings("unchecked")
  public B lazyLoading(boolean lazy) {
    this.lazyLoading = lazy;
    return (B) this;
  }

  @SuppressWarnings("unchecked")
  public B guideOrdinal(int guideOrdinal) {
    this.guideOrdinal = guideOrdinal;
    return (B) this;
  }

  public UnitMethodDescriptor get() {
    return new UnitMethodDescriptorImpl(
        name,
        List.of(),
        purpose,
        action,
        injectionName,
        injectionClass,
        injectionOrdinal,
        injectionKind,
        projectionName,
        targetClass,
        lazyLoading,
        guideOrdinal
    );
  }
}
