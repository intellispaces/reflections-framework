package tech.intellispaces.reflectionsj.annotationprocessor.domain;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.reflectionsj.ArtifactType;
import tech.intellispaces.reflectionsj.annotation.Movable;
import tech.intellispaces.reflectionsj.annotation.ObjectHandle;
import tech.intellispaces.reflectionsj.annotation.Unmovable;
import tech.intellispaces.reflectionsj.artifact.ArtifactTypes;
import tech.intellispaces.reflectionsj.channel.Channel0;
import tech.intellispaces.reflectionsj.channel.Channel1;
import tech.intellispaces.reflectionsj.channel.ChannelFunction0;
import tech.intellispaces.reflectionsj.channel.ChannelFunction1;
import tech.intellispaces.reflectionsj.exception.TraverseException;
import tech.intellispaces.reflectionsj.naming.NameConventionFunctions;
import tech.intellispaces.reflectionsj.object.reference.MovabilityType;
import tech.intellispaces.reflectionsj.object.reference.MovabilityTypes;
import tech.intellispaces.reflectionsj.object.reference.MovableObjectHandle;
import tech.intellispaces.reflectionsj.object.reference.ObjectReferenceForm;
import tech.intellispaces.reflectionsj.object.reference.ObjectReferenceForms;
import tech.intellispaces.reflectionsj.space.channel.ChannelFunctions;
import tech.intellispaces.reflectionsj.space.domain.DomainFunctions;
import tech.intellispaces.reflectionsj.traverse.MappingOfMovingTraverse;
import tech.intellispaces.reflectionsj.traverse.TraverseType;
import tech.intellispaces.statementsj.customtype.CustomType;
import tech.intellispaces.statementsj.method.MethodStatement;
import tech.intellispaces.statementsj.reference.CustomTypeReference;
import tech.intellispaces.statementsj.reference.CustomTypeReferences;
import tech.intellispaces.statementsj.reference.TypeReference;

public class MovableRegularObjectGenerator extends AbstractRegularObjectGenerator {

  public MovableRegularObjectGenerator(CustomType domainType) {
    super(domainType);
  }

  @Override
  public boolean isRelevant(ArtifactGeneratorContext context) {
    return true;
  }

  @Override
  protected ObjectReferenceForm getForm() {
    return ObjectReferenceForms.Regular;
  }

  @Override
  protected MovabilityType getMovabilityType() {
    return MovabilityTypes.Movable;
  }

  @Override
  protected List<ArtifactType> relatedArtifactTypes() {
    return List.of(ArtifactTypes.MovableRegularObject);
  }

  @Override
  public String generatedArtifactName() {
    return NameConventionFunctions.getMovableRegularObjectTypename(sourceArtifact().className(), false);
  }

  @Override
  protected String templateName() {
    return "/movable_regular_object.template";
  }

  @Override
  protected boolean analyzeSourceArtifact(ArtifactGeneratorContext context) {
    addImports(
        Movable.class,
        ObjectHandle.class,
        MovableObjectHandle.class,
        Channel0.class,
        Channel1.class,
        ChannelFunction0.class,
        ChannelFunction1.class,
        MappingOfMovingTraverse.class,
        TraverseException.class
    );

    analyzeAlias();
    analyzeDomain();
    analyzeObjectFormMethods(sourceArtifact(), context);
    analyzeConversionMethods(sourceArtifact());

    addVariable("isAlias", isAlias);
    addVariable("primaryObject", baseObjectHandle);
    addVariable("handleTypeParamsFull", typeParamsFull);
    addVariable("handleTypeParamsBrief", typeParamsBrief);
    addVariable("domainMethods", methods);
    addVariable("generalPureObjectHandle", getGeneralOriginHandleClassName());
    addVariable("underlyingTypes", underlyingTypes.isEmpty() ? "" : ", " + String.join(", ", underlyingTypes));
    return true;
  }

  protected void analyzeAlias() {
    Optional<CustomTypeReference> equivalentDomain = DomainFunctions.getAliasNearNeighbourDomain(sourceArtifact());
    isAlias = equivalentDomain.isPresent();
    if (isAlias) {
      baseObjectHandle = buildObjectFormDeclaration(equivalentDomain.get(), ObjectReferenceForms.Regular, MovabilityTypes.Movable, true);
    }
  }

  @Override
  protected Stream<MethodStatement> getObjectFormMethods(CustomType customType, ArtifactGeneratorContext context) {
    return super.getObjectFormMethods(customType, context)
        .filter(m -> ChannelFunctions.getTraverseTypes(m).stream().anyMatch(TraverseType::isMovingBased));
  }

  @Override
  protected void appendObjectFormMethodReturnType(StringBuilder sb, MethodStatement method) {
    TypeReference domainReturnType = method.returnType().orElseThrow();
    if (ChannelFunctions.getTraverseTypes(method).stream().anyMatch(TraverseType::isMoving)) {
      TypeReference sourceDomainReference = CustomTypeReferences.get(sourceArtifact());
      sb.append(buildObjectFormDeclaration(sourceDomainReference, ObjectReferenceForms.Regular, MovabilityTypes.Movable, true));
    } else if (method.hasAnnotation(Movable.class)) {
      sb.append(buildObjectFormDeclaration(domainReturnType, ObjectReferenceForms.Regular, MovabilityTypes.Movable, true));
    } else if (method.hasAnnotation(Unmovable.class)) {
      sb.append(buildObjectFormDeclaration(domainReturnType, ObjectReferenceForms.Regular, MovabilityTypes.Unmovable, true));
    } else {
      sb.append(buildObjectFormDeclaration(domainReturnType, ObjectReferenceForms.Regular, MovabilityTypes.General, true));
    }
  }
}
