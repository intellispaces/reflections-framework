package tech.intellispaces.reflections.framework.factory;

import java.util.List;

import tech.intellispaces.actions.Action;
import tech.intellispaces.commons.type.Type;
import tech.intellispaces.core.Domain;

public class FactoryMethodImpl implements FactoryMethod {
  private final Object factoryInstance;
  private final String contractType;
  private final Type<?> returnedType;
  private final Domain returnedDomain;
  private final List<String> contractQualifierNames;
  private final List<Type<?>> contractQualifierTypes;
  private final Action action;

  public FactoryMethodImpl(
      Object factoryInstance,
      String contractType,
      Type<?> returnedType,
      Domain returnedDomain,
      List<String> contractQualifierNames,
      List<Type<?>> contractQualifierTypes,
      Action action
  ) {
    this.factoryInstance = factoryInstance;
    this.contractType = contractType;
    this.returnedType = returnedType;
    this.returnedDomain = returnedDomain;
    this.contractQualifierNames = contractQualifierNames;
    this.contractQualifierTypes = contractQualifierTypes;
    this.action = action;
  }

  @Override
  public Object factoryInstance() {
    return factoryInstance;
  }

  @Override
  public String contractType() {
    return contractType;
  }

  @Override
  public Type<?> returnedType() {
    return returnedType;
  }

  @Override
  public Domain returnedDomain() {
    return returnedDomain;
  }

  @Override
  public List<String> contractQualifierNames() {
    return contractQualifierNames;
  }

  @Override
  public List<Type<?>> contractQualifierTypes() {
    return contractQualifierTypes;
  }

  @Override
  public Action action() {
    return action;
  }
}
