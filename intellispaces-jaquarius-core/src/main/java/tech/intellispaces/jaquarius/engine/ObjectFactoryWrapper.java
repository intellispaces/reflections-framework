package tech.intellispaces.jaquarius.engine;

import java.util.List;

import tech.intellispaces.jaquarius.engine.description.ObjectFactoryMethodDescription;

public interface ObjectFactoryWrapper {

  List<ObjectFactoryMethodDescription> methods();
}
