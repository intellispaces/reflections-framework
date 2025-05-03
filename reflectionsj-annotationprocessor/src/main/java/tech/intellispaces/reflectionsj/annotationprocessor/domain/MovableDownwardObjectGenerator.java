package tech.intellispaces.reflectionsj.annotationprocessor.domain;

import java.util.List;
import java.util.stream.Stream;

import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.commons.type.Type;
import tech.intellispaces.commons.type.Types;
import tech.intellispaces.reflectionsj.ArtifactType;
import tech.intellispaces.reflectionsj.annotation.ObjectHandle;
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
import tech.intellispaces.reflectionsj.object.reference.ObjectHandles;
import tech.intellispaces.reflectionsj.object.reference.ObjectReferenceForm;
import tech.intellispaces.reflectionsj.object.reference.ObjectReferenceForms;
import tech.intellispaces.reflectionsj.space.channel.ChannelFunctions;
import tech.intellispaces.reflectionsj.space.domain.DomainFunctions;
import tech.intellispaces.reflectionsj.traverse.MappingOfMovingTraverse;
import tech.intellispaces.reflectionsj.traverse.MappingTraverse;
import tech.intellispaces.statementsj.customtype.CustomType;
import tech.intellispaces.statementsj.method.MethodStatement;
import tech.intellispaces.statementsj.reference.CustomTypeReference;
import tech.intellispaces.statementsj.type.TypeOf;

public class MovableDownwardObjectGenerator extends ConversionObjectGenerator {

  public MovableDownwardObjectGenerator(
      CustomType annotatedType, CustomTypeReference superDomainType
  ) {
    super(annotatedType, superDomainType);
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
    return MovabilityTypes.Movable;
  }

  @Override
  protected List<ArtifactType> relatedArtifactTypes() {
    return List.of(ArtifactTypes.MovableDownwardObject, ArtifactTypes.ObjectHandle, ArtifactTypes.RegularObject);
  }

  @Override
  public String generatedArtifactName() {
    return NameConventionFunctions.getMovableDownwardObjectTypename(
        sourceArtifact(), superDomainType.targetType());
  }

  @Override
  protected String templateName() {
    return "/movable_downward_object.template";
  }

  @Override
  protected boolean analyzeSourceArtifact(ArtifactGeneratorContext context) {
    addImports(
        ObjectHandle.class,
        MovableObjectHandle.class,
        ObjectHandles.class,
        Type.class,
        Types.class,
        TypeOf.class,
        Channel0.class,
        Channel1.class,
        ChannelFunction0.class,
        ChannelFunction1.class,
        MappingTraverse.class,
        MappingOfMovingTraverse.class,
        ChannelFunctions.class,
        TraverseException.class
    );

    String movableObjectHandleName = addImportAndGetSimpleName(
        NameConventionFunctions.getMovableObjectHandleTypename(superDomainType.targetType().className(), false));

    analyzeDomain();
    analyzeChildObjectHandleType();
    analyzeObjectHandleMethods(context);
    analyzeAlias();

    addVariable("classTypeParams", classTypeParams);
    addVariable("classTypeParamsBrief", classTypeParamsBrief);
    addVariable("domainTypeParamsBrief", domainTypeParamsBrief);
    addVariable("domainTypeArguments", domainTypeArguments);
    addVariable("parentDomainClassSimpleName", parentDomainClassSimpleName);
    addVariable("childObjectHandleType", childObjectHandleType);
    addVariable("childField", childFieldName);
    addVariable("methods", methods);
    addVariable("movableObjectHandleName", movableObjectHandleName);
    addVariable("domainClassSimpleName", domainClassSimpleName);
    addVariable("isAlias", isAlias);
    addVariable("primaryDomainSimpleName", primaryDomainSimpleName);
    addVariable("domainType", domainType);
    addVariable("objectHandleClassSimpleName", getObjectHandleSimpleName());
    return true;
  }

  private void analyzeChildObjectHandleType() {
    childObjectHandleType = addImportAndGetSimpleName(
        NameConventionFunctions.getMovableObjectHandleTypename(sourceArtifact().className(), true)
    );
  }

  @Override
  protected Stream<MethodStatement> getObjectFormMethods(
      CustomType customType, ArtifactGeneratorContext context
  ) {
    return buildActualType(superDomainType.targetType(), context, true).actualMethods().stream()
        .filter(m -> !m.isDefault())
        .filter(m -> excludeDeepConversionMethods(m, customType))
        .filter(DomainFunctions::isNotDomainClassGetter);
  }
}
