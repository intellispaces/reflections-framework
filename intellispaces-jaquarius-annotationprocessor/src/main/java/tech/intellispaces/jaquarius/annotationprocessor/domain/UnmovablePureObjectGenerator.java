package tech.intellispaces.jaquarius.annotationprocessor.domain;

import tech.intellispaces.commons.annotation.processor.ArtifactGeneratorContext;
import tech.intellispaces.commons.base.exception.UnexpectedExceptions;
import tech.intellispaces.commons.java.reflection.customtype.CustomType;
import tech.intellispaces.commons.java.reflection.method.MethodStatement;
import tech.intellispaces.commons.java.reflection.reference.CustomTypeReference;
import tech.intellispaces.commons.java.reflection.reference.TypeReference;
import tech.intellispaces.jaquarius.annotation.Movable;
import tech.intellispaces.jaquarius.annotation.ObjectHandle;
import tech.intellispaces.jaquarius.annotation.Unmovable;
import tech.intellispaces.jaquarius.naming.NameConventionFunctions;
import tech.intellispaces.jaquarius.object.reference.ObjectHandleType;
import tech.intellispaces.jaquarius.object.reference.ObjectHandleTypes;
import tech.intellispaces.jaquarius.object.reference.UnmovableObjectHandle;
import tech.intellispaces.jaquarius.space.channel.ChannelFunctions;
import tech.intellispaces.jaquarius.space.domain.DomainFunctions;
import tech.intellispaces.jaquarius.traverse.TraverseType;

import java.util.Optional;
import java.util.stream.Stream;

public class UnmovablePureObjectGenerator extends PureObjectGenerator {

  public UnmovablePureObjectGenerator(CustomType domainType) {
    super(domainType);
  }

  @Override
  public boolean isRelevant(ArtifactGeneratorContext context) {
    return true;
  }

  @Override
  protected ObjectHandleType getObjectHandleType() {
    return ObjectHandleTypes.UnmovablePureObject;
  }

  @Override
  public String generatedArtifactName() {
    return NameConventionFunctions.getUnmovablePureObjectTypename(sourceArtifact().className(), false);
  }

  @Override
  protected String templateName() {
    return "/unmovable_pure_object.template";
  }

  @Override
  protected boolean analyzeSourceArtifact(ArtifactGeneratorContext context) {
    addImports(
        ObjectHandle.class,
        Unmovable.class,
        UnmovableObjectHandle.class,
        UnexpectedExceptions.class
    );

    analyzeAlias();
    analyzeDomain();
    analyzeObjectHandleMethods(sourceArtifact(), context);
    analyzeConversionMethods(sourceArtifact());

    addVariable("handleTypeParamsBrief", typeParamsBrief);
    addVariable("handleTypeParamsFull", typeParamsFull);
    addVariable("conversionMethods", conversionMethods);
    addVariable("undefinedPureObjectHandle", getUndefinedOriginHandleClassName());
    addVariable("isAlias", isAlias);
    addVariable("primaryObject", baseObjectHandle);
    return true;
  }

  protected void analyzeAlias() {
    Optional<CustomTypeReference> equivalentDomain = DomainFunctions.getAliasNearNeighbourDomain(sourceArtifact());
    isAlias = equivalentDomain.isPresent();
    if (isAlias) {
      baseObjectHandle = buildObjectHandleDeclaration(equivalentDomain.get(), ObjectHandleTypes.UnmovablePureObject, true);
    }
  }

  @Override
  protected Stream<MethodStatement> getObjectHandleMethods(CustomType customType, ArtifactGeneratorContext context) {
    return super.getObjectHandleMethods(customType, context)
        .filter(m -> ChannelFunctions.getTraverseTypes(m).stream().noneMatch(TraverseType::isMovingBased));
  }

  @Override
  protected void appendObjectFormMethodReturnType(StringBuilder sb, MethodStatement method) {
    TypeReference domainReturnType = method.returnType().orElseThrow();
    if (
        ChannelFunctions.getTraverseTypes(method).stream().anyMatch(TraverseType::isMoving)
            || method.hasAnnotation(Movable.class)
    ) {
      sb.append(buildObjectHandleDeclaration(domainReturnType, ObjectHandleTypes.MovablePureObject, true));
    } else if (method.hasAnnotation(Unmovable.class)) {
      sb.append(buildObjectHandleDeclaration(domainReturnType, ObjectHandleTypes.UnmovablePureObject, true));
    } else {
      sb.append(buildObjectHandleDeclaration(domainReturnType, ObjectHandleTypes.UndefinedPureObject, true));
    }
  }
}
