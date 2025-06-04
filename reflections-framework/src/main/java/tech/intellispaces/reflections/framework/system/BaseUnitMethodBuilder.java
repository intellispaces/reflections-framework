package tech.intellispaces.reflections.framework.system;

import java.util.List;

import tech.intellispaces.actions.Action;
import tech.intellispaces.core.Rid;
import tech.intellispaces.reflections.framework.guide.GuideKind;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;

class BaseUnitMethodBuilder<B extends BaseUnitMethodBuilder<B>> {
  private final String name;
  private final List<Class<?>> paramClasses;
  private String prototypeMethodName;
  protected Action action;
  private UnitMethodPurpose purpose;
  private String injectionName;
  private Class<?> injectionClass;
  private int injectionOrdinal;
  private InjectionKind injectionKind;
  private String projectionName;
  private Class<?> targetClass;
  private List<ProjectionReference> requiredProjections;
  private boolean lazyLoading;
  private int guideOrdinal;
  private GuideKind guideKind;
  private Rid guideCid;
  private ReflectionForm guideTargetForm;

  BaseUnitMethodBuilder(String name, List<Class<?>> paramClasses) {
    this.name = name;
    this.paramClasses = paramClasses;
  }

  @SuppressWarnings("unchecked")
  public B prototypeMethodName(String prototypeMethodName) {
    this.prototypeMethodName = prototypeMethodName;
    return (B) this;
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
  public B requiredProjections(List<ProjectionReference> projections) {
    this.requiredProjections = projections;
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

  @SuppressWarnings("unchecked")
  public B guideKind(GuideKind guideKind) {
    this.guideKind = guideKind;
    return (B) this;
  }

  @SuppressWarnings("unchecked")
  public B guideCid(Rid guideCid) {
    this.guideCid = guideCid;
    return (B) this;
  }

  @SuppressWarnings("unchecked")
  public B guideTargetForm(ReflectionForm guideTargetForm) {
    this.guideTargetForm = guideTargetForm;
    return (B) this;
  }

  public UnitMethod get() {
    return new UnitMethodImpl(
        name,
        paramClasses,
        prototypeMethodName,
        purpose,
        action,
        injectionName,
        injectionClass,
        injectionOrdinal,
        injectionKind,
        projectionName,
        targetClass,
        requiredProjections,
        lazyLoading,
        guideOrdinal,
        guideKind,
        guideCid,
        guideTargetForm
    );
  }
}
