package tech.intellispaces.jaquarius.engine.description;

import tech.intellispaces.commons.action.Action;
import tech.intellispaces.commons.action.functional.FunctionActions;
import tech.intellispaces.jaquarius.traverse.TraverseType;

import java.util.List;
import java.util.function.BiFunction;

public class ObjectHandleMethodBuilder2<H, P> {
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

  public ObjectHandleMethodBuilder2(Class<H> objectHandleClass, String name, Class<P> paramClass) {
    this.name = name;
    this.paramClass = paramClass;
  }

  public ObjectHandleMethodBuilder2<H, P> purpose(ObjectHandleMethodPurpose purpose) {
    this.purpose = purpose;
    return this;
  }

  public <R> ObjectHandleMethodBuilder2<H, P> function(BiFunction<H, P, R> function) {
    this.action = FunctionActions.ofBiFunction(function);
    return this;
  }

  public ObjectHandleMethodBuilder2<H, P> traverseOrdinal(int ordinal) {
    this.traverseOrdinal = ordinal;
    return this;
  }

  public ObjectHandleMethodBuilder2<H, P> channelClass(Class<?> channelClass) {
    this.channelClass = channelClass;
    return this;
  }

  public ObjectHandleMethodBuilder2<H, P> traverseType(TraverseType traverseType) {
    this.traverseType = traverseType;
    return this;
  }

  public ObjectHandleMethodBuilder2<H, P> injectionKind(String kind) {
    this.injectionKind = kind;
    return this;
  }

  public ObjectHandleMethodBuilder2<H, P> injectionOrdinal(int ordinal) {
    this.injectionOrdinal = ordinal;
    return this;
  }

  public ObjectHandleMethodBuilder2<H, P> injectionName(String injectionName) {
    this.injectionName = injectionName;
    return this;
  }

  public ObjectHandleMethodBuilder2<H, P> injectionType(Class<?> injectionType) {
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
