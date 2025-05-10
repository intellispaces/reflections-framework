package tech.intellispaces.reflections.annotationprocessor.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.commons.text.StringFunctions;
import tech.intellispaces.commons.type.Type;
import tech.intellispaces.reflections.framework.annotation.Dataset;
import tech.intellispaces.reflections.annotationprocessor.AnnotationFunctions;
import tech.intellispaces.reflections.annotationprocessor.JaquariusArtifactGenerator;
import tech.intellispaces.reflections.framework.artifact.ArtifactTypes;
import tech.intellispaces.reflections.framework.naming.NameConventionFunctions;
import tech.intellispaces.reflections.framework.object.reference.MovabilityTypes;
import tech.intellispaces.reflections.framework.object.reference.ObjectReferenceForms;
import tech.intellispaces.reflections.framework.object.reference.ObjectReferenceFunctions;
import tech.intellispaces.reflections.framework.system.Modules;
import tech.intellispaces.jstatements.customtype.CustomType;
import tech.intellispaces.jstatements.method.MethodParam;
import tech.intellispaces.jstatements.method.MethodSignatureDeclarations;
import tech.intellispaces.jstatements.method.MethodStatement;
import tech.intellispaces.jstatements.reference.CustomTypeReference;
import tech.intellispaces.jstatements.reference.NamedReference;
import tech.intellispaces.jstatements.reference.NotPrimitiveReference;
import tech.intellispaces.jstatements.reference.TypeReference;

public class ObjectAssistantGenerator extends JaquariusArtifactGenerator {

  public ObjectAssistantGenerator(CustomType domainType) {
    super(domainType);
  }

  @Override
  protected String templateName() {
    return "/object_assistant.template";
  }

  @Override
  public String generatedArtifactName() {
    return NameConventionFunctions.getObjectAssistantCanonicalName(sourceArtifact());
  }

  @Override
  public boolean isRelevant(ArtifactGeneratorContext context) {
    // Starts the Object Assistant generator after all other types of generators
    return context.isPenaltyRound();
  }

  @Override
  protected boolean analyzeSourceArtifact(ArtifactGeneratorContext context) {
    addImport(Modules.class);
    addImport(UnexpectedExceptions.class);

    addVariable("brokerSimpleName", addImportAndGetSimpleName(NameConventionFunctions.getObjectAssistantBrokerCanonicalName(sourceArtifact())));
    addVariable("isDataset", AnnotationFunctions.isAssignableAnnotation(sourceArtifact(), Dataset.class));
    addVariable("regularFirstLetterLowercaseSimpleName",
        StringFunctions.lowercaseFirstLetter(StringFunctions.removeTailOrElseThrow(sourceArtifactSimpleName(), "Domain")));
    addVariable("datasetBuilderSimpleName",
        addImportAndGetSimpleName(NameConventionFunctions.getDatasetBuilderCanonicalName(sourceArtifact().className())));
    addVariable("generalObjectHandleSimpleName",
        addImportAndGetSimpleName(ObjectReferenceFunctions.getGeneralObjectHandleTypename(sourceArtifact())));
    addTypeParamVariables();
    addVariable("customizerMethods", getCustomizerMethods(context));
    return true;
  }

  void addTypeParamVariables() {
    addVariable("typeParamsFull", ObjectReferenceFunctions.getObjectFormTypeParamDeclaration(
        sourceArtifact(),
        ObjectReferenceForms.Regular,
        MovabilityTypes.General,
        this::addImportAndGetSimpleName,
        true,
        true));
    addVariable("typeParamsBrief", ObjectReferenceFunctions.getObjectFormTypeParamDeclaration(
        sourceArtifact(),
        ObjectReferenceForms.Regular,
        MovabilityTypes.General,
        this::addImportAndGetSimpleName,
        true,
        false));
    addVariable("typeParams", makeTypeParams());
  }

  List<Map<String, String>> makeTypeParams() {
    var params = new ArrayList<Map<String, String>>();
    for (NamedReference namedReference : sourceArtifact().typeParameters()) {
      String projectionName = findProjectionName(namedReference.name());
      if (projectionName != null) {
        params.add(Map.of(
            "type", namedReference.name(),
            "name", projectionName));
      }
    }
    return params;
  }

  String findProjectionName(String typeParamName) {
    for (MethodStatement method : sourceArtifact().declaredMethods()) {
      TypeReference returnType = method.returnType().orElseThrow();
      if (returnType.isCustomTypeReference()) {
        CustomTypeReference customTypeReference = returnType.asCustomTypeReferenceOrElseThrow();
        if (Type.class.getCanonicalName().equals(customTypeReference.targetType().canonicalName())) {
          if (customTypeReference.typeArguments().size() == 1) {
            NotPrimitiveReference typeArgument = customTypeReference.typeArguments().get(0);
            if (typeArgument.isNamedReference() && typeParamName.equals(typeArgument.asNamedReference().orElseThrow().name())) {
              return method.name();
            }
          }
        }
      }
    }
    return null;
  }

  List<Map<String, Object>> getCustomizerMethods(ArtifactGeneratorContext context) {
    var methods = new ArrayList<Map<String, Object>>();
    Collection<CustomType> customizers = AnnotationFunctions.findCustomizer(
        sourceArtifact(), ArtifactTypes.ObjectAssistant, context.roundEnvironments()
    );

    for (CustomType customizer : customizers) {
      for (MethodStatement method : customizer.declaredMethods()) {
        methods.add(Map.of(
            "signature", MethodSignatureDeclarations.build(method).get(this::addImport, this::simpleNameOf),
            "name", method.name(),
            "returnTypeDeclaration", method.returnType().orElseThrow().actualDeclaration(this::addImportAndGetSimpleName),
            "paramNames", method.params().stream().map(MethodParam::name).collect(Collectors.joining(", "))
        ));
      }
    }
    return methods;
  }
}
