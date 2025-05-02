package tech.intellispaces.reflectionsj.annotationprocessor.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.reflectionsj.ArtifactType;
import tech.intellispaces.reflectionsj.annotation.ObjectHandle;
import tech.intellispaces.reflectionsj.annotation.Unmovable;
import tech.intellispaces.reflectionsj.artifact.ArtifactTypes;
import tech.intellispaces.reflectionsj.naming.NameConventionFunctions;
import tech.intellispaces.reflectionsj.object.reference.MovabilityType;
import tech.intellispaces.reflectionsj.object.reference.MovabilityTypes;
import tech.intellispaces.reflectionsj.object.reference.ObjectReferenceForm;
import tech.intellispaces.reflectionsj.object.reference.ObjectReferenceForms;
import tech.intellispaces.reflectionsj.object.reference.UnmovableObjectHandle;
import tech.intellispaces.reflectionsj.space.channel.ChannelFunctions;
import tech.intellispaces.reflectionsj.space.domain.DomainFunctions;
import tech.intellispaces.reflectionsj.traverse.TraverseType;
import tech.intellispaces.statementsj.customtype.CustomType;
import tech.intellispaces.statementsj.method.MethodSignatureDeclarations;
import tech.intellispaces.statementsj.method.MethodStatement;
import tech.intellispaces.statementsj.reference.CustomTypeReference;

public class UnmovableObjectHandleGenerator extends AbstractObjectGenerator {

  private final List<Map<String, String>> movableMethods = new ArrayList<>();

  public UnmovableObjectHandleGenerator(CustomType domainType) {
    super(domainType);
  }

  @Override
  public boolean isRelevant(ArtifactGeneratorContext context) {
    return true;
  }

  @Override
  protected ObjectReferenceForm getForm() {
    return ObjectReferenceForms.ObjectHandle;
  }

  @Override
  protected MovabilityType getMovabilityType() {
    return MovabilityTypes.Unmovable;
  }

  @Override
  protected List<ArtifactType> relatedArtifactTypes() {
    return List.of(ArtifactTypes.UnmovableObjectHandle);
  }

  @Override
  public String generatedArtifactName() {
    return NameConventionFunctions.getUnmovableObjectHandleTypename(sourceArtifact().className(), false);
  }

  @Override
  protected String templateName() {
    return "/unmovable_object_handle.template";
  }

  @Override
  protected boolean analyzeSourceArtifact(ArtifactGeneratorContext context) {
    addImports(
        ObjectHandle.class,
        Unmovable.class,
        UnmovableObjectHandle.class,
        UnexpectedExceptions.class
    );

    analyzeDomain();
    analyzeAlias();
    analyzeObjectFormMethods(sourceArtifact(), context);
    analyzeConversionMethods(sourceArtifact());
    analyzeMovableMethods(context);

    addVariable("movableClassSimpleName", movableClassSimpleName());
    addVariable("handleTypeParamsBrief", typeParamsBrief);
    addVariable("handleTypeParamsFull", typeParamsFull);
    addVariable("domainTypeParamsBrief", domainTypeParamsBrief);
    addVariable("generalObjectHandle", generalObjectHandle);
    addVariable("conversionMethods", conversionMethods);
    addVariable("isAlias", isAlias);
    addVariable("primaryObjectHandle", baseObjectHandle);
    addVariable("movableMethods", movableMethods);
    addVariable("simpleObject", getSimpleObjectClassName());
    return true;
  }

  private String getSimpleObjectClassName() {
    return addImportAndGetSimpleName(NameConventionFunctions.getUnmovableRegularObjectTypename(sourceArtifact().className(), false));
  }

  private void analyzeAlias() {
    Optional<CustomTypeReference> equivalentDomain = DomainFunctions.getAliasNearNeighbourDomain(sourceArtifact());
    isAlias = equivalentDomain.isPresent();
    if (isAlias) {
      baseObjectHandle = buildObjectFormDeclaration(equivalentDomain.get(), ObjectReferenceForms.ObjectHandle, MovabilityTypes.Unmovable, true);
    }
  }

  @Override
  protected Stream<MethodStatement> getObjectFormMethods(CustomType customType, ArtifactGeneratorContext context) {
    return super.getObjectFormMethods(customType, context)
        .filter(m -> ChannelFunctions.getTraverseTypes(m).stream().noneMatch(TraverseType::isMovingBased));
  }

  private void analyzeMovableMethods(ArtifactGeneratorContext context) {
    super.getObjectFormMethods(sourceArtifact(), context)
        .filter(m -> ChannelFunctions.getTraverseTypes(m).stream().anyMatch(TraverseType::isMovingBased))
        .map(this::generateMethod)
        .forEach(movableMethods::add);
  }

  private Map<String, String> generateMethod(MethodStatement method) {
    var returnTypeHandle = new StringBuilder();
    appendObjectFormMethodReturnType(returnTypeHandle, method);

    String declaration = MethodSignatureDeclarations.build(method)
        .returnType(returnTypeHandle.toString())
        .includeMethodTypeParams(true)
        .includeOwnerTypeParams(false)
        .get(this::addImport, this::addImportAndGetSimpleName);
    return Map.of(
        "javadoc", buildGeneratedMethodJavadoc(method.owner().canonicalName(), method),
        "declaration", declaration
    );
  }
}
