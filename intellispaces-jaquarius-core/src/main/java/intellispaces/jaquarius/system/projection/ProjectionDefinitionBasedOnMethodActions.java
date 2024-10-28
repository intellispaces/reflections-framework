package intellispaces.jaquarius.system.projection;

import intellispaces.common.action.Action;

import java.util.List;

public interface ProjectionDefinitionBasedOnMethodActions {

  static ProjectionDefinitionBasedOnMethodAction get(
      Class<?> unitClass,
      String name,
      Class<?> type,
      boolean lazy,
      Action projectonMethodAction
  ) {
    return new ProjectionDefinitionBasedOnMethodAction(unitClass, name, type, lazy, projectonMethodAction, List.of());
  }

  static ProjectionDefinitionBasedOnMethodAction get(
      Class<?> unitClass,
      String name,
      Class<?> type,
      boolean lazy,
      Action projectonMethodAction,
      String requiredProjectionName,
      Class<?> requiredProjectionClass
  ) {
    return new ProjectionDefinitionBasedOnMethodAction(unitClass, name, type, lazy, projectonMethodAction, ProjectionReferences.list(
        requiredProjectionName, requiredProjectionClass
    ));
  }

  static ProjectionDefinitionBasedOnMethodAction get(
      Class<?> unitClass,
      String name,
      Class<?> type,
      boolean lazy,
      Action projectonMethodAction,
      String requiredProjectionName1,
      Class<?> requiredProjectionClass1,
      String requiredProjectionName2,
      Class<?> requiredProjectionClass2
  ) {
    return new ProjectionDefinitionBasedOnMethodAction(unitClass, name, type, lazy, projectonMethodAction, ProjectionReferences.list(
        requiredProjectionName1, requiredProjectionClass1,
        requiredProjectionName2, requiredProjectionClass2
    ));
  }

  static ProjectionDefinitionBasedOnMethodAction get(
      Class<?> unitClass,
      String name,
      Class<?> type,
      boolean lazy,
      Action projectonMethodAction,
      String requiredProjectionName1,
      Class<?> requiredProjectionClass1,
      String requiredProjectionName2,
      Class<?> requiredProjectionClass2,
      String requiredProjectionName3,
      Class<?> requiredProjectionClass3
  ) {
    return new ProjectionDefinitionBasedOnMethodAction(unitClass, name, type, lazy, projectonMethodAction, ProjectionReferences.list(
        requiredProjectionName1, requiredProjectionClass1,
        requiredProjectionName2, requiredProjectionClass2,
        requiredProjectionName3, requiredProjectionClass3
    ));
  }

  static ProjectionDefinitionBasedOnMethodAction get(
      Class<?> unitClass,
      String name,
      Class<?> type,
      boolean lazy,
      Action projectonMethodAction,
      String requiredProjectionName1,
      Class<?> requiredProjectionClass1,
      String requiredProjectionName2,
      Class<?> requiredProjectionClass2,
      String requiredProjectionName3,
      Class<?> requiredProjectionClass3,
      String requiredProjectionName4,
      Class<?> requiredProjectionClass4
  ) {
    return new ProjectionDefinitionBasedOnMethodAction(unitClass, name, type, lazy, projectonMethodAction, ProjectionReferences.list(
        requiredProjectionName1, requiredProjectionClass1,
        requiredProjectionName2, requiredProjectionClass2,
        requiredProjectionName3, requiredProjectionClass3,
        requiredProjectionName4, requiredProjectionClass4
    ));
  }

