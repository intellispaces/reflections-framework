package tech.intellispaces.reflections.framework.factory;

import java.util.List;

import tech.intellispaces.actions.Action;
import tech.intellispaces.commons.type.Type;
import tech.intellispaces.core.ReflectionDomain;

public class FactoryMethodImpl implements FactoryMethod {
  private final Object factoryInstance;
  private final String contractType;
  private final Type<?> returnedType;
  private final ReflectionDomain outputDomain;
  private final List<String> contractQualifierNames;
  private final List<Type<?>> contractQualifierTypes;
  private final Action action;

  public FactoryMethodImpl(
      Object factoryInstance,
      String contractType,
      Type<?> returnedType,
      ReflectionDomain outputDomain,
      List<String> contractQualifierNames,
      List<Type<?>> contractQualifierTypes,
      Action action
  ) {
    this.factoryInstance = factoryInstance;
    this.contractType = contractType;
    this.returnedType = returnedType;
    this.outputDomain = outputDomain;
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
  public ReflectionDomain outputDomain() {
    return outputDomain;
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
