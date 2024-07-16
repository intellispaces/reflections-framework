package tech.intellispaces.framework.core.annotation.processor.domain;

import tech.intellispaces.framework.commons.string.StringFunctions;
import tech.intellispaces.framework.core.annotation.ObjectHandle;
import tech.intellispaces.framework.core.common.NameFunctions;
import tech.intellispaces.framework.core.object.ObjectHandleTypes;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;
import tech.intellispaces.framework.javastatements.statement.method.MethodParam;
import tech.intellispaces.framework.javastatements.statement.method.MethodStatement;
import tech.intellispaces.framework.javastatements.statement.reference.CustomTypeReference;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class UnmovableUpgradeObjectHandleGenerator extends AbstractDomainObjectHandleGenerator {
  private final CustomTypeReference baseDomainType;
  private String classTypeParams;
  private String unmovableObjectHandleName;
  private String baseObjectHandleType;
  private String baseField;

  public UnmovableUpgradeObjectHandleGenerator(CustomType annotatedType, CustomTypeReference baseDomainType) {
    super(annotatedType);
    this.baseDomainType = baseDomainType;
  }

  @Override
  protected ObjectHandleTypes getObjectHandleType() {
    return ObjectHandleTypes.Unmovable;
  }

  @Override
  protected String templateName() {
    return "/unmovable_upgrade_object_handle.template";
  }

  @Override
  protected Map<String, Object> templateVariables() {
    Map<String, Object> vars = new HashMap<>();
    vars.put("generatedAnnotation", generatedAnnotation());
    vars.put("packageName", context.packageName());
    vars.put("sourceClassName", sourceClassCanonicalName());
    vars.put("sourceClassSimpleName", sourceClassSimpleName());
    vars.put("classSimpleName", context.generatedClassSimpleName());
    vars.put("classTypeParams", classTypeParams);
    vars.put("baseObjectHandleType", baseObjectHandleType);
    vars.put("baseField", baseField);
    vars.put("methods", methods);
    vars.put("importedClasses", context.getImports());
    vars.put("unmovableObjectHandleName", unmovableObjectHandleName);
    return vars;
  }

  @Override
  protected boolean analyzeAnnotatedType() {
    context.generatedClassCanonicalName(
        NameFunctions.getUnmovableUpgradeObjectHandleTypename(annotatedType, baseDomainType.targetType()));

    context.addImport(ObjectHandle.class);

    unmovableObjectHandleName = context.addToImportAndGetSimpleName(
        NameFunctions.getUnmovableObjectHandleTypename(annotatedType.className()));

    classTypeParams = annotatedType.typeParametersFullDeclaration();
    baseField = StringFunctions.lowercaseFirstLetter(baseDomainType.targetType().simpleName());
    baseObjectHandleType = getBaseObjectHandleType();

    analyzeObjectHandleMethods(annotatedType);
    return true;
  }

  @Override
  protected String buildMethod(MethodStatement method) {
    if (method.name().equals("as" + baseDomainType.targetType().simpleName())) {
      return buildConvertMethod(method);
    } else {
      return buildNormalMethod(method);
    }
  }

  private String buildConvertMethod(MethodStatement method) {
    var sb = new StringBuilder();
    sb.append("public ");
    appendMethodTypeParameters(sb, method);
    appendMethodReturnType(sb, method);
    sb.append(" ");
    sb.append(method.name());
    sb.append("(");
    appendMethodParameters(sb, method);
    sb.append(")");
    appendMethodExceptions(sb, method);
    sb.append(" {\n");
    sb.append("  return (");
    appendMethodReturnType(sb, method);
    sb.append(")  this.").append(baseField).append(";\n");
    sb.append("}\n");
    return sb.toString();
  }

  private String buildNormalMethod(MethodStatement method) {
    var sb = new StringBuilder();
    sb.append("public ");
    appendMethodTypeParameters(sb, method);
    appendMethodReturnType(sb, method);
    sb.append(" ");
    sb.append(method.name());
    sb.append("(");
    appendMethodParameters(sb, method);
    sb.append(")");
    appendMethodExceptions(sb, method);
    sb.append(" {\n");
    sb.append("  return (");
    appendMethodReturnType(sb, method);
    sb.append(") this.").append(baseField).append(".");
    sb.append(method.name());
    sb.append("(");
    sb.append(method.params().stream()
        .map(MethodParam::name)
        .collect(Collectors.joining(", ")));
    sb.append(");\n");
    sb.append("}\n");
    return sb.toString();
  }

  private String getBaseObjectHandleType() {
    return context.addToImportAndGetSimpleName(
        NameFunctions.getUnmovableObjectHandleTypename(baseDomainType.targetType().className())
    ) + annotatedType.parentTypes().get(0).typeArgumentsDeclaration(context::addToImportAndGetSimpleName);
  }
}
