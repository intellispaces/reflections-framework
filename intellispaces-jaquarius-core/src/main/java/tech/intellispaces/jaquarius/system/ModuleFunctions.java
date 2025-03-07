package tech.intellispaces.jaquarius.system;

import tech.intellispaces.commons.collection.ArraysFunctions;
import tech.intellispaces.commons.java.reflection.customtype.CustomType;
import tech.intellispaces.commons.java.reflection.instance.AnnotationInstance;
import tech.intellispaces.commons.java.reflection.instance.Instance;
import tech.intellispaces.jaquarius.annotation.Configuration;
import tech.intellispaces.jaquarius.annotation.Module;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface ModuleFunctions {

  static Iterable<Class<?>> getIncludedUnits(Class<?> moduleClass) {
    Set<Class<?>> unitClasses = new HashSet<>();
    ArraysFunctions.foreach(moduleClass.getAnnotation(Module.class).value(),
      u -> addUnitClass(u, unitClasses)
    );
    return unitClasses;
  }

  private static void addUnitClass(Class<?> unitClass, Set<Class<?>> unitClasses) {
    unitClasses.add(unitClass);
    Configuration configuration = unitClass.getAnnotation(Configuration.class);
    if (configuration != null) {
      ArraysFunctions.foreach(configuration.value(),
        u -> addUnitClass(u, unitClasses)
      );
    }
  }

  static Iterable<CustomType> getIncludedUnits(CustomType moduleType) {
    Map<String, CustomType> unitTypes = new HashMap<>();
    AnnotationInstance moduleAnnotation = moduleType.selectAnnotation(Module.class.getCanonicalName()).orElseThrow();
    Optional<Instance> unitsAttr = moduleAnnotation.value();
    if (unitsAttr.isPresent()) {
      List<Instance> units = unitsAttr.get().asArray().orElseThrow().elements();
      for (Instance unit : units) {
        CustomType unitType = unit.asClass().orElseThrow().type();
        addUnitType(unitType, unitTypes);
      }
    }
    return unitTypes.values();
  }

  private static void addUnitType(CustomType unitType, Map<String, CustomType> unitTypes) {
    if (!unitTypes.containsKey(unitType.className())) {
      unitTypes.put(unitType.className(), unitType);

      Optional<AnnotationInstance> configurationAnnotation = unitType.selectAnnotation(
        Configuration.class.getCanonicalName()
      );
      if (configurationAnnotation.isPresent()) {
        Optional<Instance> includeAttr = configurationAnnotation.get().value();
        if (includeAttr.isPresent()) {
          List<Instance> units = includeAttr.get().asArray().orElseThrow().elements();
          for (Instance unit : units) {
            CustomType includedUnitType = unit.asClass().orElseThrow().type();
            addUnitType(includedUnitType, unitTypes);
          }
        }
      }
    }
  }
}
