package tech.intellispaces.reflections.framework.reflection;

import tech.intellispaces.commons.exception.UnexpectedExceptions;

public interface Reflections {

  static Reflection<?> reflection(Object reflection) {
    if (reflection == null) {
      return null;
    }
    if (reflection instanceof Reflection<?>) {
      return (Reflection<?>) reflection;
    }
    throw UnexpectedExceptions.withMessage("Not a object reflection");
  }

  @SuppressWarnings("unchecked")
  static <R extends Reflection<?>> R reflection(Object reflection, Class<R> reflectionClass) {
    if (reflection == null) {
      return null;
    }
    if (reflectionClass.isAssignableFrom(reflection.getClass())) {
      return (R) reflection;
    }
    throw UnexpectedExceptions.withMessage("Not a object reflection");
  }

  @SuppressWarnings("unchecked")
  static <D> Reflection<D> reflectionOf(Object reflection, Class<D> domainClass) {
    if (reflection == null) {
      return null;
    }
    if (reflection instanceof Reflection<?>) {
      return (Reflection<D>) reflection;
    }
    throw UnexpectedExceptions.withMessage("Not a object reflection");
  }

  @SuppressWarnings("unchecked")
  static <D> MovableReflection<D> movableReflectionOf(Object reflection, Class<D> domainClass) {
    if (reflection == null) {
      return null;
    }
    if (reflection instanceof Reflection<?>) {
      return (MovableReflection<D>) reflection;
    }
    throw UnexpectedExceptions.withMessage("Not a movable object reflection");
  }
}
