package tech.intellispaces.reflectionsframework.engine;

import java.util.List;

import tech.intellispaces.reflectionsframework.engine.description.ObjectFactoryMethodDescription;

public interface ObjectFactoryWrapper {

  List<ObjectFactoryMethodDescription> methods();
}
