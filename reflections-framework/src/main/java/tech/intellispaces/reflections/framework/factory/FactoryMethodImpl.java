package tech.intellispaces.reflections.framework.factory;

import java.util.List;

import tech.intellispaces.actions.Action;
import tech.intellispaces.commons.type.Type;

public class FactoryMethodImpl implements FactoryMethod {
  private final Object factoryInstance;
  private final String name;
  private final Type<?> returnedType;
  private final Class<?> returnedDomainClass;
  private final List<Type<?>> paramTypes;
  private final Action action;

  public FactoryMethodImpl(
      Object factoryInstance,
      String name,
      Type<?> returnedType,
      Class<?> returnedDomainClass,
      List<Type<?>> paramTypes,
      Action action
  ) {
    this.factoryInstance = factoryInstance;
    this.name = name;
    this.returnedType = returnedType;
    this.returnedDomainClass = returnedDomainClass;
    this.paramTypes = paramTypes;
    this.action = action;
  }

  @Override
  public Object factoryInstance() {
    return factoryInstance;
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
