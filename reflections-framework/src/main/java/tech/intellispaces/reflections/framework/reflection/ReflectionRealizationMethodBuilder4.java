package tech.intellispaces.reflections.framework.reflection;

import java.util.List;

import tech.intellispaces.actions.Action;
import tech.intellispaces.actions.functional.FunctionActions;
import tech.intellispaces.commons.function.Function4;
import tech.intellispaces.reflections.framework.traverse.TraverseType;

public class ReflectionRealizationMethodBuilder4<W, P1, P2, P3> {
  private final String name;
  private final Class<P1> paramClass1;
  private final Class<P2> paramClass2;
  private final Class<P3> paramClass3;
  private Action action;
  private ReflectionRealizationMethodPurpose purpose;

  private int traverseOrdinal;
  private Class<?> channelClass;
  private TraverseType traverseType;

  private String injectionKind;
  private int injectionOrdinal;
  private String injectionName;
  private Class<?> injectionType;

  public ReflectionRealizationMethodBuilder4(
      Class<W> reflectionWrapperClass,
      String name,
      Class<P1> paramClass1,
      Class<P2> paramClass2,
      Class<P3> paramClass3
  ) {
    this.name = name;
    this.paramClass1 = paramClass1;
    this.paramClass2 = paramClass2;
    this.paramClass3 = paramClass3;
  }

  public ReflectionRealizationMethodBuilder4<W, P1, P2, P3> purpose(ReflectionRealizationMethodPurpose purpose) {
    this.purpose = purpose;
    return this;
  }

  public <T> ReflectionRealizationMethodBuilder4<W, P1, P2, P3> function(Function4<W, P1, P2, P3, T> function) {
    this.action = FunctionActions.ofFunction4(function);
    return this;
  }

  public ReflectionRealizationMethodBuilder4<W, P1, P2, P3> traverseOrdinal(int ordinal) {
    this.traverseOrdinal = ordinal;
    return this;
  }

  public ReflectionRealizationMethodBuilder4<W, P1, P2, P3> channelClass(Class<?> channelClass) {
    this.channelClass = channelClass;
    return this;
  }

  public ReflectionRealizationMethodBuilder4<W, P1, P2, P3> traverseType(TraverseType traverseType) {
    this.traverseType = traverseType;
    return this;
  }

  public ReflectionRealizationMethodBuilder4<W, P1, P2, P3> injectionKind(String kind) {
    this.injectionKind = kind;
    return this;
  }

  public ReflectionRealizationMethodBuilder4<W, P1, P2, P3> injectionOrdinal(int ordinal) {
    this.injectionOrdinal = ordinal;
    return this;
  }

  public ReflectionRealizationMethodBuilder4<W, P1, P2, P3> injectionName(String injectionName) {
    this.injectionName = injectionName;
    return this;
  }

  public ReflectionRealizationMethodBuilder4<W, P1, P2, P3> injectionType(Class<?> injectionType) {
    this.injectionType = injectionType;
    return this;
  }

  public ReflectionRealizationMethod get() {
    return new ReflectionRealizationMethodImpl(
        name,
        List.of(paramClass1, paramClass2, paramClass3),
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
