package tech.intellispaces.reflections.framework.reflection;

import java.util.List;
import java.util.function.BiFunction;

import tech.intellispaces.actions.Action;
import tech.intellispaces.actions.functional.FunctionActions;
import tech.intellispaces.reflections.framework.traverse.TraverseType;

public class ReflectionRealizationMethodBuilder2<W, P> {
  private final String name;
  private final Class<P> paramClass;
  private Action action;
  private ReflectionRealizationMethodPurpose purpose;

  private int traverseOrdinal;
  private Class<?> channelClass;
  private TraverseType traverseType;

  private String injectionKind;
  private int injectionOrdinal;
  private String injectionName;
  private Class<?> injectionType;

  public ReflectionRealizationMethodBuilder2(Class<W> reflectionWrapperClass, String name, Class<P> paramClass) {
    this.name = name;
    this.paramClass = paramClass;
  }

  public ReflectionRealizationMethodBuilder2<W, P> purpose(ReflectionRealizationMethodPurpose purpose) {
    this.purpose = purpose;
    return this;
  }

  public <T> ReflectionRealizationMethodBuilder2<W, P> function(BiFunction<W, P, T> function) {
    this.action = FunctionActions.ofBiFunction(function);
    return this;
  }

  public ReflectionRealizationMethodBuilder2<W, P> traverseOrdinal(int ordinal) {
    this.traverseOrdinal = ordinal;
    return this;
  }

  public ReflectionRealizationMethodBuilder2<W, P> channelClass(Class<?> channelClass) {
    this.channelClass = channelClass;
    return this;
  }

  public ReflectionRealizationMethodBuilder2<W, P> traverseType(TraverseType traverseType) {
    this.traverseType = traverseType;
    return this;
  }

  public ReflectionRealizationMethodBuilder2<W, P> injectionKind(String kind) {
    this.injectionKind = kind;
    return this;
  }

  public ReflectionRealizationMethodBuilder2<W, P> injectionOrdinal(int ordinal) {
    this.injectionOrdinal = ordinal;
    return this;
  }

  public ReflectionRealizationMethodBuilder2<W, P> injectionName(String injectionName) {
    this.injectionName = injectionName;
    return this;
  }

  public ReflectionRealizationMethodBuilder2<W, P> injectionType(Class<?> injectionType) {
    this.injectionType = injectionType;
    return this;
  }

  public ReflectionRealizationMethod get() {
    return new ReflectionRealizationMethodImpl(
        name,
        List.of(paramClass),
        purpose,
        traverseOrdinal,
        action,
        channelClass,
        traverseType,
        injectionKind,
        injectionOrdinal,
        injectionName,
        injectionType
    );
  }
}
