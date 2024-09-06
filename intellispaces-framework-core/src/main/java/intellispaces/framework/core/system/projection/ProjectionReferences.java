package intellispaces.framework.core.system.projection;

import intellispaces.framework.core.system.ProjectionReference;

import java.util.List;

public interface ProjectionReferences {

  static ProjectionReference get(String name, Class<?> targetClass) {
    return new ProjectionReferenceImpl(name, targetClass);
  }

  static List<ProjectionReference> list(String name, Class<?> targetClass) {
    return List.of(get(name, targetClass));
  }

  static List<ProjectionReference> list(
      String name1, Class<?> targetClass1,
      String name2, Class<?> targetClass2
  ) {
    return List.of(
        get(name1, targetClass1),
        get(name2, targetClass2)
    );
  }

  static List<ProjectionReference> list(
      String name1, Class<?> targetClass1,
      String name2, Class<?> targetClass2,
      String name3, Class<?> targetClass3
  ) {
    return List.of(
        get(name1, targetClass1),
        get(name2, targetClass2),
        get(name3, targetClass3)
    );
  }

  static List<ProjectionReference> list(
      String name1, Class<?> targetClass1,
      String name2, Class<?> targetClass2,
      String name3, Class<?> targetClass3,
      String name4, Class<?> targetClass4
  ) {
    return List.of(
        get(name1, targetClass1),
        get(name2, targetClass2),
        get(name3, targetClass3),
        get(name4, targetClass4)
    );
  }

  static List<ProjectionReference> list(
      String name1, Class<?> targetClass1,
      String name2, Class<?> targetClass2,
      String name3, Class<?> targetClass3,
      String name4, Class<?> targetClass4,
      String name5, Class<?> targetClass5
  ) {
    return List.of(
        get(name1, targetClass1),
        get(name2, targetClass2),
        get(name3, targetClass3),
        get(name4, targetClass4),
        get(name5, targetClass5)
    );
  }

  static List<ProjectionReference> list(
      String name1, Class<?> targetClass1,
      String name2, Class<?> targetClass2,
      String name3, Class<?> targetClass3,
      String name4, Class<?> targetClass4,
      String name5, Class<?> targetClass5,
      String name6, Class<?> targetClass6
  ) {
    return List.of(
        get(name1, targetClass1),
        get(name2, targetClass2),
        get(name3, targetClass3),
        get(name4, targetClass4),
        get(name5, targetClass5),
        get(name6, targetClass6)
    );
  }

  static List<ProjectionReference> list(
      String name1, Class<?> targetClass1,
      String name2, Class<?> targetClass2,
      String name3, Class<?> targetClass3,
      String name4, Class<?> targetClass4,
      String name5, Class<?> targetClass5,
      String name6, Class<?> targetClass6,
      String name7, Class<?> targetClass7
  ) {
    return List.of(
        get(name1, targetClass1),
        get(name2, targetClass2),
        get(name3, targetClass3),
        get(name4, targetClass4),
        get(name5, targetClass5),
        get(name6, targetClass6),
        get(name7, targetClass7)
    );
  }

  static List<ProjectionReference> list(
      String name1, Class<?> targetClass1,
      String name2, Class<?> targetClass2,
      String name3, Class<?> targetClass3,
      String name4, Class<?> targetClass4,
      String name5, Class<?> targetClass5,
      String name6, Class<?> targetClass6,
      String name7, Class<?> targetClass7,
      String name8, Class<?> targetClass8
  ) {
    return List.of(
        get(name1, targetClass1),
        get(name2, targetClass2),
        get(name3, targetClass3),
        get(name4, targetClass4),
        get(name5, targetClass5),
        get(name6, targetClass6),
        get(name7, targetClass7),
        get(name8, targetClass8)
    );
  }

  static List<ProjectionReference> list(
      String name1, Class<?> targetClass1,
      String name2, Class<?> targetClass2,
      String name3, Class<?> targetClass3,
      String name4, Class<?> targetClass4,
      String name5, Class<?> targetClass5,
      String name6, Class<?> targetClass6,
      String name7, Class<?> targetClass7,
      String name8, Class<?> targetClass8,
      String name9, Class<?> targetClass9
  ) {
    return List.of(
        get(name1, targetClass1),
        get(name2, targetClass2),
        get(name3, targetClass3),
        get(name4, targetClass4),
        get(name5, targetClass5),
        get(name6, targetClass6),
        get(name7, targetClass7),
        get(name8, targetClass8),
        get(name9, targetClass9)
    );
  }

  static List<ProjectionReference> list(
      String name1, Class<?> targetClass1,
      String name2, Class<?> targetClass2,
      String name3, Class<?> targetClass3,
      String name4, Class<?> targetClass4,
      String name5, Class<?> targetClass5,
      String name6, Class<?> targetClass6,
      String name7, Class<?> targetClass7,
      String name8, Class<?> targetClass8,
      String name9, Class<?> targetClass9,
      String name10, Class<?> targetClass10
  ) {
    return List.of(
        get(name1, targetClass1),
        get(name2, targetClass2),
        get(name3, targetClass3),
        get(name4, targetClass4),
        get(name5, targetClass5),
        get(name6, targetClass6),
        get(name7, targetClass7),
        get(name8, targetClass8),
        get(name9, targetClass9),
        get(name10, targetClass10)
    );
  }
}