  static ProjectionDefinitionBasedOnMethodAction get(
      Class<?> unitClass,
      String name,
      Class<?> type,
      boolean lazy,
      Action projectonMethodAction,
      String requiredProjectionName1,
      Class<?> requiredProjectionClass1,
      String requiredProjectionName2,
      Class<?> requiredProjectionClass2,
      String requiredProjectionName3,
      Class<?> requiredProjectionClass3,
      String requiredProjectionName4,
      Class<?> requiredProjectionClass4,
      String requiredProjectionName5,
      Class<?> requiredProjectionClass5
  ) {
    return new ProjectionDefinitionBasedOnMethodAction(unitClass, name, type, lazy, projectonMethodAction, ProjectionReferences.list(
        requiredProjectionName1, requiredProjectionClass1,
        requiredProjectionName2, requiredProjectionClass2,
        requiredProjectionName3, requiredProjectionClass3,
        requiredProjectionName4, requiredProjectionClass4,
        requiredProjectionName5, requiredProjectionClass5
    ));
  }

  static ProjectionDefinitionBasedOnMethodAction get(
      Class<?> unitClass,
      String name,
      Class<?> type,
      boolean lazy,
      Action projectonMethodAction,
      String requiredProjectionName1,
      Class<?> requiredProjectionClass1,
      String requiredProjectionName2,
      Class<?> requiredProjectionClass2,
      String requiredProjectionName3,
      Class<?> requiredProjectionClass3,
      String requiredProjectionName4,
      Class<?> requiredProjectionClass4,
      String requiredProjectionName5,
      Class<?> requiredProjectionClass5,
      String requiredProjectionName6,
      Class<?> requiredProjectionClass6
  ) {
    return new ProjectionDefinitionBasedOnMethodAction(unitClass, name, type, lazy, projectonMethodAction, ProjectionReferences.list(
        requiredProjectionName1, requiredProjectionClass1,
        requiredProjectionName2, requiredProjectionClass2,
        requiredProjectionName3, requiredProjectionClass3,
        requiredProjectionName4, requiredProjectionClass4,
        requiredProjectionName5, requiredProjectionClass5,
        requiredProjectionName6, requiredProjectionClass6
    ));
  }

  static ProjectionDefinitionBasedOnMethodAction get(
      Class<?> unitClass,
      String name,
      Class<?> type,
      boolean lazy,
      Action projectonMethodAction,
      String requiredProjectionName1,
      Class<?> requiredProjectionClass1,
      String requiredProjectionName2,
      Class<?> requiredProjectionClass2,
      String requiredProjectionName3,
      Class<?> requiredProjectionClass3,
      String requiredProjectionName4,
      Class<?> requiredProjectionClass4,
      String requiredProjectionName5,
      Class<?> requiredProjectionClass5,
      String requiredProjectionName6,
      Class<?> requiredProjectionClass6,
      String requiredProjectionName7,
      Class<?> requiredProjectionClass7
  ) {
    return new ProjectionDefinitionBasedOnMethodAction(unitClass, name, type, lazy, projectonMethodAction, ProjectionReferences.list(
        requiredProjectionName1, requiredProjectionClass1,
        requiredProjectionName2, requiredProjectionClass2,
        requiredProjectionName3, requiredProjectionClass3,
        requiredProjectionName4, requiredProjectionClass4,
        requiredProjectionName5, requiredProjectionClass5,
        requiredProjectionName6, requiredProjectionClass6,
        requiredProjectionName7, requiredProjectionClass7
    ));
  }

  static ProjectionDefinitionBasedOnMethodAction get(
      Class<?> unitClass,
      String name,
      Class<?> type,
      boolean lazy,
      Action projectonMethodAction,
      String requiredProjectionName1,
      Class<?> requiredProjectionClass1,
      String requiredProjectionName2,
      Class<?> requiredProjectionClass2,
      String requiredProjectionName3,
      Class<?> requiredProjectionClass3,
      String requiredProjectionName4,
      Class<?> requiredProjectionClass4,
      String requiredProjectionName5,
      Class<?> requiredProjectionClass5,
      String requiredProjectionName6,
      Class<?> requiredProjectionClass6,
      String requiredProjectionName7,
      Class<?> requiredProjectionClass7,
      String requiredProjectionName8,
      Class<?> requiredProjectionClass8
  ) {
    return new ProjectionDefinitionBasedOnMethodAction(unitClass, name, type, lazy, projectonMethodAction, ProjectionReferences.list(
        requiredProjectionName1, requiredProjectionClass1,
        requiredProjectionName2, requiredProjectionClass2,
        requiredProjectionName3, requiredProjectionClass3,
        requiredProjectionName4, requiredProjectionClass4,
        requiredProjectionName5, requiredProjectionClass5,
        requiredProjectionName6, requiredProjectionClass6,
        requiredProjectionName7, requiredProjectionClass7,
        requiredProjectionName8, requiredProjectionClass8
    ));
  }

