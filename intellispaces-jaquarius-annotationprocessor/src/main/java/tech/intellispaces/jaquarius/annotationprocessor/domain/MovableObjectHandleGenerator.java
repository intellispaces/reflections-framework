package tech.intellispaces.jaquarius.annotationprocessor.domain;

import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.jaquarius.annotation.Movable;
import tech.intellispaces.jaquarius.annotation.ObjectHandle;
import tech.intellispaces.jaquarius.annotation.Unmovable;
import tech.intellispaces.jaquarius.channel.Channel0;
import tech.intellispaces.jaquarius.channel.Channel1;
import tech.intellispaces.jaquarius.channel.ChannelMethod0;
import tech.intellispaces.jaquarius.channel.ChannelMethod1;
import tech.intellispaces.jaquarius.channel.MappingOfMovingChannel;
import tech.intellispaces.jaquarius.exception.TraverseException;
import tech.intellispaces.jaquarius.naming.NameConventionFunctions;
import tech.intellispaces.jaquarius.object.handle.ObjectHandleTypes;
import tech.intellispaces.jaquarius.object.reference.MovableObjectHandle;
import tech.intellispaces.jaquarius.object.reference.ObjectHandleType;
import tech.intellispaces.jaquarius.space.channel.ChannelFunctions;
import tech.intellispaces.jaquarius.space.domain.DomainFunctions;
import tech.intellispaces.jaquarius.traverse.TraverseType;
import tech.intellispaces.java.reflection.customtype.CustomType;
import tech.intellispaces.java.reflection.method.MethodStatement;
import tech.intellispaces.java.reflection.reference.CustomTypeReference;
import tech.intellispaces.java.reflection.reference.TypeReference;

import java.util.List;
import java.util.stream.Stream;

public class MovableObjectHandleGenerator extends ObjectHandleGenerator {

  public MovableObjectHandleGenerator(CustomType domainType) {
    super(domainType);
  }

  @Override
  public boolean isRelevant(ArtifactGeneratorContext context) {
    return true;
  }

  @Override
  protected ObjectHandleType getObjectHandleType() {
    return ObjectHandleTypes.Movable;
  }

  @Override
  public String generatedArtifactName() {
    return NameConventionFunctions.getMovableObjectHandleTypename(sourceArtifact().className());
  }

  @Override
  protected String templateName() {
    return "/movable_object_handle.template";
  }

  @Override
  protected boolean analyzeSourceArtifact(ArtifactGeneratorContext context) {
    addImports(
        Movable.class,
        ObjectHandle.class,
        MovableObjectHandle.class,
        Channel0.class,
        Channel1.class,
        ChannelMethod0.class,
        ChannelMethod1.class,
        MappingOfMovingChannel.class,
        TraverseException.class
    );

    analyzeDomain();
    analyzeAlias();
    analyzeObjectHandleMethods(sourceArtifact(), context);
    analyzeConversionMethods(sourceArtifact());

    addVariable("domainTypeParamsFull", domainTypeParamsFull);
    addVariable("domainTypeParamsBrief", domainTypeParamsBrief);
    addVariable("generalObjectHandle", generalObjectHandle);
    addVariable("conversionMethods", conversionMethods);
    addVariable("domainMethods", methods);
    addVariable("isAlias", isAlias);
    addVariable("primaryObjectHandle", primaryObjectHandle);
    addVariable("primaryDomainSimpleName", primaryDomainSimpleName);
    addVariable("primaryDomainTypeArguments", primaryDomainTypeArguments);
    return true;
  }

  private void analyzeAlias() {
    List<CustomTypeReference> equivalentDomains = DomainFunctions.getEquivalentDomains(sourceArtifact());
    isAlias = !equivalentDomains.isEmpty();
    if (isAlias) {
      CustomTypeReference nearEquivalentDomain = equivalentDomains.get(0);
      CustomTypeReference mainEquivalentDomain = equivalentDomains.get(equivalentDomains.size() - 1);

      primaryObjectHandle = buildObjectHandleDeclaration(nearEquivalentDomain, ObjectHandleTypes.Movable);
      primaryDomainSimpleName = addToImportAndGetSimpleName(mainEquivalentDomain.targetType().canonicalName());
      primaryDomainTypeArguments = nearEquivalentDomain.typeArgumentsDeclaration(this::addToImportAndGetSimpleName);
    }
  }

  @Override
  protected Stream<MethodStatement> getObjectHandleMethods(CustomType customType, ArtifactGeneratorContext context) {
    return super.getObjectHandleMethods(customType, context)
        .filter(m -> ChannelFunctions.getTraverseTypes(m).stream().anyMatch(TraverseType::isMovingBased));
  }

  @Override
  protected void appendMethodReturnHandleType(StringBuilder sb, MethodStatement method) {
    TypeReference domainReturnType = method.returnType().orElseThrow();
    if (
        ChannelFunctions.getTraverseTypes(method).stream().anyMatch(TraverseType::isMoving)
            || method.hasAnnotation(Movable.class)
    ) {
      sb.append(buildObjectHandleDeclaration(domainReturnType, ObjectHandleTypes.Movable));
    } else if (method.hasAnnotation(Unmovable.class)) {
      sb.append(buildObjectHandleDeclaration(domainReturnType, ObjectHandleTypes.Unmovable));
    } else {
      sb.append(buildObjectHandleDeclaration(domainReturnType, ObjectHandleTypes.General));
    }
  }
}
