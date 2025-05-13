package tech.intellispaces.reflections.annotationprocessor.domain;

import java.util.List;
import java.util.stream.Stream;

import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.reflections.framework.ArtifactType;
import tech.intellispaces.reflections.framework.annotation.Movable;
import tech.intellispaces.reflections.framework.annotation.Reflection;
import tech.intellispaces.reflections.framework.annotation.Unmovable;
import tech.intellispaces.reflections.framework.artifact.ArtifactTypes;
import tech.intellispaces.reflections.framework.channel.Channel0;
import tech.intellispaces.reflections.framework.channel.Channel1;
import tech.intellispaces.reflections.framework.channel.ChannelFunction0;
import tech.intellispaces.reflections.framework.channel.ChannelFunction1;
import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.naming.NameConventionFunctions;
import tech.intellispaces.reflections.framework.reflection.MovabilityType;
import tech.intellispaces.reflections.framework.reflection.MovabilityTypes;
import tech.intellispaces.reflections.framework.reflection.MovableReflection;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.reflection.ReflectionForms;
import tech.intellispaces.reflections.framework.space.channel.ChannelFunctions;
import tech.intellispaces.reflections.framework.space.domain.DomainFunctions;
import tech.intellispaces.reflections.framework.traverse.MappingOfMovingTraverse;
import tech.intellispaces.reflections.framework.traverse.TraverseType;
import tech.intellispaces.jstatements.customtype.CustomType;
import tech.intellispaces.jstatements.method.MethodStatement;
import tech.intellispaces.jstatements.reference.CustomTypeReference;
import tech.intellispaces.jstatements.reference.CustomTypeReferences;
import tech.intellispaces.jstatements.reference.TypeReference;

public class MovableReflectionTypeGenerator extends AbstractReflectionFormGenerator {

  public MovableReflectionTypeGenerator(CustomType domainType) {
    super(domainType);
  }

  @Override
  public boolean isRelevant(ArtifactGeneratorContext context) {
    return true;
  }

  @Override
  protected ReflectionForm getForm() {
    return ReflectionForms.Reflection;
  }

  @Override
  protected MovabilityType getMovabilityType() {
    return MovabilityTypes.Movable;
  }

  @Override
  protected List<ArtifactType> relatedArtifactTypes() {
    return List.of(ArtifactTypes.MovableReflection);
  }

  @Override
  public String generatedArtifactName() {
    return NameConventionFunctions.getMovableReflectionTypeName(sourceArtifact().className(), false);
  }

  @Override
  protected String templateName() {
    return "/movable_reflection_type.template";
  }

  @Override
  protected boolean analyzeSourceArtifact(ArtifactGeneratorContext context) {
    addImports(
        Movable.class,
        Reflection.class,
        MovableReflection.class,
        Channel0.class,
        Channel1.class,
        ChannelFunction0.class,
        ChannelFunction1.class,
        MappingOfMovingTraverse.class,
        TraverseException.class
    );

    analyzeDomain();
    analyzeAlias();
    analyzeObjectFormMethods(sourceArtifact(), context);
    analyzeConversionMethods(sourceArtifact());

    addVariable("reflectionTypeParamsFull", typeParamsFull);
    addVariable("reflectionTypeParamsBrief", typeParamsBrief);
    addVariable("domainTypeParamsBrief", domainTypeParamsBrief);
    addVariable("generalReflection", generalReflection);
    addVariable("conversionMethods", conversionMethods);
    addVariable("domainMethods", methods);
    addVariable("isAlias", isAlias);
    addVariable("baseObjectHandle", baseReflection);
    addVariable("primaryReflection", primaryReflection);
    addVariable("primaryDomainSimpleName", primaryDomainSimpleName);
    addVariable("primaryDomainTypeArguments", primaryDomainTypeArguments);
    addVariable("simpleObject", getSimpleObjectClassName());
    return true;
  }

  private String getSimpleObjectClassName() {
    return addImportAndGetSimpleName(NameConventionFunctions.getMovableRegularFormTypeName(sourceArtifact().className(), false));
  }

  private void analyzeAlias() {
    List<CustomTypeReference> equivalentDomains = DomainFunctions.getEquivalentDomains(sourceArtifact());
    isAlias = !equivalentDomains.isEmpty();
    if (isAlias) {
      CustomTypeReference nearEquivalentDomain = equivalentDomains.get(0);
      CustomTypeReference mainEquivalentDomain = equivalentDomains.get(equivalentDomains.size() - 1);

      baseReflection = buildObjectFormDeclaration(nearEquivalentDomain, ReflectionForms.Reflection, MovabilityTypes.Movable, true);

      primaryReflection = buildObjectFormDeclaration(mainEquivalentDomain, ReflectionForms.Reflection, MovabilityTypes.General, true);

      primaryDomainSimpleName = addImportAndGetSimpleName(mainEquivalentDomain.targetType().canonicalName());
      primaryDomainTypeArguments = nearEquivalentDomain.typeArgumentsDeclaration(this::addImportAndGetSimpleName);
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
      sb.append(buildObjectFormDeclaration(sourceDomainReference, ReflectionForms.Reflection, MovabilityTypes.Movable, true));
    } else if (method.hasAnnotation(Movable.class)) {
      sb.append(buildObjectFormDeclaration(domainReturnType, ReflectionForms.Reflection, MovabilityTypes.Movable, true));
    } else if (method.hasAnnotation(Unmovable.class)) {
      sb.append(buildObjectFormDeclaration(domainReturnType, ReflectionForms.Reflection, MovabilityTypes.Unmovable, true));
    } else {
      sb.append(buildObjectFormDeclaration(domainReturnType, ReflectionForms.Reflection, MovabilityTypes.General, true));
    }
  }
}
