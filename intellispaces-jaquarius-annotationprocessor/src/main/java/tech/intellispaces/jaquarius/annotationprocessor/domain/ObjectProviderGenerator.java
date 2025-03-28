package tech.intellispaces.jaquarius.annotationprocessor.domain;

import tech.intellispaces.commons.annotation.processor.ArtifactGeneratorContext;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.commons.reflection.customtype.CustomType;
import tech.intellispaces.commons.reflection.method.MethodParam;
import tech.intellispaces.commons.reflection.method.MethodSignatureDeclarations;
import tech.intellispaces.commons.reflection.method.MethodStatement;
import tech.intellispaces.commons.reflection.reference.CustomTypeReference;
import tech.intellispaces.commons.reflection.reference.NamedReference;
import tech.intellispaces.commons.reflection.reference.NotPrimitiveReference;
import tech.intellispaces.commons.reflection.reference.TypeReference;
import tech.intellispaces.commons.text.StringFunctions;
import tech.intellispaces.commons.type.Type;
import tech.intellispaces.jaquarius.annotation.Dataset;
import tech.intellispaces.jaquarius.annotationprocessor.AnnotationFunctions;
import tech.intellispaces.jaquarius.annotationprocessor.ArtifactTypes;
import tech.intellispaces.jaquarius.annotationprocessor.JaquariusArtifactGenerator;
import tech.intellispaces.jaquarius.naming.NameConventionFunctions;
import tech.intellispaces.jaquarius.object.reference.MovabilityTypes;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForms;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceFunctions;
import tech.intellispaces.jaquarius.system.Modules;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ObjectProviderGenerator extends JaquariusArtifactGenerator {

  public ObjectProviderGenerator(CustomType domainType) {
    super(domainType);
  }

  @Override
  protected String templateName() {
    return "/object_provider.template";
  }

  @Override
  public String generatedArtifactName() {
    return NameConventionFunctions.getObjectProviderCanonicalName(sourceArtifact());
  }

  @Override
  public boolean isRelevant(ArtifactGeneratorContext context) {
    // Starts the Object Provider generator after all other types of generators
    return context.isPenaltyRound();
  }

  @Override
  protected boolean analyzeSourceArtifact(ArtifactGeneratorContext context) {
    addImport(Modules.class);
    addImport(UnexpectedExceptions.class);

    addVariable("brokerSimpleName", addImportAndGetSimpleName(NameConventionFunctions.getObjectProviderBrokerCanonicalName(sourceArtifact())));
    addVariable("isDataset", AnnotationFunctions.isAssignableAnnotation(sourceArtifact(), Dataset.class));
    addVariable("plainFirstLetterLowercaseSimpleName",
        StringFunctions.lowercaseFirstLetter(StringFunctions.removeTailOrElseThrow(sourceArtifactSimpleName(), "Domain")));
    addVariable("datasetBuilderSimpleName",
        addImportAndGetSimpleName(NameConventionFunctions.getDatasetBuilderCanonicalName(sourceArtifact().className())));
    addVariable("undefinedObjectHandleSimpleName",
        addImportAndGetSimpleName(ObjectReferenceFunctions.getUndefinedObjectHandleTypename(sourceArtifact())));
    addTypeParamVariables();
    addVariable("customizeMethods", getCustomizeMethods(context));
    return true;
  }

  void addTypeParamVariables() {
    addVariable("typeParamsFull", ObjectReferenceFunctions.getObjectFormTypeParamDeclaration(
        sourceArtifact(),
        ObjectReferenceForms.Plain,
        MovabilityTypes.Undefined,
        this::addImportAndGetSimpleName,
        true,
        true));
    addVariable("typeParamsBrief", ObjectReferenceFunctions.getObjectFormTypeParamDeclaration(
        sourceArtifact(),
        ObjectReferenceForms.Plain,
        MovabilityTypes.Undefined,
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

  List<Map<String, Object>> getCustomizeMethods(ArtifactGeneratorContext context) {
    var methods = new ArrayList<Map<String, Object>>();
    List<CustomType> customizers = AnnotationFunctions.findArtifactCustomizers(
        sourceArtifact(), ArtifactTypes.ObjectProvider, context.initialRoundEnvironment()
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