  static ProjectionDefinitionBasedOnMethodAction get(
      Class<?> unitClass,
      String name,
      Class<?> type,
      boolean lazy,
      Action projectonMethodAction,
      String requiredProjectionName1,
      Class<?> requiredProjectionClass1,
      String requiredProjectionName2,
      Class<?> requiredProjectionClass2,
      String requiredProjectionName3,
      Class<?> requiredProjectionClass3,
      String requiredProjectionName4,
      Class<?> requiredProjectionClass4,
      String requiredProjectionName5,
      Class<?> requiredProjectionClass5,
      String requiredProjectionName6,
      Class<?> requiredProjectionClass6,
      String requiredProjectionName7,
      Class<?> requiredProjectionClass7,
      String requiredProjectionName8,
      Class<?> requiredProjectionClass8,
      String requiredProjectionName9,
      Class<?> requiredProjectionClass9
  ) {
    return new ProjectionDefinitionBasedOnMethodAction(unitClass, name, type, lazy, projectonMethodAction, ProjectionReferences.list(
        requiredProjectionName1, requiredProjectionClass1,
        requiredProjectionName2, requiredProjectionClass2,
        requiredProjectionName3, requiredProjectionClass3,
        requiredProjectionName4, requiredProjectionClass4,
        requiredProjectionName5, requiredProjectionClass5,
        requiredProjectionName6, requiredProjectionClass6,
        requiredProjectionName7, requiredProjectionClass7,
        requiredProjectionName8, requiredProjectionClass8,
        requiredProjectionName9, requiredProjectionClass9
    ));
  }

  static ProjectionDefinitionBasedOnMethodAction get(
      Class<?> unitClass,
      String name,
      Class<?> type,
      boolean lazy,
      Action projectonMethodAction,
      String requiredProjectionName1,
      Class<?> requiredProjectionClass1,
      String requiredProjectionName2,
      Class<?> requiredProjectionClass2,
      String requiredProjectionName3,
      Class<?> requiredProjectionClass3,
      String requiredProjectionName4,
      Class<?> requiredProjectionClass4,
      String requiredProjectionName5,
      Class<?> requiredProjectionClass5,
      String requiredProjectionName6,
      Class<?> requiredProjectionClass6,
      String requiredProjectionName7,
      Class<?> requiredProjectionClass7,
      String requiredProjectionName8,
      Class<?> requiredProjectionClass8,
      String requiredProjectionName9,
      Class<?> requiredProjectionClass9,
      String requiredProjectionName10,
      Class<?> requiredProjectionClass10
  ) {
    return new ProjectionDefinitionBasedOnMethodAction(unitClass, name, type, lazy, projectonMethodAction, ProjectionReferences.list(
        requiredProjectionName1, requiredProjectionClass1,
        requiredProjectionName2, requiredProjectionClass2,
        requiredProjectionName3, requiredProjectionClass3,
        requiredProjectionName4, requiredProjectionClass4,
        requiredProjectionName5, requiredProjectionClass5,
        requiredProjectionName6, requiredProjectionClass6,
        requiredProjectionName7, requiredProjectionClass7,
        requiredProjectionName8, requiredProjectionClass8,
        requiredProjectionName9, requiredProjectionClass9,
        requiredProjectionName10, requiredProjectionClass10
    ));
  }
}
