package intellispaces.framework.core.guide;

import intellispaces.common.base.exception.UnexpectedViolationException;
import intellispaces.common.base.type.TypeFunctions;
import intellispaces.common.javastatement.customtype.CustomType;
import intellispaces.common.javastatement.customtype.CustomTypes;
import intellispaces.common.javastatement.instance.AnnotationInstance;
import intellispaces.common.javastatement.instance.ClassInstance;
import intellispaces.common.javastatement.instance.Instance;
import intellispaces.common.javastatement.method.MethodFunctions;
import intellispaces.common.javastatement.method.MethodStatement;
import intellispaces.common.javastatement.method.Methods;
import intellispaces.common.javastatement.reference.TypeReference;
import intellispaces.framework.core.annotation.Mapper;
import intellispaces.framework.core.annotation.Mover;
import intellispaces.framework.core.annotation.Ordinal;
import intellispaces.framework.core.annotation.Transition;
import intellispaces.framework.core.common.NameConventionFunctions;
import intellispaces.framework.core.guide.n0.Mapper0;
import intellispaces.framework.core.guide.n0.Mover0;
import intellispaces.framework.core.guide.n0.ObjectMapper0;
import intellispaces.framework.core.guide.n0.ObjectMover0;
import intellispaces.framework.core.guide.n0.UnitMapper0;
import intellispaces.framework.core.guide.n1.Mapper1;
import intellispaces.framework.core.guide.n1.Mover1;
import intellispaces.framework.core.guide.n1.ObjectMapper1;
import intellispaces.framework.core.guide.n1.ObjectMover1;
import intellispaces.framework.core.guide.n1.UnitMapper1;
import intellispaces.framework.core.guide.n2.Mapper2;
import intellispaces.framework.core.guide.n2.Mover2;
import intellispaces.framework.core.guide.n2.ObjectMapper2;
import intellispaces.framework.core.guide.n2.ObjectMover2;
import intellispaces.framework.core.guide.n3.Mapper3;
import intellispaces.framework.core.guide.n3.Mover3;
import intellispaces.framework.core.guide.n3.ObjectMapper3;
import intellispaces.framework.core.guide.n3.ObjectMover3;
import intellispaces.framework.core.guide.n4.Mapper4;
import intellispaces.framework.core.guide.n4.Mover4;
import intellispaces.framework.core.guide.n5.Mapper5;
import intellispaces.framework.core.guide.n5.Mover5;
import intellispaces.framework.core.space.transition.TransitionFunctions;
import intellispaces.framework.core.system.AttachedUnitGuide;
import intellispaces.framework.core.system.UnitWrapper;
import intellispaces.framework.core.system.guide.AttachedUnitGuides;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class GuideFunctions {

  private GuideFunctions() {}

  public static boolean isGuideType(TypeReference type) {
    return type.isCustomTypeReference() &&
        type.asCustomTypeReferenceOrElseThrow().targetType().hasAnnotation(intellispaces.framework.core.annotation.Guide.class);
  }

  public static boolean isGuideMethod(MethodStatement method) {
    return isMapperMethod(method) || isMoverMethod(method);
  }

  public static boolean isMoverMethod(MethodStatement method) {
    if (method.hasAnnotation(intellispaces.framework.core.annotation.Mover.class)) {
      return true;
    }
    return method.overrideMethods().stream()
        .anyMatch(m -> m.hasAnnotation(intellispaces.framework.core.annotation.Mover.class));
  }

  public static boolean isMapperMethod(MethodStatement method) {
    if (method.hasAnnotation(intellispaces.framework.core.annotation.Mapper.class)) {
      return true;
    }
    return method.overrideMethods().stream()
        .anyMatch(m -> m.hasAnnotation(intellispaces.framework.core.annotation.Mapper.class));
  }

  public static boolean isGuideMethod(Method method) {
    return isGuideMethod(Methods.of(method));
  }

  public static boolean isMoverMethod(Method method) {
    return isMoverMethod(Methods.of(method));
  }

  public static boolean isMapperMethod(Method method) {
    return isMapperMethod(Methods.of(method));
  }

  public static CustomType getTransitionType(MethodStatement guideMethod) {
    Optional<AnnotationInstance> mapper = guideMethod.selectAnnotation(Mapper.class.getCanonicalName());
    if (mapper.isPresent()) {
      Optional<Instance> value = mapper.get().elementValue("value");
      if (value.isPresent()) {
        return value.get().asClass().orElseThrow().type();
      }
    }

    Optional<AnnotationInstance> mover = guideMethod.selectAnnotation(Mover.class.getCanonicalName());
    if (mover.isPresent()) {
      return mover.get()
          .elementValue("value")
          .flatMap(Instance::asClass)
          .map(ClassInstance::type)
          .orElseThrow(() -> new RuntimeException("Not implemented"));
    }

    throw new RuntimeException("Not implemented");
  }

  public static Transition getObjectGuideTransitionAnnotation(Method objectHandleMethod) {
    return TransitionFunctions.getAttachedGuideTransitionAnnotation(objectHandleMethod);
  }

  public static String getUnitGuideTid(Object unitInstance, Method guideMethod) {
    return TransitionFunctions.getUnitGuideTid(unitInstance, guideMethod);
  }

  public static List<Guide<?, ?>> loadObjectGuides(Class<?> objectHandleClass) {
    List<Guide<?, ?>> guides = new ArrayList<>();
    for (Method method : objectHandleClass.getDeclaredMethods()) {
      if (isGuideMethod(method)) {
        Transition transition = getObjectGuideTransitionAnnotation(method);
        if (TransitionFunctions.getTraverseType(transition).isMovingBased()) {
          guides.add(createObjectMover(objectHandleClass, transition.value(), method));
        } else {
          guides.add(createObjectMapper(objectHandleClass, transition.value(), method));
        }
      }
    }
    return guides;
  }

  public static List<AttachedUnitGuide> readAttachedUnitGuides(
      Class<?> unitClass, UnitWrapper unitInstance
  ) {
    List<AttachedUnitGuide> guides = new ArrayList<>();
    for (Method method : unitClass.getDeclaredMethods()) {
      if (isMapperMethod(method)) {
        intellispaces.framework.core.guide.Mapper<?, ?> mapper = createMapper(unitInstance, method);
          guides.add(AttachedUnitGuides.get(mapper));
      } else if (isMoverMethod(method)) {
        intellispaces.framework.core.guide.Mover<?, ?> mover = createMover(unitInstance, method);
          guides.add(AttachedUnitGuides.get(mover));
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

  private static intellispaces.framework.core.guide.Mapper<?, ?> createMapper(
      UnitWrapper unitInstance, Method guideMethod
  ) {
    String tid = getUnitGuideTid(unitInstance, guideMethod);
    int guideIndex = getUnitGuideOrdinal(unitInstance, guideMethod);
    int qualifiersCount = guideMethod.getParameterCount();
    return switch (qualifiersCount) {
      case 1 -> new UnitMapper0<>(tid, unitInstance, guideMethod, guideIndex);
      case 2 -> new UnitMapper1<>(tid, unitInstance, guideMethod, guideIndex);
      default -> throw UnexpectedViolationException.withMessage("Unsupported number of guide qualifiers: {0}",
          qualifiersCount);
    };
  }

  private static intellispaces.framework.core.guide.Mover<?, ?> createMover(
      Object unitInstance, Method guideMethod
  ) {
    String tid = getUnitGuideTid(unitInstance, guideMethod);
    throw new UnsupportedOperationException("Not implemented");
  }

  @SuppressWarnings("unchecked, rawtypes")
  private static <S, T> Guide<S, T> createObjectMapper(
      Class<S> objectHandleClass, String tid, Method guideMethod
  ) {
    int transitionIndex = getTransitionOrdinal(objectHandleClass, guideMethod);
    int qualifiersCount = guideMethod.getParameterCount();
    return switch (qualifiersCount) {
      case 0 -> new ObjectMapper0<>(tid, (Class) objectHandleClass, guideMethod, transitionIndex);
      case 1 -> new ObjectMapper1<>(tid, (Class) objectHandleClass, guideMethod, transitionIndex);
      case 2 -> new ObjectMapper2<>(tid, (Class) objectHandleClass, guideMethod, transitionIndex);
      case 3 -> new ObjectMapper3<>(tid, (Class) objectHandleClass, guideMethod, transitionIndex);
      default -> throw UnexpectedViolationException.withMessage("Unsupported number of guide qualifiers: {0}",
          qualifiersCount);
    };
  }

  @SuppressWarnings("unchecked, rawtypes")
  private static <S, T> Guide<S, T> createObjectMover(
      Class<S> objectHandleClass, String tid, Method guideMethod
  ) {
    int transitionIndex = getTransitionOrdinal(objectHandleClass, guideMethod);
    int qualifiersCount = guideMethod.getParameterCount();
    return switch (qualifiersCount) {
      case 0 -> new ObjectMover0<>(tid, (Class) objectHandleClass, guideMethod, transitionIndex);
      case 1 -> new ObjectMover1<>(tid, (Class) objectHandleClass, guideMethod, transitionIndex);
      case 2 -> new ObjectMover2<>(tid, (Class) objectHandleClass, guideMethod, transitionIndex);
      case 3 -> new ObjectMover3<>(tid, (Class) objectHandleClass, guideMethod, transitionIndex);
      default -> throw UnexpectedViolationException.withMessage("Unsupported number of guide qualifiers: {0}",
          qualifiersCount);
    };
  }

  public static int getTransitionOrdinal(Class<?> objectHandleClass, Method guideMethod) {
    String implClassCanonicalName = NameConventionFunctions.getObjectHandleWrapperCanonicalName(
        objectHandleClass
    );
    Optional<Class<?>> objectHandleImplClass = TypeFunctions.getClass(implClassCanonicalName);
    if (objectHandleImplClass.isEmpty()) {
      throw UnexpectedViolationException.withMessage("Could not get object handle implementation class {0}",
          implClassCanonicalName);
    }

    Optional<MethodStatement> objectHandleImplGuideMethod = MethodFunctions.getOverrideMethod(
        CustomTypes.of(objectHandleImplClass.get()),
        Methods.of(guideMethod)
    );
    if (objectHandleImplGuideMethod.isEmpty()) {
      throw UnexpectedViolationException.withMessage("Could not find override method in object handle " +
          "implementation class {0}. Method {1}", objectHandleImplClass.get(), guideMethod.getName());
    }
    Optional<Ordinal> indexAnnotation = objectHandleImplGuideMethod.get().selectAnnotation(Ordinal.class);
    if (indexAnnotation.isEmpty()) {
      throw UnexpectedViolationException.withMessage("Method {0} does not contain annotation {1}",
          guideMethod.getName(), Ordinal.class.getCanonicalName());
    }
    return indexAnnotation.get().value();
  }

  private static int getUnitGuideOrdinal(UnitWrapper unitInstance, Method guideMethod) {
    Class<?> unitWrapperClass = unitInstance.getClass();
    Optional<MethodStatement> overrideGuideMethod = MethodFunctions.getOverrideMethod(
        CustomTypes.of(unitWrapperClass),
        Methods.of(guideMethod)
    );
    if (overrideGuideMethod.isEmpty()) {
      throw UnexpectedViolationException.withMessage("Could not find override method in unit wrapper " +
          "class {0}. Method {1}", unitWrapperClass, guideMethod.getName());
    }
    Optional<Ordinal> indexAnnotation = overrideGuideMethod.get().selectAnnotation(Ordinal.class);
    if (indexAnnotation.isEmpty()) {
      throw UnexpectedViolationException.withMessage("Method {0} does not contain annotation {1}",
          guideMethod.getName(), Ordinal.class.getCanonicalName());
    }
    return indexAnnotation.get().value();
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
