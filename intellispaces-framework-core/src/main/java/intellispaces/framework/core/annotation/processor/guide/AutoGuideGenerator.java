package intellispaces.framework.core.annotation.processor.guide;

import intellispaces.common.action.Action;
import intellispaces.common.action.Actions;
import intellispaces.common.action.runner.Runner;
import intellispaces.common.annotationprocessor.context.AnnotationProcessingContext;
import intellispaces.common.base.text.TextActions;
import intellispaces.common.base.text.TextFunctions;
import intellispaces.common.javastatement.customtype.CustomType;
import intellispaces.common.javastatement.method.MethodParam;
import intellispaces.common.javastatement.method.MethodStatement;
import intellispaces.common.javastatement.reference.TypeReference;
import intellispaces.common.javastatement.type.Types;
import intellispaces.framework.core.action.TraverseActions;
import intellispaces.framework.core.annotation.processor.AbstractGenerator;
import intellispaces.framework.core.annotation.processor.GuideProcessorFunctions;
import intellispaces.framework.core.common.NameConventionFunctions;
import intellispaces.framework.core.guide.GuideForm;
import intellispaces.framework.core.guide.GuideForms;
import intellispaces.framework.core.guide.GuideFunctions;

import javax.annotation.processing.RoundEnvironment;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutoGuideGenerator extends AbstractGenerator {
  private List<Map<String, Object>> guideMethods;
  private String typeParamsFullDeclaration;
  private String typeParamsBriefDeclaration;

  public AutoGuideGenerator(CustomType initiatorType, CustomType annotatedType) {
    super(initiatorType, annotatedType);
  }

  @Override
  public boolean isRelevant(AnnotationProcessingContext context) {
    return true;
  }

  @Override
  public String artifactName() {
    return NameConventionFunctions.getAutoGuiderCanonicalName(annotatedType.className());
  }

  @Override
  protected String templateName() {
    return "/auto_guide.template";
  }

  @Override
  protected Map<String, Object> templateVariables() {
    Map<String, Object> vars = new HashMap<>();
    vars.put("generatedAnnotation", makeGeneratedAnnotation());
    vars.put("packageName", context.packageName());
    vars.put("sourceClassName", sourceClassCanonicalName());
    vars.put("sourceClassSimpleName", sourceClassSimpleName());
    vars.put("classSimpleName", context.generatedClassSimpleName());
    vars.put("importedClasses", context.getImports());
    vars.put("typeParamsFullDeclaration", typeParamsFullDeclaration);
    vars.put("typeParamsBriefDeclaration", typeParamsBriefDeclaration);
    vars.put("guideMethods", guideMethods);
    return vars;
  }

  @Override
  protected boolean analyzeAnnotatedType(RoundEnvironment roundEnv) {
    context.generatedClassCanonicalName(artifactName());
    if (annotatedType.isNested()) {
      context.addImport(sourceClassCanonicalName());
    }
    context.addImport(Action.class);
    context.addImport(GuideForms.class);

    analyzeTypeParams();
    analyzeGuideMethods();
    return true;
  }

  private void analyzeTypeParams() {
    typeParamsFullDeclaration = annotatedType.typeParametersFullDeclaration();
    typeParamsBriefDeclaration = annotatedType.typeParametersBriefDeclaration();
  }

  private void analyzeGuideMethods() {
    guideMethods = annotatedType.declaredMethods().stream()
        .filter(m -> !m.isDefault())
        .map(this::buildMethodMap)
        .toList();
  }

  private Map<String, Object> buildMethodMap(MethodStatement method) {
    Map<String, Object> map = new HashMap<>();
    map.put("signature", buildMethodSignature(method));

    String actionName = buildActionName(method);
    map.put("actionName", buildActionName(method));

    map.put("body", buildMethodBody(method, actionName));
    map.put("actionDeclaration", buildActionDeclaration(method));
    return map;
  }

  private String buildActionName(MethodStatement method) {
    var sb = new StringBuilder();
    sb.append(method.name());
    for (MethodParam param : method.params()) {
      if (param.type().isPrimitiveReference()) {
        sb.append(TextFunctions.capitalizeFirstLetter(param.type().asPrimitiveReferenceOrElseThrow().typename()));
      } else if (param.type().isNamedReference()) {
        sb.append(TextFunctions.capitalizeFirstLetter(param.type().asNamedReferenceOrElseThrow().name()));
      } else if (param.type().isCustomTypeReference()) {
        sb.append(TextFunctions.capitalizeFirstLetter(param.type().asCustomTypeReferenceOrElseThrow().targetType().simpleName()));
      } else {
        throw new RuntimeException("Not implemented");
      }
    }
    sb.append("Action");
    return sb.toString();
  }

  private String buildActionDeclaration(MethodStatement method) {
    TypeReference sourceType = method.params().get(0).type();

    var sb = new StringBuilder();
    sb.append(context.addToImportAndGetSimpleName(Actions.class));
    sb.append(".delegate");
    sb.append(method.params().size());
    sb.append("(");
    sb.append(context.addToImportAndGetSimpleName(Actions.class));
    sb.append(".cachedLazyGetter(");
    sb.append(context.addToImportAndGetSimpleName(TraverseActions.class));
    sb.append("::");
    if (GuideFunctions.isMapperMethod(method)) {
      sb.append("map");
    } else if (GuideFunctions.isMoverMethod(method)) {
      sb.append("move");
    } else if (GuideFunctions.isMapperOfMovingMethod(method)) {
      sb.append("mapOfMoving");
    }
    sb.append("ThruChannel");
    sb.append(method.params().size() - 1);
    sb.append(",\n");
    sb.append("    ");
    sb.append(context.addToImportAndGetSimpleName(Types.class));
    sb.append(".<");
    sb.append(sourceType.actualDeclaration(context::addToImportAndGetSimpleName));
    sb.append(", ");
    sb.append(sourceType.simpleDeclaration(context::addToImportAndGetSimpleName));
    sb.append("> of(");
    sb.append(sourceType.simpleDeclaration(context::addToImportAndGetSimpleName));
    sb.append(".class),\n    ");
    sb.append(context.addToImportAndGetSimpleName(GuideFunctions.getChannelType(method).canonicalName()));
    sb.append(".class,\n");
    GuideForm guideForm = GuideProcessorFunctions.getGuideForm(method);
    sb.append("GuideForms.");
    sb.append(guideForm.name());
    sb.append("))");
    return sb.toString();
  }

  private String buildMethodBody(MethodStatement method, String actionName) {
    var sb = new StringBuilder();
    if (method.returnType().isPresent()) {
      sb.append("return ");
      sb.append("(");
      sb.append(method.returnType().get().actualDeclaration(context::addToImportAndGetSimpleName));
      sb.append(") ");
    }
    sb.append(actionName);
    sb.append(".asAction");
    sb.append(method.params().size());
    sb.append("().execute(");
    Runner commaAppender = TextActions.skippingFirstTimeCommaAppender(sb);
    for (MethodParam param : method.params()) {
      commaAppender.run();
      sb.append(param.name());
    }
    sb.append(");");
    return sb.toString();
  }
}
