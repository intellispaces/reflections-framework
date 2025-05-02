package tech.intellispaces.reflectionsj.engine.description;

import java.util.List;

import tech.intellispaces.actions.Action;
import tech.intellispaces.commons.type.Type;

public class ObjectFactoryMethodDescriptionImpl implements ObjectFactoryMethodDescription {
  private final Object objectFactory;
  private final String name;
  private final Type<?> returnedType;
  private final Class<?> returnedDomainClass;
  private final List<Type<?>> paramTypes;
  private final Action action;

  public ObjectFactoryMethodDescriptionImpl(
      Object objectFactory,
      String name,
      Type<?> returnedType,
      Class<?> returnedDomainClass,
      List<Type<?>> paramTypes,
      Action action
  ) {
    this.objectFactory = objectFactory;
    this.name = name;
    this.returnedType = returnedType;
    this.returnedDomainClass = returnedDomainClass;
    this.paramTypes = paramTypes;
    this.action = action;
  }

  @Override
  public Object objectFactory() {
    return objectFactory;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public Type<?> returnedType() {
    return returnedType;
  }

  @Override
  public Class<?> returnedDomainClass() {
    return returnedDomainClass;
  }

  @Override
  public List<Type<?>> paramTypes() {
    return paramTypes;
  }

  @Override
  public Action action() {
    return action;
  }
}
