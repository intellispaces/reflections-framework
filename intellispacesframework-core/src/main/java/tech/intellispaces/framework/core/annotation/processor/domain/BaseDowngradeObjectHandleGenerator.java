package tech.intellispaces.framework.core.annotation.processor.domain;

import tech.intellispaces.framework.commons.string.StringFunctions;
import tech.intellispaces.framework.core.annotation.ObjectHandle;
import tech.intellispaces.framework.core.annotation.processor.ArtifactTypes;
import tech.intellispaces.framework.core.annotation.processor.PreprocessingAnnotationFunctions;
import tech.intellispaces.framework.core.common.NameConventionFunctions;
import tech.intellispaces.framework.core.exception.TraverseException;
import tech.intellispaces.framework.core.object.ObjectHandleTypes;
import tech.intellispaces.framework.core.transition.TransitionMethod0;
import tech.intellispaces.framework.core.transition.TransitionMethod1;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;
import tech.intellispaces.framework.javastatements.statement.instance.AnnotationInstance;
import tech.intellispaces.framework.javastatements.statement.method.MethodStatement;
import tech.intellispaces.framework.javastatements.statement.reference.CustomTypeReference;
import tech.intellispaces.framework.javastatements.statement.reference.CustomTypeReferences;

import javax.annotation.processing.RoundEnvironment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BaseDowngradeObjectHandleGenerator extends AbstractConversionDomainObjectHandleGenerator {
  private List<Map<String, String>> additionalMethods;
  private String classTypeParams;
  private String classTypeParamsBrief;
  private String baseObjectHandleName;
  private String movableObjectHandleName;
  private String childObjectHandleType;
  private String movableDowngradeObjectHandleName;

  public BaseDowngradeObjectHandleGenerator(CustomType annotatedType, CustomTypeReference parentDomainType) {
    super(annotatedType, parentDomainType);
  }

  @Override
  public String getArtifactName() {
    return NameConventionFunctions.getBaseDowngradeObjectHandleTypename(annotatedType, parentDomainType.targetType());
  }

  @Override
  protected ObjectHandleTypes getObjectHandleType() {
    return ObjectHandleTypes.Base;
  }

  @Override
  protected String templateName() {
    return "/base_downgrade_object_handle.template";
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
    vars.put("childObjectHandleType", childObjectHandleType);
    vars.put("childField", childFieldName);
    vars.put("methods", methods);
    vars.put("additionalMethods", additionalMethods);
    vars.put("importedClasses", context.getImports());
    vars.put("baseObjectHandleName", baseObjectHandleName);
    vars.put("movableObjectHandleName", movableObjectHandleName);
    vars.put("movableDowngradeObjectHandleName", movableDowngradeObjectHandleName);
    return vars;
  }

  @Override
  protected boolean analyzeAnnotatedType(RoundEnvironment roundEnv) {
    context.generatedClassCanonicalName(getArtifactName());

    context.addImport(ObjectHandle.class);
    context.addImport(TraverseException.class);
    context.addImport(TransitionMethod0.class);
    context.addImport(TransitionMethod1.class);

    baseObjectHandleName = context.addToImportAndGetSimpleName(
        NameConventionFunctions.getBaseObjectHandleTypename(parentDomainType.targetType().canonicalName()));
    movableObjectHandleName = context.addToImportAndGetSimpleName(
        NameConventionFunctions.getMovableObjectHandleTypename(parentDomainType.targetType().canonicalName()));

    classTypeParams = annotatedType.typeParametersFullDeclaration();
    classTypeParamsBrief = parentDomainType.typeArgumentsDeclaration(context::addToImportAndGetSimpleName);
    childFieldName = StringFunctions.lowercaseFirstLetter(annotatedType.simpleName());
    childObjectHandleType = getChildObjectHandleType();

    movableDowngradeObjectHandleName = NameConventionFunctions.getMovableDowngradeObjectHandleTypename(
        annotatedType, parentDomainType.targetType()
    );

    analyzeObjectHandleMethods(parentDomainType.effectiveTargetType(), roundEnv);
    analyzeAdditionalMethods(roundEnv);
    return true;
  }

  private void analyzeAdditionalMethods(RoundEnvironment roundEnv) {
    List<AnnotationInstance> preprocessingAnnotations = PreprocessingAnnotationFunctions.findPreprocessingAnnotations(
            parentDomainType.targetType(), ArtifactTypes.ObjectHandle, roundEnv
    );
    additionalMethods = new ArrayList<>();
    for (AnnotationInstance preprocessingAnnotation : preprocessingAnnotations) {
      Optional<CustomType> extensionType = PreprocessingAnnotationFunctions.getPreprocessingExtendWith(
          preprocessingAnnotation
      );
      extensionType.ifPresent(type -> CustomTypeReferences.of(type, parentDomainType.typeArguments())
          .effectiveTargetType()
          .declaredMethods().stream()
          .map(this::buildAdditionalMethod)
          .forEach(additionalMethods::add));
    }
  }

  @Override
  protected Map<String, String> buildMethod(MethodStatement method) {
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
    sb.append("    ");
    buildReturnStatement(sb, method);
    sb.append("\n}\n");
    return Map.of(
        "javadoc", buildGeneratedMethodJavadoc(method.owner().canonicalName(), method.name()),
        "declaration", sb.toString()
    );
  }

  private Map<String, String> buildAdditionalMethod(MethodStatement method) {
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
    sb.append("  return this.")
        .append(childFieldName)
        .append(".")
        .append(NameConventionFunctions.getConversionMethodName(parentDomainType.targetType()))
        .append("().")
        .append(method.name())
        .append("();\n");
    sb.append("}");
    return Map.of(
        "javadoc", buildGeneratedMethodJavadoc(method.owner().canonicalName(), method.name()),
        "declaration", sb.toString()
      );
  }

  private String getChildObjectHandleType() {
    return context.addToImportAndGetSimpleName(
        NameConventionFunctions.getBaseObjectHandleTypename(annotatedType.className())
    );
  }
}
