package tech.intellispaces.reflections.framework.reflection;

import java.util.List;

import tech.intellispaces.actions.Action;
import tech.intellispaces.reflections.framework.traverse.TraverseType;

class ReflectionRealizationMethodImpl implements ReflectionRealizationMethod {
  private final String name;
  private final List<Class<?>> paramClasses;
  private final ReflectionRealizationMethodPurpose purpose;

  private final int traverseOrdinal;
  private final Action action;
  private final Class<?> channelClass;
  private final TraverseType traverseType;

  private final String injectionKind;
  private final int injectionOrdinal;
  private final String injectionName;
  private final Class<?> injectionType;

  ReflectionRealizationMethodImpl(
      String name,
      List<Class<?>> paramClasses,
      ReflectionRealizationMethodPurpose purpose,
      int traverseOrdinal,
      Action action,
      Class<?> channelClass,
      TraverseType traverseType,
      String injectionKind,
      int injectionOrdinal,
      String injectionName,
      Class<?> injectionType
  ) {
    this.name = name;
    this.paramClasses = paramClasses;
    this.purpose = purpose;
    this.traverseOrdinal = traverseOrdinal;
    this.action = action;
    this.channelClass = channelClass;
    this.traverseType = traverseType;
    this.injectionKind = injectionKind;
    this.injectionOrdinal = injectionOrdinal;
    this.injectionName = injectionName;
    this.injectionType = injectionType;
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
  public ReflectionRealizationMethodPurpose purpose() {
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

  @Override
  public String injectionKind() {
    return injectionKind;
  }

  @Override
  public int injectionOrdinal() {
    return injectionOrdinal;
  }

  @Override
  public String injectionName() {
    return injectionName;
  }

  @Override
  public Class<?> injectionType() {
    return injectionType;
  }
}
