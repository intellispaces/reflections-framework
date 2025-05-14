package tech.intellispaces.reflections.framework.engine.description;

import java.util.List;

import tech.intellispaces.actions.Action;
import tech.intellispaces.actions.functional.FunctionActions;
import tech.intellispaces.commons.function.Function3;
import tech.intellispaces.reflections.framework.traverse.TraverseType;

public class ReflectionImplementationMethodBuilder3<W, P1, P2> {
  private final String name;
  private final Class<P1> paramClass1;
  private final Class<P2> paramClass2;
  private Action action;
  private ReflectionImplementationMethodPurpose purpose;

  private int traverseOrdinal;
  private Class<?> channelClass;
  private TraverseType traverseType;

  private String injectionKind;
  private int injectionOrdinal;
  private String injectionName;
  private Class<?> injectionType;

  public ReflectionImplementationMethodBuilder3(
      Class<W> reflectionWrapperClass, String name, Class<P1> paramClass1, Class<P2> paramClass2
  ) {
    this.name = name;
    this.paramClass1 = paramClass1;
    this.paramClass2 = paramClass2;
  }

  public ReflectionImplementationMethodBuilder3<W, P1, P2> purpose(ReflectionImplementationMethodPurpose purpose) {
    this.purpose = purpose;
    return this;
  }

  public <T> ReflectionImplementationMethodBuilder3<W, P1, P2> function(Function3<W, P1, P2, T> function) {
    this.action = FunctionActions.ofFunction3(function);
    return this;
  }

  public ReflectionImplementationMethodBuilder3<W, P1, P2> traverseOrdinal(int ordinal) {
    this.traverseOrdinal = ordinal;
    return this;
  }

  public ReflectionImplementationMethodBuilder3<W, P1, P2> channelClass(Class<?> channelClass) {
    this.channelClass = channelClass;
    return this;
  }

  public ReflectionImplementationMethodBuilder3<W, P1, P2> traverseType(TraverseType traverseType) {
    this.traverseType = traverseType;
    return this;
  }

  public ReflectionImplementationMethodBuilder3<W, P1, P2> injectionKind(String kind) {
    this.injectionKind = kind;
    return this;
  }

  public ReflectionImplementationMethodBuilder3<W, P1, P2> injectionOrdinal(int ordinal) {
    this.injectionOrdinal = ordinal;
    return this;
  }

  public ReflectionImplementationMethodBuilder3<W, P1, P2> injectionName(String injectionName) {
    this.injectionName = injectionName;
    return this;
  }

  public ReflectionImplementationMethodBuilder3<W, P1, P2> injectionType(Class<?> injectionType) {
    this.injectionType = injectionType;
    return this;
  }

  public ReflectionImplementationMethodDescription get() {
    return new ReflectionImplementationMethodDescriptionImpl(
        name,
        List.of(paramClass1, paramClass2),
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
