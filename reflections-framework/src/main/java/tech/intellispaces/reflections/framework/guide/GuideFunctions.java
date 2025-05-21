package tech.intellispaces.reflections.framework.guide;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.commons.type.ClassFunctions;
import tech.intellispaces.jstatements.customtype.Classes;
import tech.intellispaces.jstatements.customtype.CustomType;
import tech.intellispaces.jstatements.customtype.CustomTypes;
import tech.intellispaces.jstatements.customtype.Interfaces;
import tech.intellispaces.jstatements.instance.AnnotationInstance;
import tech.intellispaces.jstatements.instance.ClassInstance;
import tech.intellispaces.jstatements.instance.Instance;
import tech.intellispaces.jstatements.method.MethodFunctions;
import tech.intellispaces.jstatements.method.MethodStatement;
import tech.intellispaces.jstatements.method.Methods;
import tech.intellispaces.jstatements.reference.TypeReference;
import tech.intellispaces.reflections.framework.annotation.Channel;
import tech.intellispaces.reflections.framework.annotation.Mapper;
import tech.intellispaces.reflections.framework.annotation.MapperOfMoving;
import tech.intellispaces.reflections.framework.annotation.Mover;
import tech.intellispaces.reflections.framework.annotation.Ordinal;
import tech.intellispaces.reflections.framework.guide.n0.Mapper0;
import tech.intellispaces.reflections.framework.guide.n0.Mover0;
import tech.intellispaces.reflections.framework.guide.n0.ObjectMapper0;
import tech.intellispaces.reflections.framework.guide.n0.ObjectMapperOfMoving0;
import tech.intellispaces.reflections.framework.guide.n0.ObjectMover0;
import tech.intellispaces.reflections.framework.guide.n1.Mapper1;
import tech.intellispaces.reflections.framework.guide.n1.Mover1;
import tech.intellispaces.reflections.framework.guide.n1.ObjectMapper1;
import tech.intellispaces.reflections.framework.guide.n1.ObjectMapperOfMoving1;
import tech.intellispaces.reflections.framework.guide.n1.ObjectMover1;
import tech.intellispaces.reflections.framework.guide.n2.Mapper2;
import tech.intellispaces.reflections.framework.guide.n2.Mover2;
import tech.intellispaces.reflections.framework.guide.n2.ObjectMapper2;
import tech.intellispaces.reflections.framework.guide.n2.ObjectMapperOfMoving2;
import tech.intellispaces.reflections.framework.guide.n2.ObjectMover2;
import tech.intellispaces.reflections.framework.guide.n3.Mapper3;
import tech.intellispaces.reflections.framework.guide.n3.Mover3;
import tech.intellispaces.reflections.framework.guide.n3.ObjectMapper3;
import tech.intellispaces.reflections.framework.guide.n3.ObjectMapperOfMoving3;
import tech.intellispaces.reflections.framework.guide.n3.ObjectMover3;
import tech.intellispaces.reflections.framework.guide.n4.Mapper4;
import tech.intellispaces.reflections.framework.guide.n4.Mover4;
import tech.intellispaces.reflections.framework.guide.n4.ObjectMapperOfMoving4;
import tech.intellispaces.reflections.framework.guide.n5.Mapper5;
import tech.intellispaces.reflections.framework.guide.n5.Mover5;
import tech.intellispaces.reflections.framework.naming.NameConventionFunctions;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.reflection.ReflectionForms;
import tech.intellispaces.reflections.framework.reflection.ReflectionFunctions;
import tech.intellispaces.reflections.framework.space.channel.ChannelFunctions;
import tech.intellispaces.reflections.framework.system.UnitWrapper;
import tech.intellispaces.reflections.framework.traverse.TraverseType;
import tech.intellispaces.reflections.framework.traverse.TraverseTypes;

public final class GuideFunctions {

  private GuideFunctions() {}

  public static boolean isGuideType(TypeReference type) {
    return type.isCustomTypeReference() &&
        type.asCustomTypeReferenceOrElseThrow().targetType().hasAnnotation(tech.intellispaces.reflections.framework.annotation.Guide.class);
  }

  public static boolean isGuideMethod(MethodStatement method) {
    return isMapperMethod(method) || isMoverMethod(method) || isMapperOfMovingMethod(method);
  }

