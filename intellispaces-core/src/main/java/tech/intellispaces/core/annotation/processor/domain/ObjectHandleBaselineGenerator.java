package tech.intellispaces.core.annotation.processor.domain;

import tech.intellispaces.actions.Executor;
import tech.intellispaces.actions.string.StringActions;
import tech.intellispaces.core.annotation.ObjectHandleBaseline;
import tech.intellispaces.core.common.NameConventionFunctions;
import tech.intellispaces.core.object.ObjectHandleTypes;
import tech.intellispaces.core.space.SpaceConstants;
import tech.intellispaces.core.space.domain.DomainFunctions;
import tech.intellispaces.javastatements.customtype.CustomType;
import tech.intellispaces.javastatements.method.MethodStatement;
import tech.intellispaces.javastatements.reference.CustomTypeReference;

import javax.annotation.processing.RoundEnvironment;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class ObjectHandleBaselineGenerator extends AbstractDomainObjectHandleGenerator {
  private String extendDeclaration;

  public ObjectHandleBaselineGenerator(CustomType domainType) {
    super(domainType);
  }

  @Override
  public String getArtifactName() {
    return NameConventionFunctions.getObjectHandleBaselineTypename(annotatedType.className());
  }

  @Override
  protected ObjectHandleTypes getObjectHandleType() {
    return ObjectHandleTypes.Common;
  }

  @Override
  protected String templateName() {
    return "/object_handle_baseline.template";
  }

  protected Map<String, Object> templateVariables() {
    Map<String, Object> vars = new HashMap<>();
    vars.put("generatedAnnotation", makeGeneratedAnnotation());
    vars.put("packageName", context.packageName());
    vars.put("sourceClassName", sourceClassCanonicalName());
    vars.put("sourceClassSimpleName", sourceClassSimpleName());
    vars.put("classSimpleName", context.generatedClassSimpleName());
    vars.put("movableClassSimpleName", movableClassSimpleName());
    vars.put("domainTypeParamsFull", domainTypeParamsFull);
    vars.put("domainTypeParamsBrief", domainTypeParamsBrief);
    vars.put("extendDeclaration", extendDeclaration);
    vars.put("domainMethods", methods);
    vars.put("importedClasses", context.getImports());
    return vars;
  }

  @Override
  protected boolean analyzeAnnotatedType(RoundEnvironment roundEnv) {
    context.generatedClassCanonicalName(getArtifactName());
    context.addImport(sourceClassCanonicalName());
    context.addImport(ObjectHandleBaseline.class);

    domainTypeParamsFull = annotatedType.typeParametersFullDeclaration();
    domainTypeParamsBrief = annotatedType.typeParametersBriefDeclaration();
    extendDeclaration = buildExtendDeclaration();
    analyzeObjectHandleMethods(annotatedType, roundEnv);
    return true;
  }

  private String buildExtendDeclaration() {
    List<CustomTypeReference> parents = annotatedType.parentTypes().stream()
        .filter(DomainFunctions::isDomainType)
        .toList();
    if (parents.isEmpty()) {
      return "";
    }

    var sb = new StringBuilder();
    sb.append("extends ");
    Executor commaAppender = StringActions.commaAppender(sb);
    for (CustomTypeReference parent : parents) {
      commaAppender.execute();
      sb.append(context.addToImportAndGetSimpleName(
          NameConventionFunctions.getObjectHandleBaselineTypename(parent.targetType().className()))
      );
      sb.append(parent.typeArgumentsDeclaration(this::typeConverter));
    }
    return sb.toString();
  }
}
