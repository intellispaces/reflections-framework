package tech.intellispaces.jaquarius.engine.description;

import tech.intellispaces.actions.Action;
import tech.intellispaces.actions.functional.FunctionActions;
import tech.intellispaces.jaquarius.traverse.TraverseType;

import java.util.List;
import java.util.function.Function;

public class ObjectHandleMethodBuilder1<H> {
  private final String name;
  private Action action;
  private ObjectHandleMethodPurpose purpose;

  private int traverseOrdinal;
  private Class<?> channelClass;
  private TraverseType traverseType;

  private String injectionKind;
  private int injectionOrdinal;
  private String injectionName;
  private Class<?> injectionType;

  public ObjectHandleMethodBuilder1(Class<H> objectHandleClass, String name) {
    this.name = name;
  }

  public ObjectHandleMethodBuilder1<H> purpose(ObjectHandleMethodPurpose purpose) {
    this.purpose = purpose;
    return this;
  }

  public <R> ObjectHandleMethodBuilder1<H> function(Function<H, R> function) {
    this.action = FunctionActions.ofFunction(function);
    return this;
  }

  public ObjectHandleMethodBuilder1<H> traverseOrdinal(int ordinal) {
    this.traverseOrdinal = ordinal;
    return this;
  }

  public ObjectHandleMethodBuilder1<H> channelClass(Class<?> channelClass) {
    this.channelClass = channelClass;
    return this;
  }

  public ObjectHandleMethodBuilder1<H> traverseType(TraverseType traverseType) {
    this.traverseType = traverseType;
    return this;
  }

  public ObjectHandleMethodBuilder1<H> injectionKind(String kind) {
    this.injectionKind = kind;
    return this;
  }

  public ObjectHandleMethodBuilder1<H> injectionOrdinal(int ordinal) {
    this.injectionOrdinal = ordinal;
    return this;
  }

  public ObjectHandleMethodBuilder1<H> injectionName(String injectionName) {
    this.injectionName = injectionName;
    return this;
  }

  public ObjectHandleMethodBuilder1<H> injectionType(Class<?> injectionType) {
    this.injectionType = injectionType;
    return this;
  }

  public ObjectHandleMethodDescription get() {
    return new ObjectHandleMethodDescriptionImpl(
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
