package tech.intellispaces.reflections.framework.engine.description;

import java.util.List;
import java.util.function.BiFunction;

import tech.intellispaces.actions.Action;
import tech.intellispaces.actions.functional.FunctionActions;
import tech.intellispaces.reflections.framework.traverse.TraverseType;

public class ObjectHandleMethodBuilder2<R, P> {
  private final String name;
  private final Class<P> paramClass;
  private Action action;
  private ObjectHandleMethodPurpose purpose;

  private int traverseOrdinal;
  private Class<?> channelClass;
  private TraverseType traverseType;

  private String injectionKind;
  private int injectionOrdinal;
  private String injectionName;
  private Class<?> injectionType;

  public ObjectHandleMethodBuilder2(Class<R> objectHandleClass, String name, Class<P> paramClass) {
    this.name = name;
    this.paramClass = paramClass;
  }

  public ObjectHandleMethodBuilder2<R, P> purpose(ObjectHandleMethodPurpose purpose) {
    this.purpose = purpose;
    return this;
  }

  public <T> ObjectHandleMethodBuilder2<R, P> function(BiFunction<R, P, T> function) {
    this.action = FunctionActions.ofBiFunction(function);
    return this;
  }

  public ObjectHandleMethodBuilder2<R, P> traverseOrdinal(int ordinal) {
    this.traverseOrdinal = ordinal;
    return this;
  }

  public ObjectHandleMethodBuilder2<R, P> channelClass(Class<?> channelClass) {
    this.channelClass = channelClass;
    return this;
  }

  public ObjectHandleMethodBuilder2<R, P> traverseType(TraverseType traverseType) {
    this.traverseType = traverseType;
    return this;
  }

  public ObjectHandleMethodBuilder2<R, P> injectionKind(String kind) {
    this.injectionKind = kind;
    return this;
  }

  public ObjectHandleMethodBuilder2<R, P> injectionOrdinal(int ordinal) {
    this.injectionOrdinal = ordinal;
    return this;
  }

  public ObjectHandleMethodBuilder2<R, P> injectionName(String injectionName) {
    this.injectionName = injectionName;
    return this;
  }

  public ObjectHandleMethodBuilder2<R, P> injectionType(Class<?> injectionType) {
    this.injectionType = injectionType;
    return this;
  }

  public ObjectHandleMethodDescription get() {
    return new ObjectHandleMethodDescriptionImpl(
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
