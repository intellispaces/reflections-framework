package tech.intellispaces.core.guide;

import tech.intellispaces.commons.exception.UnexpectedViolationException;
import tech.intellispaces.commons.string.StringFunctions;
import tech.intellispaces.commons.type.TypeFunctions;
import tech.intellispaces.core.annotation.Mapper;
import tech.intellispaces.core.annotation.Mover;
import tech.intellispaces.core.annotation.Order;
import tech.intellispaces.core.annotation.Transition;
import tech.intellispaces.core.common.NameConventionFunctions;
import tech.intellispaces.core.guide.n0.AttachedMapper0;
import tech.intellispaces.core.guide.n0.AttachedMover0;
import tech.intellispaces.core.guide.n0.Mapper0;
import tech.intellispaces.core.guide.n0.MethodMapper0;
import tech.intellispaces.core.guide.n0.Mover0;
import tech.intellispaces.core.guide.n1.AttachedMapper1;
import tech.intellispaces.core.guide.n1.AttachedMover1;
import tech.intellispaces.core.guide.n1.Mapper1;
import tech.intellispaces.core.guide.n1.MethodMapper1;
import tech.intellispaces.core.guide.n1.Mover1;
import tech.intellispaces.core.guide.n2.AttachedMapper2;
import tech.intellispaces.core.guide.n2.AttachedMover2;
import tech.intellispaces.core.guide.n2.Mapper2;
import tech.intellispaces.core.guide.n2.Mover2;
import tech.intellispaces.core.guide.n3.Mapper3;
import tech.intellispaces.core.guide.n3.Mover3;
import tech.intellispaces.core.guide.n4.Mapper4;
import tech.intellispaces.core.guide.n4.Mover4;
import tech.intellispaces.core.guide.n5.Mapper5;
import tech.intellispaces.core.guide.n5.Mover5;
import tech.intellispaces.core.space.transition.TransitionFunctions;
import tech.intellispaces.core.traverse.TraverseTypes;
import tech.intellispaces.javastatements.customtype.CustomTypes;
import tech.intellispaces.javastatements.method.MethodFunctions;
import tech.intellispaces.javastatements.method.MethodStatement;
import tech.intellispaces.javastatements.method.Methods;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class GuideFunctions {

  private GuideFunctions() {}

  public static Transition getAttachedGuideTransitionAnnotation(Method objectHandleMethod) {
    return TransitionFunctions.getAttachedGuideTransitionAnnotation(objectHandleMethod);
  }

  public static String getUnitGuideTid(Object unitInstance, Method guideMethod) {
    return TransitionFunctions.getUnitGuideTid(unitInstance, guideMethod);
  }

  public static List<Guide<?, ?>> loadAttachedGuides(Class<?> objectHandleClass) {
    List<Guide<?, ?>> guides = new ArrayList<>();
    for (Method method : objectHandleClass.getDeclaredMethods()) {
      if (method.isAnnotationPresent(Mapper.class) || method.isAnnotationPresent(Mover.class)) {
        Transition transition = getAttachedGuideTransitionAnnotation(method);
        if (TraverseTypes.Mapping == TransitionFunctions.getTraverseType(transition)) {
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
      if (method.isAnnotationPresent(Mapper.class)) {
          guides.add(createMapper(unitInstance, method));
      } else if (method.isAnnotationPresent(Mover.class)) {
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

  @SuppressWarnings("unchecked, rawtypes")
  private static <S, T> Guide<S, T> createAttachedMapper(
      Class<S> objectHandleClass, String tid, Method guideMethod
  ) {
    int guideIndex = getAttachedGuideIndex(objectHandleClass, guideMethod);
    int qualifiersCount = guideMethod.getParameterCount();
    return switch (qualifiersCount) {
      case 0 -> new AttachedMapper0<>(tid, (Class) objectHandleClass, guideMethod, guideIndex);
      case 1 -> new AttachedMapper1<>(tid, (Class) objectHandleClass, guideMethod, guideIndex);
      case 2 -> new AttachedMapper2<>(tid, (Class) objectHandleClass, guideMethod, guideIndex);
      default -> throw UnexpectedViolationException.withMessage("Unsupported number of guide qualifiers: {}",
          qualifiersCount);
    };
  }

  @SuppressWarnings("unchecked, rawtypes")
  private static <S, T> Guide<S, T> createAttachedMover(
      Class<S> objectHandleClass, String tid, Method guideMethod
  ) {
    int guideIndex = getAttachedGuideIndex(objectHandleClass, guideMethod);
    int qualifiersCount = guideMethod.getParameterCount();
    return switch (qualifiersCount) {
      case 0 -> new AttachedMover0<>(tid, (Class) objectHandleClass, guideMethod, guideIndex);
      case 1 -> new AttachedMover1<>(tid, (Class) objectHandleClass, guideMethod, guideIndex);
      case 2 -> new AttachedMover2<>(tid, (Class) objectHandleClass, guideMethod, guideIndex);
      default -> throw UnexpectedViolationException.withMessage("Unsupported number of guide qualifiers: {}",
          qualifiersCount);
    };
  }

  private static int getAttachedGuideIndex(Class<?> objectHandleClass, Method guideMethod) {
    String implClassCanonicalName = NameConventionFunctions.getObjectHandleImplementationCanonicalName(
        objectHandleClass
    );
    Optional<Class<?>> objectHandleImplClass = TypeFunctions.getClass(implClassCanonicalName);
    if (objectHandleImplClass.isEmpty()) {
      throw UnexpectedViolationException.withMessage("Could not get object handle implementation class {}",
          implClassCanonicalName);
    }

    Optional<MethodStatement> objectHandleImplGuideMethod = MethodFunctions.getOverrideMethod(
        CustomTypes.of(objectHandleImplClass.get()),
        Methods.of(guideMethod)
    );
    if (objectHandleImplGuideMethod.isEmpty()) {
      throw UnexpectedViolationException.withMessage("Could not find override method in object handle implementation " +
          "class {}. Method {}", objectHandleImplClass.get(), guideMethod.getName());
    }
    Optional<Order> indexAnnotation = objectHandleImplGuideMethod.get().selectAnnotation(Order.class);
    if (indexAnnotation.isEmpty()) {
      throw UnexpectedViolationException.withMessage("Method {} does not contain annotation {}",
          guideMethod.getName(), Order.class.getCanonicalName());
    }
    return indexAnnotation.get().value();
  }

  public static String getActionGetterSupplierName(MethodStatement domainMethod) {
    var sb = new StringBuilder();
    sb.append("_get");
    sb.append(StringFunctions.capitalizeFirstLetter(
        NameConventionFunctions.joinMethodNameAndParameterTypes(domainMethod))
    );
    sb.append("ActionGetter");
    return sb.toString();
  }

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
