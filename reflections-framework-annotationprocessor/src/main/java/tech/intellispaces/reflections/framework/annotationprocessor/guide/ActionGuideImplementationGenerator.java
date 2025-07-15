package tech.intellispaces.reflections.framework.annotationprocessor.guide;

import java.util.List;
import java.util.Map;

import tech.intellispaces.actions.Action;
import tech.intellispaces.actions.cache.CachedSupplierActions;
import tech.intellispaces.actions.delegate.DelegateActions;
import tech.intellispaces.actions.runnable.RunnableAction;
import tech.intellispaces.actions.text.StringActions;
import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.commons.text.StringFunctions;
import tech.intellispaces.commons.type.Types;
import tech.intellispaces.javareflection.customtype.CustomType;
import tech.intellispaces.javareflection.method.MethodParam;
import tech.intellispaces.javareflection.method.MethodStatement;
import tech.intellispaces.javareflection.reference.TypeReference;
import tech.intellispaces.reflections.framework.action.TraverseActions;
import tech.intellispaces.reflections.framework.annotationprocessor.ReflectionsArtifactGenerator;
import tech.intellispaces.reflections.framework.guide.GuideFunctions;
import tech.intellispaces.reflections.framework.naming.NameConventionFunctions;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.reflection.ReflectionForms;

public class ActionGuideImplementationGenerator extends ReflectionsArtifactGenerator {
  private String typeParamsFullDeclaration;
  private String typeParamsBriefDeclaration;
  private List<Map<String, String>> guides;

  public ActionGuideImplementationGenerator(CustomType guideType) {
    super(guideType);
  }

  @Override
  public boolean isRelevant(ArtifactGeneratorContext context) {
    return true;
  }

  @Override
  public String generatedArtifactName() {
    return NameConventionFunctions.getActionGuideImplementationCanonicalName(sourceArtifact().className());
  }

  @Override
  protected String templateName() {
    return "/action_guide_impl.template";
  }

  @Override
  protected boolean analyzeSourceArtifact(ArtifactGeneratorContext context) {
    addImports(
        Action.class,
        ReflectionForms.class
    );

    analyzeTypeParams();
    analyzeGuideMethod();

    addVariable("typeParamsFullDeclaration", typeParamsFullDeclaration);
    addVariable("typeParamsBriefDeclaration", typeParamsBriefDeclaration);
    addVariable("guides", guides);
    return true;
  }

  private void analyzeTypeParams() {
    typeParamsFullDeclaration = sourceArtifact().typeParametersFullDeclaration();
    typeParamsBriefDeclaration = sourceArtifact().typeParametersBriefDeclaration();
  }

  private void analyzeGuideMethod() {
    guides = sourceArtifact().declaredMethods().stream()
        .filter(m -> !m.isDefault())
        .map(this::buildGuideMethod)
        .toList();
  }

  private Map<String, String> buildGuideMethod(MethodStatement guideMethod) {
    String actionName = buildActionName(guideMethod);
    String autoActionDeclaration = buildAutoActionDeclaration(guideMethod);
    String methodSignature = buildMethodSignature(guideMethod);
    String methodBody = buildGuideMethodBody(guideMethod, actionName);
    return Map.of(
        "actionName", actionName,
        "autoActionDeclaration", autoActionDeclaration,
        "methodSignature", methodSignature,
        "methodBody", methodBody
    );
  }

  private String buildActionName(MethodStatement guideMethod) {
    var sb = new StringBuilder();
    sb.append(guideMethod.name());
    for (MethodParam param : guideMethod.params()) {
      if (param.type().isPrimitiveReference()) {
        sb.append(StringFunctions.capitalizeFirstLetter(param.type().asPrimitiveReferenceOrElseThrow().typename()));
      } else if (param.type().isNamedReference()) {
        sb.append(StringFunctions.capitalizeFirstLetter(param.type().asNamedReferenceOrElseThrow().name()));
      } else if (param.type().isCustomTypeReference()) {
        sb.append(StringFunctions.capitalizeFirstLetter(param.type().asCustomTypeReferenceOrElseThrow().targetType().simpleName()));
      } else {
        throw NotImplementedExceptions.withCode("MaJAcQ");
      }
    }
    sb.append("Action");
    return sb.toString();
  }

  private String buildAutoActionDeclaration(MethodStatement guideMethod) {
    TypeReference sourceType = guideMethod.params().get(0).type();

    var sb = new StringBuilder();
    sb.append(addImportAndGetSimpleName(DelegateActions.class));
    sb.append(".delegateAction");
    sb.append(guideMethod.params().size());
    sb.append("(");
    sb.append(addImportAndGetSimpleName(CachedSupplierActions.class));
    sb.append(".get(");
    sb.append(addImportAndGetSimpleName(TraverseActions.class));
    sb.append("::");
    if (GuideFunctions.isMapperMethod(guideMethod)) {
      sb.append("map");
    } else if (GuideFunctions.isMoverMethod(guideMethod)) {
      sb.append("move");
    } else if (GuideFunctions.isMapperOfMovingMethod(guideMethod)) {
      sb.append("mapOfMoving");
    }
    sb.append("ThruChannel");
    sb.append(guideMethod.params().size() - 1);
    sb.append(",\n");
    sb.append("    ");
    sb.append(addImportAndGetSimpleName(Types.class));
    sb.append(".<");
    sb.append(sourceType.actualBlindDeclaration(this::addImportAndGetSimpleName));
    sb.append(", ");
    sb.append(sourceType.simpleDeclaration(this::addImportAndGetSimpleName));
    sb.append("> get(");
    sb.append(sourceType.simpleDeclaration(this::addImportAndGetSimpleName));
    sb.append(".class),\n    ");
    sb.append(addImportAndGetSimpleName(GuideFunctions.getChannelType(guideMethod).canonicalName()));
    sb.append(".class,\n");
    ReflectionForm targetForm = GuideFunctions.getTargetForm(guideMethod);
    sb.append("    ").append(addImportAndGetSimpleName(ReflectionForms.class)).append(".");
    sb.append(targetForm.name());
    sb.append("))");
    return sb.toString();
  }

  private String buildGuideMethodBody(MethodStatement guideMethod, String actionName) {
    var sb = new StringBuilder();
    if (guideMethod.returnType().isPresent()) {
      sb.append("return ");
      sb.append("(");
      sb.append(guideMethod.returnType().get().actualDeclaration(this::addImportAndGetSimpleName));
      sb.append(") ");
    }
    sb.append(actionName);
    sb.append(".castToAction");
    sb.append(guideMethod.params().size());
    sb.append("().execute(");
    RunnableAction commaAppender = StringActions.skipFirstTimeCommaAppender(sb);
    for (MethodParam param : guideMethod.params()) {
      commaAppender.run();
      sb.append(param.name());
    }
    sb.append(");");
    return sb.toString();
  }
}