  public static GuideKind getGuideKind(MethodStatement method) {
    if (isMapperMethod(method)) {
      return switch (method.params().size()) {
        case 0 -> GuideKinds.Mapper0;
        case 1 -> GuideKinds.Mapper1;
        case 2 -> GuideKinds.Mapper2;
        case 3 -> GuideKinds.Mapper3;
        case 4 -> GuideKinds.Mapper4;
        case 5 -> GuideKinds.Mapper5;
        default -> throw NotImplementedExceptions.withCode("qRSnYQ==");
      };
    } else if (isMoverMethod(method)) {
      return switch (method.params().size()) {
        case 0 -> GuideKinds.Mover0;
        case 1 -> GuideKinds.Mover1;
        case 2 -> GuideKinds.Mover2;
        case 3 -> GuideKinds.Mover3;
        case 4 -> GuideKinds.Mover4;
        case 5 -> GuideKinds.Mover5;
        default -> throw NotImplementedExceptions.withCode("xXtaYA==");
      };
    } else if (isMapperOfMovingMethod(method)) {
      return switch (method.params().size()) {
        case 0 -> GuideKinds.MapperOfMoving0;
        case 1 -> GuideKinds.MapperOfMoving1;
        case 2 -> GuideKinds.MapperOfMoving2;
        case 3 -> GuideKinds.MapperOfMoving3;
        case 4 -> GuideKinds.MapperOfMoving4;
        case 5 -> GuideKinds.MapperOfMoving5;
        default -> throw NotImplementedExceptions.withCode("hxDz2w==");
      };
    } else {
      throw UnexpectedExceptions.withMessage("Could not define guide kind of the guide method {0} in {1}",
          method.name(), method.owner().canonicalName());
    }
  }

  public static boolean isMapperMethod(MethodStatement method) {
    if (method.hasAnnotation(Mapper.class)) {
      return true;
    }
    return method.overrideMethods().stream()
        .anyMatch(m -> m.hasAnnotation(Mapper.class));
  }

  public static boolean isMoverMethod(MethodStatement method) {
    if (method.hasAnnotation(Mover.class)) {
      return true;
    }
    return method.overrideMethods().stream()
        .anyMatch(m -> m.hasAnnotation(Mover.class));
  }

