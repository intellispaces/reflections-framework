package tech.intellispaces.reflections.framework.reflection;

import tech.intellispaces.commons.exception.UnexpectedExceptions;

public interface Reflections {

  static SystemReflection reflection(Object reflection) {
    if (reflection == null) {
      return null;
    }
    if (reflection instanceof SystemReflection) {
      return (SystemReflection) reflection;
    }
    throw UnexpectedExceptions.withMessage("Not a object reflection");
  }

  @SuppressWarnings("unchecked")
  static <R extends SystemReflection> R reflection(Object reflection, Class<R> reflectionClass) {
    if (reflection == null) {
      return null;
    }
    if (reflectionClass.isAssignableFrom(reflection.getClass())) {
      return (R) reflection;
    }
    throw UnexpectedExceptions.withMessage("Not a object reflection");
  }

  static <D> SystemReflection reflectionOf(Object reflection, Class<D> domainClass) {
    if (reflection == null) {
      return null;
    }
    if (reflection instanceof SystemReflection) {
      return (SystemReflection) reflection;
    }
    throw UnexpectedExceptions.withMessage("Not a object reflection");
  }

  static <D> MovableReflection movableReflectionOf(Object reflection, Class<D> domainClass) {
    if (reflection == null) {
      return null;
    }
    if (reflection instanceof SystemReflection) {
      return (MovableReflection) reflection;
    }
    throw UnexpectedExceptions.withMessage("Not a movable object reflection");
  }
}
