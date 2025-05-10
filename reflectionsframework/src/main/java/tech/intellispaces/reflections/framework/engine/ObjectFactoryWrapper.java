package tech.intellispaces.reflections.framework.engine;

import java.util.List;

import tech.intellispaces.reflections.framework.engine.description.ObjectFactoryMethodDescription;

public interface ObjectFactoryWrapper {

  List<ObjectFactoryMethodDescription> methods();
}