  public static boolean isMapperOfMovingMethod(MethodStatement method) {
    if (method.hasAnnotation(MapperOfMoving.class)) {
      return true;
    }
    return method.overrideMethods().stream()
        .anyMatch(m -> m.hasAnnotation(MapperOfMoving.class));
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

  public static boolean isMapperOfMovingMethod(Method method) {
    return isMapperOfMovingMethod(Methods.of(method));
  }

  public static CustomType getChannelType(MethodStatement guideMethod) {
    Optional<AnnotationInstance> mapper = guideMethod.selectAnnotation(Mapper.class.getCanonicalName());
    if (mapper.isPresent()) {
      Optional<Instance> value = mapper.get().value();
      if (value.isPresent()) {
        return value.get().asClass().orElseThrow().type();
      }
    }

    Optional<AnnotationInstance> mover = guideMethod.selectAnnotation(Mover.class.getCanonicalName());
    if (mover.isPresent()) {
      return mover.get()
          .value()
          .flatMap(Instance::asClass)
          .map(ClassInstance::type)
          .orElseThrow(() -> NotImplementedExceptions.withCode("MrjpVg"));
    }

    Optional<AnnotationInstance> mapperRelatedToMoving = guideMethod.selectAnnotation(
      MapperOfMoving.class.getCanonicalName()
    );
    if (mapperRelatedToMoving.isPresent()) {
      return mapperRelatedToMoving.get()
        .value()
        .flatMap(Instance::asClass)
        .map(ClassInstance::type)
        .orElseThrow(() -> NotImplementedExceptions.withCode("6wSmRQ"));
    }

    throw NotImplementedExceptions.withCode("ATFwew");
  }

  public static Channel findObjectChannelAnnotation(MethodStatement reflectionMethod) {
    return ChannelFunctions.findObjectGuideChannelAnnotation(reflectionMethod);
  }

  public static String getUnitGuideCid(Object unit, MethodStatement guideMethod) {
    return ChannelFunctions.getUnitGuideChannelId(unit, guideMethod);
  }

  public static List<Guide<?, ?>> loadReflectionsGuides(Class<?> reflectionClass) {
    List<Guide<?, ?>> guides = new ArrayList<>();
    CustomType reflectionType = Classes.of(reflectionClass);
    for (MethodStatement method : reflectionType.actualMethods()) {
      if (isGuideMethod(method)) {
        Channel channel = findObjectChannelAnnotation(method);
        TraverseType traverseType = ChannelFunctions.getTraverseType(channel);
        if (TraverseTypes.Mapping.is(traverseType)) {
          guides.add(createObjectMapper(reflectionClass, channel.value(), method));
        } else if (TraverseTypes.Moving.is(traverseType)) {
          guides.add(createObjectMover(reflectionClass, channel.value(), method));
        } else if (TraverseTypes.MappingOfMoving.is(traverseType)) {
          guides.add(createObjectMapperOfMoving(reflectionClass, channel.value(), method));
        }
      }
    }
    addConversionGuides(reflectionClass, guides);
    return guides;
  }

  @SuppressWarnings("unchecked,rawtypes")
  private static void addConversionGuides(Class<?> reflectionClass, List<Guide<?, ?>> guides) {
    String implClassCanonicalName = NameConventionFunctions.getReflectionWrapperCanonicalName(
        reflectionClass
    );
    Optional<Class<?>> reflectionImplClass = ClassFunctions.getClass(implClassCanonicalName);
    if (reflectionImplClass.isEmpty()) {
      throw UnexpectedExceptions.withMessage("Could not get reflection realization class {0}",
          implClassCanonicalName);
    }
    CustomType reflectionWrapperType = Interfaces.of(reflectionImplClass.get());

    CustomType domainType = ReflectionFunctions.getDomainOfObjectFormOrElseThrow(Classes.of(reflectionClass));

    for (MethodStatement method : domainType.declaredMethods()) {
      if (NameConventionFunctions.isConversionMethod(method)) {
        String cid = method.selectAnnotation(Channel.class).orElseThrow().value();

        Optional<MethodStatement> wrapperMethod = reflectionWrapperType.declaredMethod(method.name(), List.of());
        int channelOrdinal = wrapperMethod.orElseThrow().selectAnnotation(Ordinal.class).orElseThrow().value();

        Guide<?, ?> guide = new ObjectMapper0<>(
            cid,
            (Class) reflectionClass,
            method,
            channelOrdinal,
            ReflectionForms.Reflection
        );
        guides.add(guide);
      }
    }
  }

  public static GuideKind getGuideKind(Class<?> guideClass) {
    GuideKind guideKind = GUIDE_CLASS_TO_KIND.get(guideClass);
    if (guideKind == null) {
      throw UnexpectedExceptions.withMessage("Unsupported guide class: " + guideClass.getCanonicalName());
    }
    return guideKind;
  }

  @SuppressWarnings("unchecked, rawtypes")
  private static <S, T> Guide<S, T> createObjectMapper(
      Class<S> reflectionClass, String cid, MethodStatement guideMethod
  ) {
    ReflectionForm targetForm = getTargetForm(guideMethod);
    int channelOrdinal = getChannelOrdinal(reflectionClass, guideMethod);
    int qualifiersCount = guideMethod.params().size();
    return switch (qualifiersCount) {
      case 0 -> new ObjectMapper0<>(cid, (Class) reflectionClass, guideMethod, channelOrdinal, targetForm);
      case 1 -> new ObjectMapper1<>(cid, (Class) reflectionClass, guideMethod, channelOrdinal, targetForm);
      case 2 -> new ObjectMapper2<>(cid, (Class) reflectionClass, guideMethod, channelOrdinal, targetForm);
      case 3 -> new ObjectMapper3<>(cid, (Class) reflectionClass, guideMethod, channelOrdinal, targetForm);
      default -> throw UnexpectedExceptions.withMessage("Unsupported number of guide qualifiers: {0}",
          qualifiersCount);
    };
  }

  @SuppressWarnings("unchecked, rawtypes")
  private static <S, T> Guide<S, T> createObjectMover(
      Class<S> reflectionClass, String cid, MethodStatement guideMethod
  ) {
    ReflectionForm targetForm = getTargetForm(guideMethod);
    int channelOrdinal = getChannelOrdinal(reflectionClass, guideMethod);
    int qualifiersCount = guideMethod.params().size();
    return switch (qualifiersCount) {
      case 0 -> new ObjectMover0<>(cid, (Class) reflectionClass, guideMethod, channelOrdinal, targetForm);
      case 1 -> new ObjectMover1<>(cid, (Class) reflectionClass, guideMethod, channelOrdinal, targetForm);
      case 2 -> new ObjectMover2<>(cid, (Class) reflectionClass, guideMethod, channelOrdinal, targetForm);
      case 3 -> new ObjectMover3<>(cid, (Class) reflectionClass, guideMethod, channelOrdinal, targetForm);
      default -> throw UnexpectedExceptions.withMessage("Unsupported number of guide qualifiers: {0}",
          qualifiersCount);
    };
  }

  @SuppressWarnings("unchecked, rawtypes")
  private static <S, T> Guide<S, T> createObjectMapperOfMoving(
      Class<S> reflectionClass, String cid, MethodStatement guideMethod
  ) {
    ReflectionForm targetForm = getTargetForm(guideMethod);
    int channelOrdinal = getChannelOrdinal(reflectionClass, guideMethod);
    int qualifiersCount = guideMethod.params().size();
    return switch (qualifiersCount) {
      case 0 -> new ObjectMapperOfMoving0<>(cid, (Class) reflectionClass, guideMethod, channelOrdinal, targetForm);
      case 1 -> new ObjectMapperOfMoving1<>(cid, (Class) reflectionClass, guideMethod, channelOrdinal, targetForm);
      case 2 -> new ObjectMapperOfMoving2<>(cid, (Class) reflectionClass, guideMethod, channelOrdinal, targetForm);
      case 3 -> new ObjectMapperOfMoving3<>(cid, (Class) reflectionClass, guideMethod, channelOrdinal, targetForm);
      case 4 -> new ObjectMapperOfMoving4<>(cid, (Class) reflectionClass, guideMethod, channelOrdinal, targetForm);
      default -> throw UnexpectedExceptions.withMessage("Unsupported number of guide qualifiers: {0}",
          qualifiersCount);
    };
  }

  public static ReflectionForm getTargetForm(MethodStatement guideMethod) {
    TypeReference returnType = guideMethod.returnType().orElseThrow();
    if (returnType.isCustomTypeReference()) {
      if (ClassFunctions.isPrimitiveWrapperClass(returnType.asCustomTypeReferenceOrElseThrow().targetType().canonicalName())) {
        return ReflectionForms.Primitive;
      }
      if (ReflectionFunctions.isReflectionType(returnType)) {
        return ReflectionForms.Reflection;
      }
    }
    return ReflectionForms.Reflection;
  }

  public static int getChannelOrdinal(Class<?> reflectionClass, MethodStatement guideMethod) {
    String wrapperClassCanonicalName = NameConventionFunctions.getReflectionWrapperCanonicalName(
        reflectionClass
    );
    Optional<Class<?>> wrapperClass = ClassFunctions.getClass(wrapperClassCanonicalName);
    if (wrapperClass.isEmpty()) {
      throw UnexpectedExceptions.withMessage("Could not get object wrapper class {0}",
          wrapperClassCanonicalName);
    }

    Optional<MethodStatement> wrapperGuideMethod = MethodFunctions.getOverrideMethod(
        CustomTypes.of(wrapperClass.get()),
        guideMethod
    );
    if (wrapperGuideMethod.isEmpty()) {
      throw UnexpectedExceptions.withMessage("Could not find override method in object wrapper " +
          "class {0}. Guide method {1} in {2}", wrapperClass.get(), guideMethod.name(), guideMethod.owner().canonicalName());
    }
    Optional<Ordinal> indexAnnotation = wrapperGuideMethod.get().selectAnnotation(Ordinal.class);
    if (indexAnnotation.isEmpty()) {
      throw UnexpectedExceptions.withMessage("Method {0} does not contain annotation {1}",
          guideMethod.name(), Ordinal.class.getCanonicalName());
    }
    return indexAnnotation.get().value();
  }

  private static int getUnitGuideOrdinal(UnitWrapper unit, MethodStatement guideMethod) {
    Class<?> unitWrapperClass = unit.getClass();
    Optional<MethodStatement> overrideGuideMethod = MethodFunctions.getOverrideMethod(
        CustomTypes.of(unitWrapperClass), guideMethod
    );
    if (overrideGuideMethod.isEmpty()) {
      throw UnexpectedExceptions.withMessage("Could not find override method in unit wrapper " +
          "class {0}. Method {1}", unitWrapperClass, guideMethod.name());
    }
    Optional<Ordinal> ordinalAnnotation = overrideGuideMethod.get().selectAnnotation(Ordinal.class);
    if (ordinalAnnotation.isEmpty()) {
      throw UnexpectedExceptions.withMessage("Method {0} does not contain annotation {1}",
          guideMethod.name(), Ordinal.class.getCanonicalName());
    }
    return ordinalAnnotation.get().value();
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
