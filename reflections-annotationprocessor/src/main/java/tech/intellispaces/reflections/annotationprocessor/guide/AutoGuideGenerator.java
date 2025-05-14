package tech.intellispaces.reflections.annotationprocessor.guide;

import java.util.HashMap;
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
import tech.intellispaces.reflections.framework.action.TraverseActions;
import tech.intellispaces.reflections.annotationprocessor.ReflectionsArtifactGenerator;
import tech.intellispaces.reflections.framework.guide.GuideFunctions;
import tech.intellispaces.reflections.framework.naming.NameConventionFunctions;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.reflection.ReflectionForms;
import tech.intellispaces.jstatements.customtype.CustomType;
import tech.intellispaces.jstatements.method.MethodParam;
import tech.intellispaces.jstatements.method.MethodStatement;
import tech.intellispaces.jstatements.reference.TypeReference;

public class AutoGuideGenerator extends ReflectionsArtifactGenerator {
  private List<Map<String, Object>> guideMethods;
  private String typeParamsFullDeclaration;
  private String typeParamsBriefDeclaration;

  public AutoGuideGenerator(CustomType guideType) {
    super(guideType);
  }

  @Override
  public boolean isRelevant(ArtifactGeneratorContext context) {
    return true;
  }

  @Override
  public String generatedArtifactName() {
    return NameConventionFunctions.getAutoGuiderCanonicalName(sourceArtifact().className());
  }

  @Override
  protected String templateName() {
    return "/auto_guide.template";
  }

  @Override
  protected boolean analyzeSourceArtifact(ArtifactGeneratorContext context) {
    addImports(
        Action.class,
        ReflectionForms.class
    );

    analyzeTypeParams();
    analyzeGuideMethods();

    addVariable("typeParamsFullDeclaration", typeParamsFullDeclaration);
    addVariable("typeParamsBriefDeclaration", typeParamsBriefDeclaration);
    addVariable("guideMethods", guideMethods);
    return true;
  }

  private void analyzeTypeParams() {
    typeParamsFullDeclaration = sourceArtifact().typeParametersFullDeclaration();
    typeParamsBriefDeclaration = sourceArtifact().typeParametersBriefDeclaration();
  }

  private void analyzeGuideMethods() {
    guideMethods = sourceArtifact().declaredMethods().stream()
        .filter(m -> !m.isDefault())
        .map(this::buildMethodMap)
        .toList();
  }

  private Map<String, Object> buildMethodMap(MethodStatement method) {
    Map<String, Object> map = new HashMap<>();
    map.put("signature", buildMethodSignature(method));

    String actionName = buildActionName(method);
    map.put("actionName", actionName);
    map.put("actionDeclaration", buildActionDeclaration(method));
    map.put("body", buildMethodBody(method, actionName));
    return map;
  }

  private String buildActionName(MethodStatement method) {
    var sb = new StringBuilder();
    sb.append(method.name());
    for (MethodParam param : method.params()) {
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

  private String buildActionDeclaration(MethodStatement method) {
    TypeReference sourceType = method.params().get(0).type();

    var sb = new StringBuilder();
    sb.append(addImportAndGetSimpleName(DelegateActions.class));
    sb.append(".delegateAction");
    sb.append(method.params().size());
    sb.append("(");
    sb.append(addImportAndGetSimpleName(CachedSupplierActions.class));
    sb.append(".get(");
    sb.append(addImportAndGetSimpleName(TraverseActions.class));
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
    sb.append(addImportAndGetSimpleName(Types.class));
    sb.append(".<");
    sb.append(sourceType.actualBlindDeclaration(this::addImportAndGetSimpleName));
    sb.append(", ");
    sb.append(sourceType.simpleDeclaration(this::addImportAndGetSimpleName));
    sb.append("> get(");
    sb.append(sourceType.simpleDeclaration(this::addImportAndGetSimpleName));
    sb.append(".class),\n    ");
    sb.append(addImportAndGetSimpleName(GuideFunctions.getChannelType(method).canonicalName()));
    sb.append(".class,\n");
    ReflectionForm targetForm = GuideFunctions.getTargetForm(method);
    sb.append("    ").append(addImportAndGetSimpleName(ReflectionForms.class)).append(".");
    sb.append(targetForm.name());
    sb.append("))");
    return sb.toString();
  }

  private String buildMethodBody(MethodStatement method, String actionName) {
    var sb = new StringBuilder();
    if (method.returnType().isPresent()) {
      sb.append("return ");
      sb.append("(");
      sb.append(method.returnType().get().actualDeclaration(this::addImportAndGetSimpleName));
      sb.append(") ");
    }
    sb.append(actionName);
    sb.append(".castToAction");
    sb.append(method.params().size());
    sb.append("().execute(");
    RunnableAction commaAppender = StringActions.skipFirstTimeCommaAppender(sb);
    for (MethodParam param : method.params()) {
      commaAppender.run();
      sb.append(param.name());
    }
    sb.append(");");
    return sb.toString();
  }
}
