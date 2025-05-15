package tech.intellispaces.reflections.framework.engine;

import java.util.List;

import tech.intellispaces.reflections.framework.engine.description.FactoryMethod;

public interface ObjectFactoryWrapper {

  List<FactoryMethod> methods();
}
