package tech.intellispaces.reflections.framework.reflection;

import java.util.List;
import java.util.function.Function;

import tech.intellispaces.actions.Action;
import tech.intellispaces.actions.functional.FunctionActions;
import tech.intellispaces.reflections.framework.traverse.TraverseType;

public class ReflectionRealizationMethodBuilder1<W> {
  private final String name;
  private Action action;
  private ReflectionRealizationMethodPurpose purpose;

  private int traverseOrdinal;
  private Class<?> channelClass;
  private TraverseType traverseType;

  private String injectionKind;
  private int injectionOrdinal;
  private String injectionName;
  private Class<?> injectionType;

  public ReflectionRealizationMethodBuilder1(Class<W> reflectionWrapperClass, String name) {
    this.name = name;
  }

  public ReflectionRealizationMethodBuilder1<W> purpose(ReflectionRealizationMethodPurpose purpose) {
    this.purpose = purpose;
    return this;
  }

  public <T> ReflectionRealizationMethodBuilder1<W> function(Function<W, T> function) {
    this.action = FunctionActions.ofFunction(function);
    return this;
  }

  public ReflectionRealizationMethodBuilder1<W> traverseOrdinal(int ordinal) {
    this.traverseOrdinal = ordinal;
    return this;
  }

  public ReflectionRealizationMethodBuilder1<W> channelClass(Class<?> channelClass) {
    this.channelClass = channelClass;
    return this;
  }

  public ReflectionRealizationMethodBuilder1<W> traverseType(TraverseType traverseType) {
    this.traverseType = traverseType;
    return this;
  }

  public ReflectionRealizationMethodBuilder1<W> injectionKind(String kind) {
    this.injectionKind = kind;
    return this;
  }

  public ReflectionRealizationMethodBuilder1<W> injectionOrdinal(int ordinal) {
    this.injectionOrdinal = ordinal;
    return this;
  }

  public ReflectionRealizationMethodBuilder1<W> injectionName(String injectionName) {
    this.injectionName = injectionName;
    return this;
  }

  public ReflectionRealizationMethodBuilder1<W> injectionType(Class<?> injectionType) {
    this.injectionType = injectionType;
    return this;
  }

  public ReflectionRealizationMethod get() {
    return new ReflectionRealizationMethodImpl(
        name,
        List.of(),
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
