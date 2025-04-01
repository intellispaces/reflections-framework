package tech.intellispaces.jaquarius.engine.description;

import tech.intellispaces.commons.action.Action;
import tech.intellispaces.commons.action.functional.FunctionActions;
import tech.intellispaces.commons.function.Function5;
import tech.intellispaces.jaquarius.traverse.TraverseType;

import java.util.List;

public class ObjectHandleMethodBuilder5<H, P1, P2, P3, P4> {
  private final String name;
  private final Class<P1> paramClass1;
  private final Class<P2> paramClass2;
  private final Class<P3> paramClass3;
  private final Class<P4> paramClass4;
  private Action action;
  private ObjectHandleMethodPurpose purpose;

  private int traverseOrdinal;
  private Class<?> channelClass;
  private TraverseType traverseType;

  private String injectionKind;
  private int injectionOrdinal;
  private String injectionName;
  private Class<?> injectionType;

  public ObjectHandleMethodBuilder5(
      Class<H> objectHandleClass,
      String name,
      Class<P1> paramClass1,
      Class<P2> paramClass2,
      Class<P3> paramClass3,
      Class<P4> paramClass4
  ) {
    this.name = name;
    this.paramClass1 = paramClass1;
    this.paramClass2 = paramClass2;
    this.paramClass3 = paramClass3;
    this.paramClass4 = paramClass4;
  }

  public ObjectHandleMethodBuilder5<H, P1, P2, P3, P4> purpose(ObjectHandleMethodPurpose purpose) {
    this.purpose = purpose;
    return this;
  }

  public <R> ObjectHandleMethodBuilder5<H, P1, P2, P3, P4> function(Function5<H, P1, P2, P3, P4, R> function) {
    this.action = FunctionActions.ofFunction5(function);
    return this;
  }

  public ObjectHandleMethodBuilder5<H, P1, P2, P3, P4> traverseOrdinal(int ordinal) {
    this.traverseOrdinal = ordinal;
    return this;
  }

  public ObjectHandleMethodBuilder5<H, P1, P2, P3, P4> channelClass(Class<?> channelClass) {
    this.channelClass = channelClass;
    return this;
  }

  public ObjectHandleMethodBuilder5<H, P1, P2, P3, P4> traverseType(TraverseType traverseType) {
    this.traverseType = traverseType;
    return this;
  }

  public ObjectHandleMethodBuilder5<H, P1, P2, P3, P4> injectionKind(String kind) {
    this.injectionKind = kind;
    return this;
  }

  public ObjectHandleMethodBuilder5<H, P1, P2, P3, P4> injectionOrdinal(int ordinal) {
    this.injectionOrdinal = ordinal;
    return this;
  }

  public ObjectHandleMethodBuilder5<H, P1, P2, P3, P4> injectionName(String injectionName) {
    this.injectionName = injectionName;
    return this;
  }

  public ObjectHandleMethodBuilder5<H, P1, P2, P3, P4> injectionType(Class<?> injectionType) {
    this.injectionType = injectionType;
    return this;
  }

  public ObjectHandleMethodDescription get() {
    return new ObjectHandleMethodDescriptionImpl(
        name,
        List.of(paramClass1, paramClass2, paramClass3, paramClass4),
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
