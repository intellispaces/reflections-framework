package tech.intellispaces.reflectionsj.engine;

import java.util.List;

import tech.intellispaces.reflectionsj.engine.description.ObjectFactoryMethodDescription;

public interface ObjectFactoryWrapper {

  List<ObjectFactoryMethodDescription> methods();
}
