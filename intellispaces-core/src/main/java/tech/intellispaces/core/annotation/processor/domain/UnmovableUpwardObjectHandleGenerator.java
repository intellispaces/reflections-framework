package tech.intellispaces.core.annotation.processor.domain;

import tech.intellispaces.commons.string.StringFunctions;
import tech.intellispaces.core.annotation.UnmovableObjectHandle;
import tech.intellispaces.core.common.NameConventionFunctions;
import tech.intellispaces.core.object.ObjectHandleTypes;
import tech.intellispaces.javastatements.customtype.CustomType;
import tech.intellispaces.javastatements.method.MethodParam;
import tech.intellispaces.javastatements.method.MethodStatement;
import tech.intellispaces.javastatements.reference.CustomTypeReference;
import tech.intellispaces.javastatements.type.Type;

import javax.annotation.processing.RoundEnvironment;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UnmovableUpwardObjectHandleGenerator extends AbstractDomainObjectHandleGenerator {
  private final CustomTypeReference baseDomainType;
  private String classTypeParams;
  private String classTypeParamsBrief;
  private String unmovableObjectHandleName;
  private String baseObjectHandleType;
  private String baseField;

  public UnmovableUpwardObjectHandleGenerator(CustomType annotatedType, CustomTypeReference baseDomainType) {
    super(annotatedType);
    this.baseDomainType = baseDomainType;
  }

  @Override
  public String getArtifactName() {
    return NameConventionFunctions.getUnmovableUpwardObjectHandleTypename(annotatedType, baseDomainType.targetType());
  }

  @Override
  protected ObjectHandleTypes getObjectHandleType() {
    return ObjectHandleTypes.Unmovable;
  }

  @Override
  protected String templateName() {
    return "/unmovable_upward_object_handle.template";
  }

  @Override
  protected Map<String, Object> templateVariables() {
    Map<String, Object> vars = new HashMap<>();
    vars.put("generatedAnnotation", makeGeneratedAnnotation());
    vars.put("packageName", context.packageName());
    vars.put("sourceClassName", sourceClassCanonicalName());
    vars.put("sourceClassSimpleName", sourceClassSimpleName());
    vars.put("classSimpleName", context.generatedClassSimpleName());
    vars.put("classTypeParams", classTypeParams);
    vars.put("classTypeParamsBrief", classTypeParamsBrief);
    vars.put("baseObjectHandleType", baseObjectHandleType);
    vars.put("baseField", baseField);
    vars.put("methods", methods);
    vars.put("importedClasses", context.getImports());
    vars.put("unmovableObjectHandleName", unmovableObjectHandleName);
    return vars;
  }

  @Override
  protected boolean analyzeAnnotatedType(RoundEnvironment roundEnv) {
    context.generatedClassCanonicalName(getArtifactName());
    context.addImport(Type.class);
    context.addImport(UnmovableObjectHandle.class);
    unmovableObjectHandleName = context.addToImportAndGetSimpleName(
        NameConventionFunctions.getUnmovableObjectHandleTypename(annotatedType.className()));

    classTypeParams = annotatedType.typeParametersFullDeclaration();
    classTypeParamsBrief = annotatedType.typeParametersBriefDeclaration();
    baseField = StringFunctions.lowercaseFirstLetter(baseDomainType.targetType().simpleName());
    baseObjectHandleType = getParentObjectHandleType();

    analyzeObjectHandleMethods(annotatedType, roundEnv);
    return true;
  }

  @Override
  protected Stream<MethodStatement> getObjectHandleMethods(
      CustomType customType, RoundEnvironment roundEnv
  ) {
    return buildActualDomain(customType, roundEnv)
        .actualMethods().stream()
        .filter(this::isNotGetDomainMethod);
  }

  @Override
  protected Map<String, String> buildMethod(MethodStatement method) {
    if (method.name().equals("as" + baseDomainType.targetType().simpleName())) {
      return buildConvertMethod(method);
    } else {
      return buildNormalMethod(method);
    }
  }

  private Map<String, String> buildConvertMethod(MethodStatement method) {
    var sb = new StringBuilder();
    sb.append("public ");
    appendMethodTypeParameters(sb, method);
    appendMethodReturnHandleType(sb, method);
    sb.append(" ");
    sb.append(method.name());
    sb.append("(");
    appendMethodParameters(sb, method);
    sb.append(")");
    appendMethodExceptions(sb, method);
    sb.append(" {\n");
    sb.append("  return (");
    appendMethodReturnHandleType(sb, method);
    sb.append(")  this.").append(baseField).append(";\n");
    sb.append("}\n");
    return Map.of("declaration", sb.toString());
  }

  private Map<String, String> buildNormalMethod(MethodStatement method) {
    var sb = new StringBuilder();
    sb.append("public ");
    appendMethodTypeParameters(sb, method);
    appendMethodReturnHandleType(sb, method);
    sb.append(" ");
    sb.append(method.name());
    sb.append("(");
    appendMethodParameters(sb, method);
    sb.append(")");
    appendMethodExceptions(sb, method);
    sb.append(" {\n");
    sb.append("  return (");
    appendMethodReturnHandleType(sb, method);
    sb.append(") this.").append(baseField).append(".");
    sb.append(method.name());
    sb.append("(");
    sb.append(method.params().stream()
        .map(MethodParam::name)
        .collect(Collectors.joining(", ")));
    sb.append(");\n");
    sb.append("}\n");
    return Map.of("declaration", sb.toString());
  }

  private String getParentObjectHandleType() {
    return context.addToImportAndGetSimpleName(
        NameConventionFunctions.getUnmovableObjectHandleTypename(baseDomainType.targetType().className())
    ) + annotatedType.parentTypes().get(0).typeArgumentsDeclaration(context::addToImportAndGetSimpleName);
  }
}
