package tech.intellispaces.reflections.engine;

import java.util.List;

import tech.intellispaces.reflections.engine.description.ObjectFactoryMethodDescription;

public interface ObjectFactoryWrapper {

  List<ObjectFactoryMethodDescription> methods();
}
