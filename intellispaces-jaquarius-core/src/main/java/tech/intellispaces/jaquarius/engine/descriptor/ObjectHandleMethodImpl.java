package tech.intellispaces.jaquarius.engine.descriptor;

import tech.intellispaces.action.Action;
import tech.intellispaces.jaquarius.traverse.TraverseType;

import java.util.List;

class ObjectHandleMethodImpl implements ObjectHandleMethod {
  private final String name;
  private final List<Class<?>> paramClasses;
  private final String purpose;
  private final int traverseOrdinal;
  private final Action action;
  private final Class<?> channelClass;
  private final TraverseType traverseType;

  ObjectHandleMethodImpl(
      String name,
      List<Class<?>> paramClasses,
      String purpose,
      int traverseOrdinal,
      Action action,
      Class<?> channelClass,
      TraverseType traverseType
  ) {
    this.name = name;
    this.paramClasses = paramClasses;
    this.purpose = purpose;
    this.traverseOrdinal = traverseOrdinal;
    this.action = action;
    this.channelClass = channelClass;
    this.traverseType = traverseType;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public List<Class<?>> paramClasses() {
    return paramClasses;
  }

  @Override
  public String purpose() {
    return purpose;
  }

  @Override
  public int traverseOrdinal() {
    return traverseOrdinal;
  }

  @Override
  public Action action() {
    return action;
  }

  @Override
  public Class<?> channelClass() {
    return channelClass;
  }

  @Override
  public TraverseType traverseType() {
    return traverseType;
  }
}
