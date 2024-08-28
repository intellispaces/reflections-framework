package intellispaces.core.annotation.processor.domain;

import intellispaces.commons.string.StringFunctions;
import intellispaces.core.annotation.ObjectHandle;
import intellispaces.core.common.NameConventionFunctions;
import intellispaces.core.object.ObjectHandleTypes;
import intellispaces.javastatements.customtype.CustomType;
import intellispaces.javastatements.method.MethodParam;
import intellispaces.javastatements.method.MethodStatement;
import intellispaces.javastatements.reference.CustomTypeReference;
import intellispaces.javastatements.type.Type;

import javax.annotation.processing.RoundEnvironment;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UnmovableUpwardObjectHandleGenerator extends AbstractDomainObjectHandleGenerator {
  private final CustomTypeReference baseDomainType;
  private final List<CustomTypeReference> allPrimaryDomains;
  private final Set<String> allUpwardMethodNames;
  private String classTypeParams;
  private String classTypeParamsBrief;
  private String unmovableObjectHandleName;
  private String baseObjectHandleType;
  private String baseField;

  public UnmovableUpwardObjectHandleGenerator(
      CustomType annotatedType,
      CustomTypeReference baseDomainType,
      List<CustomTypeReference> allPrimaryDomains
  ) {
    super(annotatedType);
    this.baseDomainType = baseDomainType;
    this.allPrimaryDomains = allPrimaryDomains;
    this.allUpwardMethodNames = allPrimaryDomains.stream()
        .map(NameConventionFunctions::getConversionMethodName)
        .collect(Collectors.toSet());
  }

  @Override
  public String getArtifactName() {
    return NameConventionFunctions.getUnmovableUpwardObjectHandleTypename(
        annotatedType, baseDomainType.targetType()
    );
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
    context.addImport(ObjectHandle.class);
    unmovableObjectHandleName = context.addToImportAndGetSimpleName(
        NameConventionFunctions.getUnmovableObjectHandleTypename(annotatedType.className()));

    classTypeParams = annotatedType.typeParametersFullDeclaration();
    classTypeParamsBrief = annotatedType.typeParametersBriefDeclaration();
    baseField = StringFunctions.lowercaseFirstLetter(
        StringFunctions.replaceEndingOrElseThrow(baseDomainType.targetType().simpleName(), "Domain", "")
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
