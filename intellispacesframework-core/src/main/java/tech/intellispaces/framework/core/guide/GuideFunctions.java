package tech.intellispaces.framework.core.guide;

import tech.intellispaces.framework.core.annotation.Mapper;
import tech.intellispaces.framework.core.annotation.Mover;
import tech.intellispaces.framework.core.space.transition.TransitionFunctions;
import tech.intellispaces.framework.commons.exception.UnexpectedViolationException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public interface GuideFunctions {

  static String getEmbeddedMapperTid(Method mapperMethod) {
    tech.intellispaces.framework.core.annotation.Mapper mapper = mapperMethod.getAnnotation(tech.intellispaces.framework.core.annotation.Mapper.class);
    if (!mapper.value().isEmpty()) {
      return mapper.value();
    }
    return TransitionFunctions.getEmbeddedGuideTid(mapperMethod);
  }

  static String getEmbeddedMoverTid(Method moverMethod) {
    tech.intellispaces.framework.core.annotation.Mover mover = moverMethod.getAnnotation(tech.intellispaces.framework.core.annotation.Mover.class);
    if (!mover.value().isEmpty()) {
      return mover.value();
    }
    return TransitionFunctions.getEmbeddedGuideTid(moverMethod);
  }

  static String getUnitMapperTid(Method mapperMethod) {
    tech.intellispaces.framework.core.annotation.Mapper mapper = mapperMethod.getAnnotation(tech.intellispaces.framework.core.annotation.Mapper.class);
    if (!mapper.value().isEmpty()) {
      return mapper.value();
    }
    return TransitionFunctions.getUnitGuideTid(mapperMethod);
  }

  static String getUnitMoverTid(Method moverMethod) {
    tech.intellispaces.framework.core.annotation.Mover mover = moverMethod.getAnnotation(tech.intellispaces.framework.core.annotation.Mover.class);
    if (!mover.value().isEmpty()) {
      return mover.value();
    }
    return TransitionFunctions.getUnitGuideTid(moverMethod);
  }

  static List<Guide<?, ?>> loadEmbeddedGuides(Class<?> objectHandleClass) {
    List<Guide<?, ?>> guides = new ArrayList<>();
    for (Method method : objectHandleClass.getDeclaredMethods()) {
      tech.intellispaces.framework.core.annotation.Mapper mapper = method.getAnnotation(tech.intellispaces.framework.core.annotation.Mapper.class);
      if (mapper != null) {
        String tid = GuideFunctions.getEmbeddedMapperTid(method);
        Guide<?, ?> guide = createEmbeddedMapper(objectHandleClass, tid, method);
        guides.add(guide);
      }
      tech.intellispaces.framework.core.annotation.Mover mover = method.getAnnotation(tech.intellispaces.framework.core.annotation.Mover.class);
      if (mover != null) {
        String tid = GuideFunctions.getEmbeddedMoverTid(method);
        Guide<?, ?> guide = createEmbeddedMover(objectHandleClass, tid, method);
        guides.add(guide);
      }
    }
    return guides;
  }

  static List<Guide<?, ?>> loadUnitGuides(Class<?> unitClass, Object unitInstance) {
    List<Guide<?, ?>> guides = new ArrayList<>();
    for (Method method : unitClass.getDeclaredMethods()) {
      tech.intellispaces.framework.core.annotation.Mapper mapper = method.getAnnotation(Mapper.class);
      if (mapper != null) {
        String tid = GuideFunctions.getUnitMapperTid(method);
        Guide<?, ?> guide = createUnitMapper(tid, unitInstance, method);
        guides.add(guide);
      }
      tech.intellispaces.framework.core.annotation.Mover mover = method.getAnnotation(Mover.class);
      if (mover != null) {
        String tid = GuideFunctions.getUnitMoverTid(method);
        Guide<?, ?> guide = createUnitMover(tid, unitInstance, method);
        guides.add(guide);
      }
    }
    return guides;
  }

  private static Guide<?, ?> createUnitMapper(String tid, Object unitInstance, Method guideMethod) {
    int qualifiersCount = guideMethod.getParameterCount();
    return switch (qualifiersCount) {
      case 1 -> new UnitMapper0<>(tid, unitInstance, guideMethod);
      case 2 -> new UnitMapper1<>(tid, unitInstance, guideMethod);
      default -> throw UnexpectedViolationException.withMessage("Unsupported number of guide qualifiers - {}",
          qualifiersCount);
    };
  }

  private static Guide<?, ?> createUnitMover(String tid, Object unitInstance, Method guideMethod) {
    throw new UnsupportedOperationException("Not implemented");
  }

  private static Guide<?, ?> createEmbeddedMapper(Class<?> objectHandleClass, String tid, Method guideMethod) {
    int qualifiersCount = guideMethod.getParameterCount();
    return switch (qualifiersCount) {
      case 0 -> new EmbeddedMapper0<>(tid, objectHandleClass, guideMethod);
      case 1 -> new EmbeddedMapper1<>(tid, objectHandleClass, guideMethod);
      default -> throw UnexpectedViolationException.withMessage("Unsupported number of guide qualifiers - {}",
          qualifiersCount);
    };
  }

  private static Guide<?, ?> createEmbeddedMover(Class<?> objectHandleClass, String tid, Method guideMethod) {
    int qualifiersCount = guideMethod.getParameterCount();
    return switch (qualifiersCount) {
      case 0 -> new EmbeddedMover0<>(tid, objectHandleClass, guideMethod);
      case 1 -> new EmbeddedMover1<>(tid, objectHandleClass, guideMethod);
      default -> throw UnexpectedViolationException.withMessage("Unsupported number of guide qualifiers - {}",
          qualifiersCount);
    };
  }
}
