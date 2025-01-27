package tech.intellispaces.jaquarius.annotationprocessor.domain;

import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.general.collection.ArraysFunctions;
import tech.intellispaces.general.type.Type;
import tech.intellispaces.general.type.Types;
import tech.intellispaces.jaquarius.annotation.Channel;
import tech.intellispaces.jaquarius.annotation.ObjectHandle;
import tech.intellispaces.jaquarius.channel.Channel0;
import tech.intellispaces.jaquarius.channel.Channel1;
import tech.intellispaces.jaquarius.channel.ChannelFunction0;
import tech.intellispaces.jaquarius.channel.ChannelFunction1;
import tech.intellispaces.jaquarius.channel.MappingChannel;
import tech.intellispaces.jaquarius.exception.TraverseException;
import tech.intellispaces.jaquarius.exception.TraverseExceptions;
import tech.intellispaces.jaquarius.naming.NameConventionFunctions;
import tech.intellispaces.jaquarius.object.reference.ObjectHandleType;
import tech.intellispaces.jaquarius.object.reference.ObjectHandleTypes;
import tech.intellispaces.jaquarius.space.channel.ChannelFunctions;
import tech.intellispaces.jaquarius.space.domain.DomainFunctions;
import tech.intellispaces.jaquarius.traverse.TraverseType;
import tech.intellispaces.jaquarius.traverse.TraverseTypes;
import tech.intellispaces.java.reflection.customtype.CustomType;
import tech.intellispaces.java.reflection.method.MethodSignatureDeclarations;
import tech.intellispaces.java.reflection.method.MethodStatement;
import tech.intellispaces.java.reflection.reference.CustomTypeReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class UnmovableDownwardObjectHandleGenerator extends ConversionObjectHandleGenerator {

  private final List<Map<String, String>> movableMethods = new ArrayList<>();

  public UnmovableDownwardObjectHandleGenerator(
      CustomType annotatedType, CustomTypeReference superDomainType
  ) {
    super(annotatedType, superDomainType);
  }

  @Override
  public boolean isRelevant(ArtifactGeneratorContext context) {
    return true;
  }

  @Override
  protected ObjectHandleType getObjectHandleType() {
    return ObjectHandleTypes.Unmovable;
  }

  @Override
  public String generatedArtifactName() {
    return NameConventionFunctions.getUnmovableDownwardObjectHandleTypename(
        sourceArtifact(), superDomainType.targetType());
  }

  @Override
  protected String templateName() {
    return "/unmovable_downward_object_handle.template";
  }

  @Override
  protected boolean analyzeSourceArtifact(ArtifactGeneratorContext context) {
    addImports(
        ObjectHandle.class,
        Type.class,
        Types.class,
        Channel0.class,
        Channel1.class,
        ChannelFunction0.class,
        ChannelFunction1.class,
        MappingChannel.class,
        TraverseException.class
    );

    String unmovableObjectHandleName = addImportAndGetSimpleName(
        NameConventionFunctions.getUnmovableObjectHandleTypename(superDomainType.targetType().className()));

    analyzeDomain();
    analyzeChildObjectHandleType();
    analyzeObjectHandleMethods(context);
    analyzeMovableMethods(context);
    analyzeAlias();

    addVariable("classTypeParams", classTypeParams);
    addVariable("classTypeParamsBrief", classTypeParamsBrief);
    addVariable("domainTypeParamsBrief", domainTypeParamsBrief);
    addVariable("domainTypeArguments", domainTypeArguments);
    addVariable("parentDomainClassSimpleName", parentDomainClassSimpleName);
    addVariable("childObjectHandleType", childObjectHandleType);
    addVariable("childField", childFieldName);
    addVariable("methods", methods);
    addVariable("movableMethods", movableMethods);
    addVariable("unmovableObjectHandleName", unmovableObjectHandleName);
    addVariable("domainClassSimpleName", domainClassSimpleName);
    addVariable("isAlias", isAlias);
    addVariable("primaryDomainSimpleName", primaryDomainSimpleName);
    addVariable("domainType", domainType);
    return true;
  }

  private void analyzeChildObjectHandleType() {
    childObjectHandleType = addImportAndGetSimpleName(
        NameConventionFunctions.getUnmovableObjectHandleTypename(sourceArtifact().className())
    );
  }

  @Override
  protected Stream<MethodStatement> getObjectHandleMethods(
      CustomType customType, ArtifactGeneratorContext context
  ) {
    return buildActualType(superDomainType.targetType(), context).actualMethods().stream()
        .filter(m -> excludeDeepConversionMethods(m, customType))
        .filter(this::isNotMovingMethod)
        .filter(DomainFunctions::isNotDomainClassGetter);
  }

  private boolean isNotMovingMethod(MethodStatement method) {
    Optional<Channel> channel = method.selectAnnotation(Channel.class);
    if (channel.isEmpty()) {
      return true;
    }
    return !ArraysFunctions.containsAny(
        channel.orElseThrow().allowedTraverse(), TraverseTypes.Moving, TraverseTypes.MappingOfMoving
    );
  }

  private void analyzeMovableMethods(ArtifactGeneratorContext context) {
    getObjectHandleMethods(sourceArtifact(), context)
        .filter(m -> ChannelFunctions.getTraverseTypes(m).stream().anyMatch(TraverseType::isMovingBased))
        .map(this::generateMovableMethod)
        .forEach(movableMethods::add);
    if (!movableMethods.isEmpty()) {
      addImport(TraverseExceptions.class);
    }
  }

  private Map<String, String> generateMovableMethod(MethodStatement method) {
    var returnTypeHandle = new StringBuilder();
    appendObjectFormMethodReturnType(returnTypeHandle, method);

    String signature = MethodSignatureDeclarations.build(method)
        .returnType(returnTypeHandle.toString())
        .includeMethodTypeParams(true)
        .includeOwnerTypeParams(false)
        .get(this::addImport, this::addImportAndGetSimpleName);
    return Map.of(
        "javadoc", buildGeneratedMethodJavadoc(method.owner().canonicalName(), method),
        "signature", signature
    );
  }
}
