package tech.intellispaces.jaquarius.engine;

import tech.intellispaces.jaquarius.engine.descriptor.ObjectHandleMethod;
import tech.intellispaces.jaquarius.engine.descriptor.ObjectHandleType;

import java.util.List;

class ObjectHandleTypeImpl implements ObjectHandleType {
  private final Class<?> objctHandleClass;
  private final List<ObjectHandleMethod> methods;

  ObjectHandleTypeImpl(Class<?> objctHandleClass, List<ObjectHandleMethod> methods) {
    this.objctHandleClass = objctHandleClass;
    this.methods = methods;
  }

  @Override
  public Class<?> objctHandleClass() {
    return objctHandleClass;
  }

  @Override
  public List<ObjectHandleMethod> methods() {
    return methods;
  }
}
