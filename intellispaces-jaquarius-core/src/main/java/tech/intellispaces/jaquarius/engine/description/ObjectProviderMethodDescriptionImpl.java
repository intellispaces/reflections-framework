package tech.intellispaces.jaquarius.engine.description;

import tech.intellispaces.commons.action.Action;
import tech.intellispaces.commons.type.Type;

import java.util.List;

public class ObjectProviderMethodDescriptionImpl implements ObjectProviderMethodDescription {
  private final Object objectProvider;
  private final String name;
  private final Type<?> returnedType;
  private final Class<?> returnedDomainClass;
  private final List<Type<?>> paramTypes;
  private final Action action;

  public ObjectProviderMethodDescriptionImpl(
      Object objectProvider,
      String name,
      Type<?> returnedType,
      Class<?> returnedDomainClass,
      List<Type<?>> paramTypes,
      Action action
  ) {
    this.objectProvider = objectProvider;
    this.name = name;
    this.returnedType = returnedType;
    this.returnedDomainClass = returnedDomainClass;
    this.paramTypes = paramTypes;
    this.action = action;
  }

  @Override
  public Object objectProvider() {
    return objectProvider;
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
