package intellispaces.jaquarius.guide;

import intellispaces.common.base.exception.NotImplementedExceptions;
import intellispaces.common.base.exception.UnexpectedExceptions;
import intellispaces.common.base.type.ClassFunctions;
import intellispaces.common.javastatement.customtype.Classes;
import intellispaces.common.javastatement.customtype.CustomType;
import intellispaces.common.javastatement.customtype.CustomTypes;
import intellispaces.common.javastatement.customtype.Interfaces;
import intellispaces.common.javastatement.instance.AnnotationInstance;
import intellispaces.common.javastatement.instance.ClassInstance;
import intellispaces.common.javastatement.instance.Instance;
import intellispaces.common.javastatement.method.MethodFunctions;
import intellispaces.common.javastatement.method.MethodStatement;
import intellispaces.common.javastatement.method.Methods;
import intellispaces.common.javastatement.reference.TypeReference;
import intellispaces.jaquarius.annotation.Channel;
import intellispaces.jaquarius.annotation.Mapper;
import intellispaces.jaquarius.annotation.MapperOfMoving;
import intellispaces.jaquarius.annotation.Mover;
import intellispaces.jaquarius.annotation.Ordinal;
import intellispaces.jaquarius.common.NameConventionFunctions;
import intellispaces.jaquarius.guide.n0.Mapper0;
import intellispaces.jaquarius.guide.n0.Mover0;
import intellispaces.jaquarius.guide.n0.ObjectMapper0;
import intellispaces.jaquarius.guide.n0.ObjectMapperOfMoving0;
import intellispaces.jaquarius.guide.n0.ObjectMover0;
import intellispaces.jaquarius.guide.n0.UnitMapper0;
import intellispaces.jaquarius.guide.n0.UnitMapperOfMoving0;
import intellispaces.jaquarius.guide.n1.Mapper1;
import intellispaces.jaquarius.guide.n1.Mover1;
import intellispaces.jaquarius.guide.n1.ObjectMapper1;
import intellispaces.jaquarius.guide.n1.ObjectMapperOfMoving1;
import intellispaces.jaquarius.guide.n1.ObjectMover1;
import intellispaces.jaquarius.guide.n1.UnitMapper1;
import intellispaces.jaquarius.guide.n1.UnitMapperOfMoving1;
import intellispaces.jaquarius.guide.n2.Mapper2;
import intellispaces.jaquarius.guide.n2.Mover2;
import intellispaces.jaquarius.guide.n2.ObjectMapper2;
import intellispaces.jaquarius.guide.n2.ObjectMapperOfMoving2;
import intellispaces.jaquarius.guide.n2.ObjectMover2;
import intellispaces.jaquarius.guide.n2.UnitMapper2;
import intellispaces.jaquarius.guide.n2.UnitMapperOfMoving2;
import intellispaces.jaquarius.guide.n3.Mapper3;
import intellispaces.jaquarius.guide.n3.Mover3;
import intellispaces.jaquarius.guide.n3.ObjectMapper3;
import intellispaces.jaquarius.guide.n3.ObjectMapperOfMoving3;
import intellispaces.jaquarius.guide.n3.ObjectMover3;
import intellispaces.jaquarius.guide.n3.UnitMapper3;
import intellispaces.jaquarius.guide.n3.UnitMapperOfMoving3;
import intellispaces.jaquarius.guide.n4.Mapper4;
import intellispaces.jaquarius.guide.n4.Mover4;
import intellispaces.jaquarius.guide.n4.ObjectMapperOfMoving4;
import intellispaces.jaquarius.guide.n5.Mapper5;
import intellispaces.jaquarius.guide.n5.Mover5;
import intellispaces.jaquarius.object.ObjectFunctions;
import intellispaces.jaquarius.space.channel.ChannelFunctions;
import intellispaces.jaquarius.system.UnitGuide;
import intellispaces.jaquarius.system.UnitWrapper;
import intellispaces.jaquarius.traverse.TraverseTypes;

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
        type.asCustomTypeReferenceOrElseThrow().targetType().hasAnnotation(intellispaces.jaquarius.annotation.Guide.class);
  }

  public static boolean isGuideMethod(MethodStatement method) {
    return isMapperMethod(method) || isMoverMethod(method) || isMapperOfMovingMethod(method);
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

  public static Channel findObjectChannelAnnotation(MethodStatement objectHandleMethod) {
    return ChannelFunctions.findObjectGuideChannelAnnotation(objectHandleMethod);
  }

  public static String getUnitGuideTid(Object unitInstance, MethodStatement guideMethod) {
    return ChannelFunctions.getUnitGuideCid(unitInstance, guideMethod);
  }

  public static List<Guide<?, ?>> loadObjectGuides(Class<?> objectHandleClass) {
    List<Guide<?, ?>> guides = new ArrayList<>();
    CustomType objectHandleType = Classes.of(objectHandleClass);
    for (MethodStatement method : objectHandleType.actualMethods()) {
      if (isGuideMethod(method)) {
        Channel channel = findObjectChannelAnnotation(method);
        if (ChannelFunctions.getTraverseType(channel) == TraverseTypes.Mapping) {
          guides.add(createObjectMapper(objectHandleClass, channel.value(), method));
        } else if (ChannelFunctions.getTraverseType(channel) == TraverseTypes.Moving) {
          guides.add(createObjectMover(objectHandleClass, channel.value(), method));
        } else if (ChannelFunctions.getTraverseType(channel) == TraverseTypes.MappingOfMoving) {
          guides.add(createObjectMapperOfMoving(objectHandleClass, channel.value(), method));
        }
      }
    }
    addConversionGuides(objectHandleClass, guides);
    return guides;
  }

  @SuppressWarnings("unchecked,rawtypes")
  private static void addConversionGuides(Class<?> objectHandleClass, List<Guide<?, ?>> guides) {
    String implClassCanonicalName = NameConventionFunctions.getObjectHandleWrapperCanonicalName(
        objectHandleClass
    );
    Optional<Class<?>> objectHandleImplClass = ClassFunctions.getClass(implClassCanonicalName);
    if (objectHandleImplClass.isEmpty()) {
      throw UnexpectedExceptions.withMessage("Could not get object handle implementation class {0}",
          implClassCanonicalName);
    }
    CustomType objectHandleWrapperType = Interfaces.of(objectHandleImplClass.get());

    CustomType domainType = ObjectFunctions.getDomainTypeOfObjectHandle(Classes.of(objectHandleClass));

    for (MethodStatement method : domainType.declaredMethods()) {
      if (NameConventionFunctions.isConversionMethod(method)) {
        String cid = method.selectAnnotation(Channel.class).orElseThrow().value();

        Optional<MethodStatement> wrapperMethod = objectHandleWrapperType.declaredMethod(method.name(), List.of());
        int channelOrdinal = wrapperMethod.orElseThrow().selectAnnotation(Ordinal.class).orElseThrow().value();

        Guide<?, ?> guide = new ObjectMapper0<>(
            cid,
            (Class) objectHandleClass,
            method,
            channelOrdinal,
            GuideForms.Main
        );
        guides.add(guide);
      }
    }
  }

  public static List<UnitGuide<?, ?>> readUnitGuides(Class<?> unitClass, UnitWrapper unitInstance) {
    List<UnitGuide<?, ?>> guides = new ArrayList<>();
    for (MethodStatement method : Classes.of(unitClass).actualMethods()) {
      if (isMapperMethod(method)) {
        UnitGuide<?, ?> mapper = createUnitMapper(unitInstance, method);
        guides.add(mapper);
      } else if (isMoverMethod(method)) {
        UnitGuide<?, ?> mover = createMover(unitInstance, method);
          guides.add(mover);
      } else if (isMapperOfMovingMethod(method)) {
        UnitGuide<?, ?> mapperOfMoving = createUnitMapperOfMoving(unitInstance, method);
        guides.add(mapperOfMoving);
      }
    }
    return guides;
  }

  public static GuideKind getGuideKind(Class<?> guideClass) {
    GuideKind guideKind = GUIDE_CLASS_TO_KIND.get(guideClass);
    if (guideKind == null) {
      throw UnexpectedExceptions.withMessage("Unsupported guide class: " + guideClass.getCanonicalName());
    }
    return guideKind;
  }

  private static UnitGuide<?, ?> createUnitMapper(UnitWrapper unitInstance, MethodStatement guideMethod) {
    GuideForm guideForm = getGuideForm(guideMethod);
    String cid = getUnitGuideTid(unitInstance, guideMethod);
    int guideOrdinal = getUnitGuideOrdinal(unitInstance, guideMethod);
    int qualifiersCount = guideMethod.params().size();
    return switch (qualifiersCount) {
      case 1 -> new UnitMapper0<>(cid, unitInstance, guideMethod, guideOrdinal, guideForm);
      case 2 -> new UnitMapper1<>(cid, unitInstance, guideMethod, guideOrdinal, guideForm);
      case 3 -> new UnitMapper2<>(cid, unitInstance, guideMethod, guideOrdinal, guideForm);
      case 4 -> new UnitMapper3<>(cid, unitInstance, guideMethod, guideOrdinal, guideForm);
      default -> throw UnexpectedExceptions.withMessage("Unsupported number of guide qualifiers: {0}",
          qualifiersCount);
    };
  }

  private static UnitGuide<?, ?> createMover(Object unitInstance, MethodStatement guideMethod) {
    String cid = getUnitGuideTid(unitInstance, guideMethod);
    throw NotImplementedExceptions.withCode("4GL2+g");
  }

  private static UnitGuide<?, ?> createUnitMapperOfMoving(UnitWrapper unitInstance, MethodStatement guideMethod) {
    GuideForm guideForm = getGuideForm(guideMethod);
    String cid = getUnitGuideTid(unitInstance, guideMethod);
    int guideOrdinal = getUnitGuideOrdinal(unitInstance, guideMethod);
    int qualifiersCount = guideMethod.params().size();
    return switch (qualifiersCount) {
      case 1 -> new UnitMapperOfMoving0<>(cid, unitInstance, guideMethod, guideOrdinal, guideForm);
      case 2 -> new UnitMapperOfMoving1<>(cid, unitInstance, guideMethod, guideOrdinal, guideForm);
      case 3 -> new UnitMapperOfMoving2<>(cid, unitInstance, guideMethod, guideOrdinal, guideForm);
      case 4 -> new UnitMapperOfMoving3<>(cid, unitInstance, guideMethod, guideOrdinal, guideForm);
      default -> throw UnexpectedExceptions.withMessage("Unsupported number of guide qualifiers: {0}",
          qualifiersCount);
    };
  }

  @SuppressWarnings("unchecked, rawtypes")
  private static <S, T> Guide<S, T> createObjectMapper(
      Class<S> objectHandleClass, String cid, MethodStatement guideMethod
  ) {
    GuideForm guideForm = getGuideForm(guideMethod);
    int channelOrdinal = getChannelOrdinal(objectHandleClass, guideMethod);
    int qualifiersCount = guideMethod.params().size();
    return switch (qualifiersCount) {
      case 0 -> new ObjectMapper0<>(cid, (Class) objectHandleClass, guideMethod, channelOrdinal, guideForm);
      case 1 -> new ObjectMapper1<>(cid, (Class) objectHandleClass, guideMethod, channelOrdinal, guideForm);
      case 2 -> new ObjectMapper2<>(cid, (Class) objectHandleClass, guideMethod, channelOrdinal, guideForm);
      case 3 -> new ObjectMapper3<>(cid, (Class) objectHandleClass, guideMethod, channelOrdinal, guideForm);
      default -> throw UnexpectedExceptions.withMessage("Unsupported number of guide qualifiers: {0}",
          qualifiersCount);
    };
  }

  @SuppressWarnings("unchecked, rawtypes")
  private static <S, T> Guide<S, T> createObjectMover(
      Class<S> objectHandleClass, String cid, MethodStatement guideMethod
  ) {
    GuideForm guideForm = getGuideForm(guideMethod);
    int channelOrdinal = getChannelOrdinal(objectHandleClass, guideMethod);
    int qualifiersCount = guideMethod.params().size();
    return switch (qualifiersCount) {
      case 0 -> new ObjectMover0<>(cid, (Class) objectHandleClass, guideMethod, channelOrdinal, guideForm);
      case 1 -> new ObjectMover1<>(cid, (Class) objectHandleClass, guideMethod, channelOrdinal, guideForm);
      case 2 -> new ObjectMover2<>(cid, (Class) objectHandleClass, guideMethod, channelOrdinal, guideForm);
      case 3 -> new ObjectMover3<>(cid, (Class) objectHandleClass, guideMethod, channelOrdinal, guideForm);
      default -> throw UnexpectedExceptions.withMessage("Unsupported number of guide qualifiers: {0}",
          qualifiersCount);
    };
  }

  @SuppressWarnings("unchecked, rawtypes")
  private static <S, T> Guide<S, T> createObjectMapperOfMoving(
      Class<S> objectHandleClass, String cid, MethodStatement guideMethod
  ) {
    GuideForm guideForm = getGuideForm(guideMethod);
    int channelOrdinal = getChannelOrdinal(objectHandleClass, guideMethod);
    int qualifiersCount = guideMethod.params().size();
    return switch (qualifiersCount) {
      case 0 -> new ObjectMapperOfMoving0<>(cid, (Class) objectHandleClass, guideMethod, channelOrdinal, guideForm);
      case 1 -> new ObjectMapperOfMoving1<>(cid, (Class) objectHandleClass, guideMethod, channelOrdinal, guideForm);
      case 2 -> new ObjectMapperOfMoving2<>(cid, (Class) objectHandleClass, guideMethod, channelOrdinal, guideForm);
      case 3 -> new ObjectMapperOfMoving3<>(cid, (Class) objectHandleClass, guideMethod, channelOrdinal, guideForm);
      case 4 -> new ObjectMapperOfMoving4<>(cid, (Class) objectHandleClass, guideMethod, channelOrdinal, guideForm);
      default -> throw UnexpectedExceptions.withMessage("Unsupported number of guide qualifiers: {0}",
          qualifiersCount);
    };
  }

  private static GuideForm getGuideForm(MethodStatement guideMethod) {
    if (guideMethod.returnType().orElseThrow().isPrimitiveReference()) {
      return GuideForms.Primitive;
    }
    return GuideForms.Main;
  }

  public static int getChannelOrdinal(Class<?> objectHandleClass, MethodStatement guideMethod) {
    String implClassCanonicalName = NameConventionFunctions.getObjectHandleWrapperCanonicalName(
        objectHandleClass
    );
    Optional<Class<?>> objectHandleImplClass = ClassFunctions.getClass(implClassCanonicalName);
    if (objectHandleImplClass.isEmpty()) {
      throw UnexpectedExceptions.withMessage("Could not get object handle implementation class {0}",
          implClassCanonicalName);
    }

    Optional<MethodStatement> objectHandleImplGuideMethod = MethodFunctions.getOverrideMethod(
        CustomTypes.of(objectHandleImplClass.get()),
        guideMethod
    );
    if (objectHandleImplGuideMethod.isEmpty()) {
      throw UnexpectedExceptions.withMessage("Could not find override method in object handle " +
          "implementation class {0}. Method {1}", objectHandleImplClass.get(), guideMethod.name());
    }
    Optional<Ordinal> indexAnnotation = objectHandleImplGuideMethod.get().selectAnnotation(Ordinal.class);
    if (indexAnnotation.isEmpty()) {
      throw UnexpectedExceptions.withMessage("Method {0} does not contain annotation {1}",
          guideMethod.name(), Ordinal.class.getCanonicalName());
    }
    return indexAnnotation.get().value();
  }

  private static int getUnitGuideOrdinal(UnitWrapper unitInstance, MethodStatement guideMethod) {
    Class<?> unitWrapperClass = unitInstance.getClass();
    Optional<MethodStatement> overrideGuideMethod = MethodFunctions.getOverrideMethod(
        CustomTypes.of(unitWrapperClass),
        guideMethod
    );
    if (overrideGuideMethod.isEmpty()) {
      throw UnexpectedExceptions.withMessage("Could not find override method in unit wrapper " +
          "class {0}. Method {1}", unitWrapperClass, guideMethod.name());
    }
    Optional<Ordinal> indexAnnotation = overrideGuideMethod.get().selectAnnotation(Ordinal.class);
    if (indexAnnotation.isEmpty()) {
      throw UnexpectedExceptions.withMessage("Method {0} does not contain annotation {1}",
          guideMethod.name(), Ordinal.class.getCanonicalName());
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
