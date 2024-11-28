package tech.intellispaces.jaquarius.annotation.processor;

import tech.intellispaces.jaquarius.guide.GuideForm;
import tech.intellispaces.jaquarius.guide.GuideForms;
import tech.intellispaces.action.runnable.RunnableAction;
import tech.intellispaces.action.text.StringActions;
import tech.intellispaces.entity.exception.UnexpectedExceptions;
import tech.intellispaces.entity.object.ObjectFunctions;
import tech.intellispaces.entity.text.StringFunctions;
import tech.intellispaces.entity.type.ClassFunctions;
import tech.intellispaces.entity.type.PrimitiveType;
import tech.intellispaces.entity.type.PrimitiveTypes;
import tech.intellispaces.jaquarius.object.ObjectHandleFunctions;
import tech.intellispaces.jaquarius.object.ObjectHandleTypes;
import tech.intellispaces.java.annotation.context.JavaArtifactContext;
import tech.intellispaces.java.reflection.customtype.Classes;
import tech.intellispaces.java.reflection.customtype.CustomType;
import tech.intellispaces.java.reflection.method.MethodParam;
import tech.intellispaces.java.reflection.method.MethodStatement;
import tech.intellispaces.java.reflection.reference.CustomTypeReferences;
import tech.intellispaces.java.reflection.reference.NamedReference;
import tech.intellispaces.java.reflection.reference.PrimitiveReference;
import tech.intellispaces.java.reflection.reference.PrimitiveReferences;
import tech.intellispaces.java.reflection.reference.TypeReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public interface GuideProcessorFunctions {

  static GuideForm getGuideForm(MethodStatement guideMethod) {
    TypeReference returnType = guideMethod.returnType().orElseThrow();
    if (returnType.isPrimitiveReference()) {
      return GuideForms.PrimitiveType;
    } else {
      return GuideForms.Main;
    }
  }

  static TypeReference normalizeType(TypeReference type) {
    if (type.isPrimitiveReference()) {
      String typename = type.asPrimitiveReferenceOrElseThrow().typename();
      if (ObjectFunctions.equalsAnyOf(typename,
          PrimitiveTypes.Boolean.typename(),
          PrimitiveTypes.Char.typename(),
          PrimitiveTypes.Byte.typename(),
          PrimitiveTypes.Short.typename(),
          PrimitiveTypes.Int.typename()
      )) {
        return PrimitiveReferences.Int;
      }
      if (PrimitiveTypes.Float.typename().equals(typename)) {
        return PrimitiveReferences.Double;
      }
    }
    return type;
  }

  static String buildGuideAction(
      String wrapperClassCanonicalName, MethodStatement guideMethod, JavaArtifactContext context
  ) {
    CustomType wrapperType = Classes.build().canonicalName(wrapperClassCanonicalName).get();

    List<TypeReference> paramTypes = new ArrayList<>();
    paramTypes.add(CustomTypeReferences.get(wrapperType));
    rearrangementParams(guideMethod.params()).forEach(param -> paramTypes.add(param.type()));

    return buildGuideAction(wrapperType, guideMethod, paramTypes, context);
  }

  private static String buildGuideAction(
      CustomType wrapperType, MethodStatement guideMethod, List<TypeReference> paramTypes, JavaArtifactContext context
  ) {
    GuideForm guideForm = getGuideForm(guideMethod);

    var sb = new StringBuilder();
    sb.append(buildGuideActionGetterName(paramTypes, guideMethod.returnType().orElseThrow()));
    sb.append("(");
    sb.append(wrapperType.simpleName());
    sb.append("::");
    sb.append(ObjectHandleFunctions.buildObjectHandleGuideMethodName(guideMethod));
    sb.append(", ");
    sb.append(buildGuideActionReturnType(normalizeType(guideMethod.returnType().orElseThrow()), guideForm, context));
    sb.append(".class");
    for (TypeReference paramType : paramTypes) {
      sb.append(", ");
      sb.append(buildGuideActionParamType(normalizeType(paramType), context));
      sb.append(".class");
    }
    sb.append(")");
    return sb.toString();
  }

  private static String buildGuideActionReturnType(
      TypeReference type, GuideForm guideForm, JavaArtifactContext context
  ) {
    if (guideForm == GuideForms.Main) {
      return buildGuideActionParamType(type, context);
    } else if (guideForm == GuideForms.PrimitiveType) {
      return type.asPrimitiveReferenceOrElseThrow().typename();
    } else {
      throw UnexpectedExceptions.withMessage("Not supported guide form: {0}", guideForm.name());
    }
  }

  private static String buildGuideActionParamType(
      TypeReference type, JavaArtifactContext context
  ) {
    if (type.isNamedReference()) {
      return "Object";
    } else if (type.isCustomTypeReference()) {
      return context.addToImportAndGetSimpleName(type.asCustomTypeReferenceOrElseThrow().targetType().canonicalName());
    }
    return type.actualDeclaration(context::addToImportAndGetSimpleName);
  }

  private static String buildGuideActionGetterName(List<TypeReference> paramTypes, TypeReference returnType) {
    var allParamsAreObject = new AtomicBoolean(true);
    List<String> paramTypenames = new ArrayList<>();
    paramTypes.forEach(type -> {
      paramTypenames.add(type(type));
      if (type.isPrimitiveReference()) {
        allParamsAreObject.set(false);
      }
    });
    String returnTypeName = type(returnType);

    if (allParamsAreObject.get() && Object.class.getSimpleName().equals(returnTypeName)) {
      return switch (paramTypes.size()) {
        case 1 -> "FunctionActions.ofFunction";
        case 2 -> "FunctionActions.ofBiFunction";
        case 3 -> "FunctionActions.ofTriFunction";
        case 4 -> "FunctionActions.ofQuadriFunction";
        case 5 -> "FunctionActions.ofQuintiFunction";
        default -> throw UnexpectedExceptions.withMessage("Not supported number of params");
      };
    } else {
      var sb = new StringBuilder();
      sb.append("FunctionActions.of");

      String curParamTypename = null;
      boolean first = true;
      int counter = 0;
      for (String paramTypename : paramTypenames) {
        if (curParamTypename == null) {
          curParamTypename = paramTypename;
          counter = 1;
        } else if (curParamTypename.equals(paramTypename)) {
          counter++;
        } else {
          appendParameterTypename(sb, curParamTypename, counter, first);
          curParamTypename = paramTypename;
          first = false;
          counter = 1;
        }
      }
      if (curParamTypename != null) {
        appendParameterTypename(sb, curParamTypename, counter, first);
      }
      sb.append("To");
      sb.append(StringFunctions.capitalizeFirstLetter(returnTypeName));
      sb.append("Function");
      return sb.toString();
    }
  }

  private static void appendParameterTypename(StringBuilder sb, String paramTypename, int counter, boolean first) {
    if (!first) {
      sb.append("And");
    }
    String prefix = switch (counter) {
      case 2 -> "Two";
      case 3 -> "Three";
      case 4 -> "Four";
      case 5 -> "Five";
      default -> "";
    };
    sb.append(prefix);
    sb.append(StringFunctions.capitalizeFirstLetter(paramTypename));
    if (counter > 1) {
      sb.append("s");
    }
  }

  private static String type(TypeReference type) {
    if (type.isNamedReference()) {
      return Object.class.getSimpleName();
    } else if (type.isPrimitiveReference()) {
      return normalizeType(type).asPrimitiveReferenceOrElseThrow().typename();
    } else {
      return Object.class.getSimpleName();
    }
  }

  static Map<String, String> buildGuideActionMethod(MethodStatement guideMethod, JavaArtifactContext context) {
    var sb = new StringBuilder();
    RunnableAction commaAppender = StringActions.skipFirstTimeCommaAppender(sb);
    sb.append("private ");
    if (!guideMethod.typeParameters().isEmpty()) {
      sb.append("<");
      for (NamedReference param : guideMethod.typeParameters()) {
        commaAppender.run();
        sb.append(param.formalFullDeclaration());
      }
      sb.append("> ");
    }
    String returnType = buildGuideTypeDeclaration(guideMethod.returnType().orElseThrow(), context);
    sb.append(returnType);
    sb.append(" ");
    sb.append(ObjectHandleFunctions.buildObjectHandleGuideMethodName(guideMethod));
    sb.append("(");
    commaAppender = StringActions.skipFirstTimeCommaAppender(sb);
    for (MethodParam param : rearrangementParams(guideMethod.params())) {
      commaAppender.run();
      sb.append(buildGuideTypeDeclaration(param.type(), context));
      sb.append(" ");
      sb.append(param.name());
    }
    sb.append(") {\n");
    sb.append("  return ");
    String actualReturnType = guideMethod.returnType().orElseThrow().actualDeclaration(context::addToImportAndGetSimpleName);
    if (!actualReturnType.equals(returnType)) {
      if (PrimitiveTypes.Boolean.typename().equals(actualReturnType)) {
        sb.append("PrimitiveFunctions.booleanToInt(");
        buildInvokeSuperMethod(guideMethod, sb, context);
        sb.append(")");
      } else {
        buildInvokeSuperMethod(guideMethod, sb, context);
      }
    } else {
      buildInvokeSuperMethod(guideMethod, sb, context);
    }
    sb.append(";\n");
    sb.append("}\n");
    return Map.of("declaration", sb.toString());
  }

  static Map<String, Object> buildObjectHandleMethodDescriptor(
      MethodStatement objectHandleMethod, MethodStatement guideMethod, JavaArtifactContext context
  ) {
    var map = new HashMap<String, Object>();
    map.put("name", objectHandleMethod.name());

    List<String> paramClasses = new ArrayList<>();
    for (MethodParam param : objectHandleMethod.params()) {
      paramClasses.add(buildGuideParamClassName(param.type(), context));
    }
    map.put("params", paramClasses);

    if (guideMethod != null) {
      map.put("guideName", ObjectHandleFunctions.buildObjectHandleGuideMethodName(guideMethod));

      List<String> guideParamClasses = new ArrayList<>();
      for (MethodParam param : guideMethod.params()) {
        guideParamClasses.add(buildGuideParamClassName(param.type(), context));
      }
      map.put("guideParams", guideParamClasses);
    } else {
      map.put("guideName", "");
      map.put("guideParams", List.of());
    }
    return map;
  }

  private static void buildInvokeSuperMethod(
      MethodStatement objectHandleMethod, StringBuilder sb, JavaArtifactContext context
  ) {
    RunnableAction commaAppender;
    sb.append("super.");
    sb.append(objectHandleMethod.name());
    sb.append("(");
    commaAppender = StringActions.skipFirstTimeCommaAppender(sb);
    for (MethodParam param : rearrangementParams(objectHandleMethod.params())) {
      commaAppender.run();
      String actualType = param.type().actualDeclaration(context::addToImportAndGetSimpleName);
      if (!buildGuideTypeDeclaration(param.type(), context).equals(actualType)) {
        if (PrimitiveTypes.Boolean.typename().equals(actualType)) {
          sb.append("PrimitiveFunctions.booleanToInt(").append(param.name());
        } else {
          sb.append("(");
          sb.append(actualType);
          sb.append(") ");
          sb.append(param.name());
        }
      } else {
        sb.append(param.name());
      }
    }
    sb.append(")");
  }

  private static String buildGuideParamClassName(TypeReference type, JavaArtifactContext context) {
    return ObjectHandleFunctions.getObjectHandleDeclaration(
        GuideProcessorFunctions.normalizeType(type),
        ObjectHandleTypes.Common,
        false,
        context::addToImportAndGetSimpleName
    );
  }

  private static String buildGuideTypeDeclaration(TypeReference type, JavaArtifactContext context) {
    return GuideProcessorFunctions.normalizeType(type).actualDeclaration(context::addToImportAndGetSimpleName);
  }

  static List<MethodParam> rearrangementParams(List<MethodParam> params) {
    List<MethodParam> result = new ArrayList<>(params.size());

    // 1. Objects
    for (MethodParam param : params) {
      if (param.type().isNamedReference()) {
        result.add(param);
      }
      if (param.type().isCustomTypeReference()) {
        CustomType type = param.type().asCustomTypeReferenceOrElseThrow().targetType();
        Optional<PrimitiveType> primitive = ClassFunctions.primitiveByWrapperClassName(type.canonicalName());
        if (primitive.isEmpty()) {
          result.add(param);
        }
      }
    }

    // 2. Long primitives
    for (MethodParam param : params) {
      if (param.type().isCustomTypeReference()) {
        CustomType type = param.type().asCustomTypeReferenceOrElseThrow().targetType();
        Optional<PrimitiveType> primitive = ClassFunctions.primitiveByWrapperClassName(type.canonicalName());
        if (primitive.isPresent() && primitive.get().isLong()) {
          result.add(param);
        }
      } else if (param.type().isPrimitiveReference()) {
        if (param.type().asPrimitiveReferenceOrElseThrow().primitiveType().isLong()) {
          result.add(param);
        }
      }
    }

    // 3. Integer related primitives
    for (MethodParam param : params) {
      if (param.type().isCustomTypeReference()) {
        CustomType type = param.type().asCustomTypeReferenceOrElseThrow().targetType();
        Optional<PrimitiveType> primitive = ClassFunctions.primitiveByWrapperClassName(type.canonicalName());
        if (primitive.isPresent()
            && (primitive.get().isBoolean()
                || primitive.get().isChar()
                || primitive.get().isByte()
                || primitive.get().isShort()
                || primitive.get().isInt()
        )) {
          result.add(param);
        }
      } else if (param.type().isPrimitiveReference()) {
        PrimitiveReference primitiveReference = param.type().asPrimitiveReferenceOrElseThrow();
        PrimitiveType primitiveType = primitiveReference.primitiveType();
        if (primitiveType.isBoolean()
            || primitiveType.isChar()
            || primitiveType.isByte()
            || primitiveType.isShort()
            || primitiveType.isInt()
        ) {
          result.add(param);
        }
      }
    }

    // 4. Float related primitives
    for (MethodParam param : params) {
      if (param.type().isCustomTypeReference()) {
        CustomType type = param.type().asCustomTypeReferenceOrElseThrow().targetType();
        Optional<PrimitiveType> primitive = ClassFunctions.primitiveByWrapperClassName(type.canonicalName());
        if (primitive.isPresent() && (primitive.get().isFloat() || primitive.get().isDouble())) {
          result.add(param);
        }
      } else if (param.type().isPrimitiveReference()) {
        PrimitiveReference primitiveReference = param.type().asPrimitiveReferenceOrElseThrow();
        PrimitiveType primitiveType = primitiveReference.primitiveType();
        if (primitiveType.isFloat() || primitiveType.isDouble()) {
          result.add(param);
        }
      }
    }

    if (result.size() != params.size()) {
      throw UnexpectedExceptions.withMessage("Invalid state");
    }
    return result;
  }
}
