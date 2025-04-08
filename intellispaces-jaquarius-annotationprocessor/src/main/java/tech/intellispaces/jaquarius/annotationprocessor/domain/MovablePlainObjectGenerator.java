package tech.intellispaces.jaquarius.annotationprocessor.domain;

import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.jaquarius.ArtifactType;
import tech.intellispaces.jaquarius.annotation.Movable;
import tech.intellispaces.jaquarius.annotation.ObjectHandle;
import tech.intellispaces.jaquarius.annotation.Unmovable;
import tech.intellispaces.jaquarius.artifact.ArtifactTypes;
import tech.intellispaces.jaquarius.channel.Channel0;
import tech.intellispaces.jaquarius.channel.Channel1;
import tech.intellispaces.jaquarius.channel.ChannelFunction0;
import tech.intellispaces.jaquarius.channel.ChannelFunction1;
import tech.intellispaces.jaquarius.exception.TraverseException;
import tech.intellispaces.jaquarius.naming.NameConventionFunctions;
import tech.intellispaces.jaquarius.object.reference.MovabilityType;
import tech.intellispaces.jaquarius.object.reference.MovabilityTypes;
import tech.intellispaces.jaquarius.object.reference.MovableObjectHandle;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForm;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForms;
import tech.intellispaces.jaquarius.space.channel.ChannelFunctions;
import tech.intellispaces.jaquarius.space.domain.DomainFunctions;
import tech.intellispaces.jaquarius.traverse.MappingOfMovingTraverse;
import tech.intellispaces.jaquarius.traverse.TraverseType;
import tech.intellispaces.reflection.customtype.CustomType;
import tech.intellispaces.reflection.method.MethodStatement;
import tech.intellispaces.reflection.reference.CustomTypeReference;
import tech.intellispaces.reflection.reference.TypeReference;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class MovablePlainObjectGenerator extends AbstractPlainObjectGenerator {

  public MovablePlainObjectGenerator(CustomType domainType) {
    super(domainType);
  }

  @Override
  public boolean isRelevant(ArtifactGeneratorContext context) {
    return true;
  }

  @Override
  protected ObjectReferenceForm getForm() {
    return ObjectReferenceForms.Plain;
  }

  @Override
  protected MovabilityType getMovabilityType() {
    return MovabilityTypes.Movable;
  }

  @Override
  protected List<ArtifactType> relatedArtifactTypes() {
    return List.of(ArtifactTypes.MovablePlainObject);
  }

  @Override
  public String generatedArtifactName() {
    return NameConventionFunctions.getMovablePlainObjectTypename(sourceArtifact().className(), false);
  }

  @Override
  protected String templateName() {
    return "/movable_plain_object.template";
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
    addVariable("conversionMethods", conversionMethods);
    addVariable("domainMethods", methods);
    addVariable("undefinedPureObjectHandle", getUndefinedOriginHandleClassName());
    return true;
  }

  protected void analyzeAlias() {
    Optional<CustomTypeReference> equivalentDomain = DomainFunctions.getAliasNearNeighbourDomain(sourceArtifact());
    isAlias = equivalentDomain.isPresent();
    if (isAlias) {
      baseObjectHandle = buildObjectFormDeclaration(equivalentDomain.get(), ObjectReferenceForms.Plain, MovabilityTypes.Movable, true);
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
    if (
        ChannelFunctions.getTraverseTypes(method).stream().anyMatch(TraverseType::isMoving)
            || method.hasAnnotation(Movable.class)
    ) {
      sb.append(buildObjectFormDeclaration(domainReturnType, ObjectReferenceForms.Plain, MovabilityTypes.Movable, true));
    } else if (method.hasAnnotation(Unmovable.class)) {
      sb.append(buildObjectFormDeclaration(domainReturnType, ObjectReferenceForms.Plain, MovabilityTypes.Unmovable, true));
    } else {
      sb.append(buildObjectFormDeclaration(domainReturnType, ObjectReferenceForms.Plain, MovabilityTypes.Undefined, true));
    }
  }
}
