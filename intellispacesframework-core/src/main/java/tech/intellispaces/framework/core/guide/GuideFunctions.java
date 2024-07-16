package tech.intellispaces.framework.core.guide;

import tech.intellispaces.framework.commons.exception.UnexpectedViolationException;
import tech.intellispaces.framework.commons.type.TypeFunctions;
import tech.intellispaces.framework.core.annotation.Transition;
import tech.intellispaces.framework.core.common.NameFunctions;
import tech.intellispaces.framework.core.guide.n0.AttachedMapper0;
import tech.intellispaces.framework.core.guide.n0.AttachedMover0;
import tech.intellispaces.framework.core.guide.n0.Mapper0;
import tech.intellispaces.framework.core.guide.n0.MethodMapper0;
import tech.intellispaces.framework.core.guide.n0.Mover0;
import tech.intellispaces.framework.core.guide.n1.AttachedMapper1;
import tech.intellispaces.framework.core.guide.n1.AttachedMover1;
import tech.intellispaces.framework.core.guide.n1.Mapper1;
import tech.intellispaces.framework.core.guide.n1.MethodMapper1;
import tech.intellispaces.framework.core.guide.n1.Mover1;
import tech.intellispaces.framework.core.guide.n2.Mapper2;
import tech.intellispaces.framework.core.guide.n2.Mover2;
import tech.intellispaces.framework.core.guide.n3.Mapper3;
import tech.intellispaces.framework.core.guide.n3.Mover3;
import tech.intellispaces.framework.core.guide.n4.Mapper4;
import tech.intellispaces.framework.core.guide.n4.Mover4;
import tech.intellispaces.framework.core.guide.n5.Mapper5;
import tech.intellispaces.framework.core.guide.n5.Mover5;
import tech.intellispaces.framework.core.space.transition.TransitionFunctions;
import tech.intellispaces.framework.core.traverse.TraverseTypes;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class GuideFunctions {

  public static Transition getAttachedGuideTransitionAnnotation(Method objectHandleMethod) {
    return TransitionFunctions.getAttachedGuideTransitionAnnotation(objectHandleMethod);
  }

  public static String getUnitGuideTid(Object unitInstance, Method guideMethod) {
    return TransitionFunctions.getUnitGuideTid(unitInstance, guideMethod);
  }

  public static List<Guide<?, ?>> loadAttachedGuides(Class<?> objectHandleClass) {
    List<Guide<?, ?>> guides = new ArrayList<>();
    for (Method method : objectHandleClass.getDeclaredMethods()) {
      if (
          method.isAnnotationPresent(tech.intellispaces.framework.core.annotation.Mapper.class) ||
              method.isAnnotationPresent(tech.intellispaces.framework.core.annotation.Mover.class)
      ) {
        Transition transition = getAttachedGuideTransitionAnnotation(method);
        if (TraverseTypes.Mapping == transition.type()) {
          guides.add(createAttachedMapper(objectHandleClass, transition.value(), method));
        } else {
          guides.add(createAttachedMover(objectHandleClass, transition.value(), method));
        }
      }
    }
    return guides;
  }

  public static List<Guide<?, ?>> loadUnitGuides(Class<?> unitClass, Object unitInstance) {
    List<Guide<?, ?>> guides = new ArrayList<>();
    for (Method method : unitClass.getDeclaredMethods()) {
      if (method.isAnnotationPresent(tech.intellispaces.framework.core.annotation.Mapper.class)) {
          guides.add(createMapper(unitInstance, method));
      } else if (method.isAnnotationPresent(tech.intellispaces.framework.core.annotation.Mover.class)) {
          guides.add(createMover(unitInstance, method));
      }
    }
    return guides;
  }

  public static GuideKind getGuideKind(Class<?> guideClass) {
    GuideKind guideKind = GUIDE_CLASS_TO_KIND.get(guideClass);
    if (guideKind == null) {
      throw UnexpectedViolationException.withMessage("Unsupported guide class: " + guideClass.getCanonicalName());
    }
    return guideKind;
  }

  private static Guide<?, ?> createMapper(Object unitInstance, Method guideMethod) {
    String tid = getUnitGuideTid(unitInstance, guideMethod);
    int qualifiersCount = guideMethod.getParameterCount();
    return switch (qualifiersCount) {
      case 1 -> new MethodMapper0<>(tid, unitInstance, guideMethod);
      case 2 -> new MethodMapper1<>(tid, unitInstance, guideMethod);
      default -> throw UnexpectedViolationException.withMessage("Unsupported number of guide qualifiers: {}",
          qualifiersCount);
    };
  }

  private static Guide<?, ?> createMover(Object unitInstance, Method guideMethod) {
    String tid = getUnitGuideTid(unitInstance, guideMethod);
    throw new UnsupportedOperationException("Not implemented");
  }

  private static Guide<?, ?> createAttachedMapper(Class<?> objectHandleClass, String tid, Method guideMethod) {
    int qualifiersCount = guideMethod.getParameterCount();
    Method actualGuideMethod = getActualGuideMethod(objectHandleClass, guideMethod);
    return switch (qualifiersCount) {
      case 0 -> new AttachedMapper0<>(tid, objectHandleClass, guideMethod, actualGuideMethod);
      case 1 -> new AttachedMapper1<>(tid, objectHandleClass, guideMethod, actualGuideMethod);
      default -> throw UnexpectedViolationException.withMessage("Unsupported number of guide qualifiers: {}",
          qualifiersCount);
    };
  }

  private static Guide<?, ?> createAttachedMover(Class<?> objectHandleClass, String tid, Method guideMethod) {
    int qualifiersCount = guideMethod.getParameterCount();
    Method actualGuideMethod = getActualGuideMethod(objectHandleClass, guideMethod);
    return switch (qualifiersCount) {
      case 0 -> new AttachedMover0<>(tid, objectHandleClass, guideMethod, actualGuideMethod);
      case 1 -> new AttachedMover1<>(tid, objectHandleClass, guideMethod, actualGuideMethod);
      default -> throw UnexpectedViolationException.withMessage("Unsupported number of guide qualifiers: {}",
          qualifiersCount);
    };
  }

  private static Method getActualGuideMethod(Class<?> objectHandleClass, Method guideMethod) {
    String implementationClassname = NameFunctions.getObjectHandleImplementationTypename(objectHandleClass);
    Optional<Class<?>> implementationClass = TypeFunctions.getClass(implementationClassname);
    if (implementationClass.isEmpty()) {
      throw UnexpectedViolationException.withMessage("Could not load object handle implementation class {}",
          implementationClassname);
    }

    String actualGuideMethodName = "_" + guideMethod.getName();
    Optional<Method> actualGuideMethod = TypeFunctions.getMethod(
        implementationClass.get(), actualGuideMethodName, guideMethod.getParameterTypes()
    );
    if (actualGuideMethod.isEmpty()) {
      throw UnexpectedViolationException.withMessage("Could not find actual guide method {} in class {}",
          actualGuideMethodName, guideMethod.getParameterTypes());
    }
    return actualGuideMethod.get();
  }

  private GuideFunctions() {}

  private static final Map<Class<?>, GuideKind> GUIDE_CLASS_TO_KIND = new HashMap<>();
  static {
    GUIDE_CLASS_TO_KIND.put(Mapper0.class, GuideKinds.Mapper0);
    GUIDE_CLASS_TO_KIND.put(Mapper1.class, GuideKinds.Mapper1);
    GUIDE_CLASS_TO_KIND.put(Mapper2.class, GuideKinds.Mapper2);
    GUIDE_CLASS_TO_KIND.put(Mapper3.class, GuideKinds.Mapper3);
    GUIDE_CLASS_TO_KIND.put(Mapper4.class, GuideKinds.Mapper4);
    GUIDE_CLASS_TO_KIND.put(Mapper5.class, GuideKinds.Mapper5);
    GUIDE_CLASS_TO_KIND.put(Mover0.class, GuideKinds.Mover0);
    GUIDE_CLASS_TO_KIND.put(Mover1.class, GuideKinds.Mover1);
    GUIDE_CLASS_TO_KIND.put(Mover2.class, GuideKinds.Mover2);
    GUIDE_CLASS_TO_KIND.put(Mover3.class, GuideKinds.Mover3);
    GUIDE_CLASS_TO_KIND.put(Mover4.class, GuideKinds.Mover4);
    GUIDE_CLASS_TO_KIND.put(Mover5.class, GuideKinds.Mover5);
  }
}
