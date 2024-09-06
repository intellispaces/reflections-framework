package intellispaces.framework.core.annotation.processor.domain;

import intellispaces.common.annotationprocessor.context.AnnotationProcessingContext;
import intellispaces.common.base.text.TextFunctions;
import intellispaces.framework.core.annotation.ObjectHandle;
import intellispaces.framework.core.common.NameConventionFunctions;
import intellispaces.framework.core.object.ObjectHandleTypes;
import intellispaces.common.javastatement.customtype.CustomType;
import intellispaces.common.javastatement.method.MethodParam;
import intellispaces.common.javastatement.method.MethodStatement;
import intellispaces.common.javastatement.reference.CustomTypeReference;
import intellispaces.common.javastatement.type.Type;

import javax.annotation.processing.RoundEnvironment;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UnmovableUpwardObjectHandleGenerationTask extends AbstractDomainObjectHandleGenerationTask {
  private final CustomTypeReference baseDomainType;
  private final List<CustomTypeReference> allPrimaryDomains;
  private final Set<String> allUpwardMethodNames;
  private String classTypeParams;
  private String classTypeParamsBrief;
  private String unmovableObjectHandleName;
  private String baseObjectHandleType;
  private String baseField;

  public UnmovableUpwardObjectHandleGenerationTask(
      CustomType initiatorType,
      CustomType annotatedType,
      CustomTypeReference baseDomainType,
      List<CustomTypeReference> allPrimaryDomains
  ) {
    super(initiatorType, annotatedType);
    this.baseDomainType = baseDomainType;
    this.allPrimaryDomains = allPrimaryDomains;
    this.allUpwardMethodNames = allPrimaryDomains.stream()
        .map(NameConventionFunctions::getConversionMethodName)
        .collect(Collectors.toSet());
  }

  @Override
  public boolean isRelevant(AnnotationProcessingContext context) {
    return true;
  }

  @Override
  protected ObjectHandleTypes getObjectHandleType() {
    return ObjectHandleTypes.Unmovable;
  }

  @Override
  public String artifactName() {
    return NameConventionFunctions.getUnmovableUpwardObjectHandleTypename(
        annotatedType, baseDomainType.targetType()
    );
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
    context.generatedClassCanonicalName(artifactName());
    context.addImport(Type.class);
    context.addImport(ObjectHandle.class);
    unmovableObjectHandleName = context.addToImportAndGetSimpleName(
        NameConventionFunctions.getUnmovableObjectHandleTypename(annotatedType.className()));

    classTypeParams = annotatedType.typeParametersFullDeclaration();
    classTypeParamsBrief = annotatedType.typeParametersBriefDeclaration();
    baseField = TextFunctions.lowercaseFirstLetter(
        TextFunctions.replaceEndingOrElseThrow(baseDomainType.targetType().simpleName(), "Domain", "")
    );
    baseObjectHandleType = getParentObjectHandleType();

    analyzeObjectHandleMethods(annotatedType, roundEnv);
    return true;
  }

  @Override
  protected Stream<MethodStatement> getObjectHandleMethods(
      CustomType customType, RoundEnvironment roundEnv
  ) {
    return buildActualType(customType, roundEnv)
        .actualMethods().stream()
        .filter(this::isNotGetDomainMethod);
  }

  @Override
  protected Map<String, String> buildMethod(MethodStatement method) {
    if (allUpwardMethodNames.contains(method.name())) {
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
