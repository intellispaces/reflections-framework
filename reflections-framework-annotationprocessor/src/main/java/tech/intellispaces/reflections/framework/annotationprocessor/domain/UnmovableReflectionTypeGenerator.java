package tech.intellispaces.reflections.framework.annotationprocessor.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.javareflection.customtype.CustomType;
import tech.intellispaces.javareflection.method.MethodSignatureDeclarations;
import tech.intellispaces.javareflection.method.MethodStatement;
import tech.intellispaces.javareflection.reference.CustomTypeReference;
import tech.intellispaces.reflections.framework.ArtifactType;
import tech.intellispaces.reflections.framework.annotation.Reflection;
import tech.intellispaces.reflections.framework.annotation.Unmovable;
import tech.intellispaces.reflections.framework.artifact.ArtifactTypes;
import tech.intellispaces.reflections.framework.naming.NameConventionFunctions;
import tech.intellispaces.reflections.framework.reflection.MovabilityType;
import tech.intellispaces.reflections.framework.reflection.MovabilityTypes;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.reflection.ReflectionForms;
import tech.intellispaces.reflections.framework.reflection.UnmovableReflection;
import tech.intellispaces.reflections.framework.space.channel.ChannelFunctions;
import tech.intellispaces.reflections.framework.space.domain.DomainFunctions;
import tech.intellispaces.reflections.framework.traverse.TraverseType;

public class UnmovableReflectionTypeGenerator extends AbstractReflectionFormGenerator {

  private final List<Map<String, String>> movableMethods = new ArrayList<>();

  public UnmovableReflectionTypeGenerator(CustomType domainType) {
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
    return MovabilityTypes.Unmovable;
  }

  @Override
  protected List<ArtifactType> relatedArtifactTypes() {
    return List.of(ArtifactTypes.UnmovableReflection);
  }

  @Override
  public String generatedArtifactName() {
    return NameConventionFunctions.getUnmovableReflectionTypeName(sourceArtifact().className(), false);
  }

  @Override
  protected String templateName() {
    return "/unmovable_reflection_type.template";
  }

  @Override
  protected boolean analyzeSourceArtifact(ArtifactGeneratorContext context) {
    addImports(
        Reflection.class,
        Unmovable.class,
        UnmovableReflection.class,
        UnexpectedExceptions.class
    );

    analyzeDomain();
    analyzeAlias();
    analyzeObjectFormMethods(sourceArtifact(), context);
    analyzeConversionMethods(sourceArtifact());
    analyzeMovableMethods(context);

    addVariable("movableClassSimpleName", movableClassSimpleName());
    addVariable("reflectionTypeParamsBrief", typeParamsBrief);
    addVariable("reflectionTypeParamsFull", typeParamsFull);
    addVariable("domainTypeParamsBrief", domainTypeParamsBrief);
    addVariable("generalReflection", generalReflection);
    addVariable("conversionMethods", conversionMethods);
    addVariable("isAlias", isAlias);
    addVariable("primaryReflection", baseReflection);
    addVariable("movableMethods", movableMethods);
    addVariable("simpleObject", getSimpleObjectClassName());
    return true;
  }

  private String getSimpleObjectClassName() {
    return addImportAndGetSimpleName(NameConventionFunctions.getUnmovableRegularFormTypeName(sourceArtifact().className(), false));
  }

  private void analyzeAlias() {
    Optional<CustomTypeReference> equivalentDomain = DomainFunctions.getAliasNearNeighbourDomain(sourceArtifact());
    isAlias = equivalentDomain.isPresent();
    if (isAlias) {
      baseReflection = buildObjectFormDeclaration(equivalentDomain.get(), ReflectionForms.Reflection, MovabilityTypes.Unmovable, true);
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
    var returnType = new StringBuilder();
    appendObjectFormMethodReturnType(returnType, method);

    String declaration = MethodSignatureDeclarations.build(method)
        .returnType(returnType.toString())
        .includeMethodTypeParams(true)
        .includeOwnerTypeParams(false)
        .get(this::addImport, this::addImportAndGetSimpleName);
    return Map.of(
        "javadoc", buildGeneratedMethodJavadoc(method.owner().canonicalName(), method),
        "declaration", declaration
    );
  }
}
