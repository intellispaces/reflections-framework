package tech.intellispaces.core.common;

import tech.intellispaces.commons.type.TypeFunctions;
import tech.intellispaces.javastatements.customtype.CustomType;
import tech.intellispaces.javastatements.reference.CustomTypeReference;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public interface AnnotationFunctions {

  static List<CustomType> findTopAnnotatedTypes(CustomType type, String annotationCanonicalName) {
    var result = new ArrayList<CustomType>();
    findTopAnnotatedTypesRecursively(type, annotationCanonicalName, result::add, new HashSet<>());
    return result;
  }

  private static void findTopAnnotatedTypesRecursively(
      CustomType type, String annotationCanonicalName, Consumer<CustomType> consumer, Set<String> history
  ) {
    for (CustomTypeReference parent : type.parentTypes()) {
      if (history.add(parent.targetType().canonicalName())) {
        if (parent.targetType().selectAnnotation(annotationCanonicalName).isPresent()) {
          consumer.accept(parent.targetType());
        } else {
          findTopAnnotatedTypesRecursively(parent.targetType(), annotationCanonicalName, consumer, history);
        }
      }
    }
  }

  static List<Class<?>> findTopAnnotatedClasses(Class<?> aClass, Class<? extends Annotation> annotation) {
    var result = new ArrayList<Class<?>>();
    findTopAnnotatedClassesRecursively(aClass, annotation, result::add, new HashSet<>());
    return result;
  }

  private static void findTopAnnotatedClassesRecursively(
      Class<?> aClass, Class<? extends Annotation> annotation, Consumer<Class<?>> consumer, Set<String> history
  ) {
    for (Class<?> parent : TypeFunctions.getParents(aClass)) {
      if (history.add(parent.getCanonicalName())) {
        if (parent.isAnnotationPresent(annotation)) {
          consumer.accept(parent);
        } else {
          findTopAnnotatedClassesRecursively(parent, annotation, consumer, history);
        }
      }
    }
  }
}
