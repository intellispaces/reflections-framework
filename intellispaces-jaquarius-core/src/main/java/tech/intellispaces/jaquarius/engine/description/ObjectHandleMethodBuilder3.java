package tech.intellispaces.jaquarius.engine.description;

import tech.intellispaces.commons.action.Action;
import tech.intellispaces.commons.action.functional.FunctionActions;
import tech.intellispaces.commons.function.Function3;
import tech.intellispaces.jaquarius.traverse.TraverseType;

import java.util.List;

public class ObjectHandleMethodBuilder3<H, P1, P2> {
  private final String name;
  private final Class<P1> paramClass1;
  private final Class<P2> paramClass2;
  private Action action;
  private ObjectHandleMethodPurpose purpose;

  private int traverseOrdinal;
  private Class<?> channelClass;
  private TraverseType traverseType;

  private String injectionKind;
  private int injectionOrdinal;
  private String injectionName;
  private Class<?> injectionType;

  public ObjectHandleMethodBuilder3(
      Class<H> objectHandleClass, String name, Class<P1> paramClass1, Class<P2> paramClass2
  ) {
    this.name = name;
    this.paramClass1 = paramClass1;
    this.paramClass2 = paramClass2;
  }

  public ObjectHandleMethodBuilder3<H, P1, P2> purpose(ObjectHandleMethodPurpose purpose) {
    this.purpose = purpose;
    return this;
  }

  public <R> ObjectHandleMethodBuilder3<H, P1, P2> function(Function3<H, P1, P2, R> function) {
    this.action = FunctionActions.ofFunction3(function);
    return this;
  }

  public ObjectHandleMethodBuilder3<H, P1, P2> traverseOrdinal(int ordinal) {
    this.traverseOrdinal = ordinal;
    return this;
  }

  public ObjectHandleMethodBuilder3<H, P1, P2> channelClass(Class<?> channelClass) {
    this.channelClass = channelClass;
    return this;
  }

  public ObjectHandleMethodBuilder3<H, P1, P2> traverseType(TraverseType traverseType) {
    this.traverseType = traverseType;
    return this;
  }

  public ObjectHandleMethodBuilder3<H, P1, P2> injectionKind(String kind) {
    this.injectionKind = kind;
    return this;
  }

  public ObjectHandleMethodBuilder3<H, P1, P2> injectionOrdinal(int ordinal) {
    this.injectionOrdinal = ordinal;
    return this;
  }

  public ObjectHandleMethodBuilder3<H, P1, P2> injectionName(String injectionName) {
    this.injectionName = injectionName;
    return this;
  }

  public ObjectHandleMethodBuilder3<H, P1, P2> injectionType(Class<?> injectionType) {
    this.injectionType = injectionType;
    return this;
  }

  public ObjectHandleMethodDescription get() {
    return new ObjectHandleMethodDescriptionImpl(
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
