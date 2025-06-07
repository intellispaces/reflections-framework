package tech.intellispaces.reflections.framework.annotationprocessor.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.commons.text.StringFunctions;
import tech.intellispaces.commons.type.Type;
import tech.intellispaces.commons.type.Types;
import tech.intellispaces.javareflection.customtype.CustomType;
import tech.intellispaces.javareflection.instance.AnnotationInstance;
import tech.intellispaces.javareflection.method.MethodStatement;
import tech.intellispaces.javareflection.reference.CustomTypeReference;
import tech.intellispaces.javareflection.reference.NotPrimitiveReference;
import tech.intellispaces.javareflection.reference.TypeReferenceFunctions;
import tech.intellispaces.reflections.framework.ArtifactType;
import tech.intellispaces.reflections.framework.annotation.Channel;
import tech.intellispaces.reflections.framework.annotation.Customizer;
import tech.intellispaces.reflections.framework.annotation.Reflection;
import tech.intellispaces.reflections.framework.artifact.ArtifactTypes;
import tech.intellispaces.reflections.framework.channel.Channel1;
import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.naming.NameConventionFunctions;
import tech.intellispaces.reflections.framework.reflection.MovabilityType;
import tech.intellispaces.reflections.framework.reflection.MovabilityTypes;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.reflection.ReflectionForms;
import tech.intellispaces.reflections.framework.reflection.ReflectionFunctions;
import tech.intellispaces.reflections.framework.space.channel.ChannelFunctions;
import tech.intellispaces.reflections.framework.space.domain.DomainFunctions;
import tech.intellispaces.reflections.framework.traverse.MappingTraverse;
import tech.intellispaces.reflections.framework.traverse.TraverseType;

public class GeneralReflectionTypeGenerator extends AbstractReflectionFormGenerator {

  public GeneralReflectionTypeGenerator(CustomType domainType) {
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
    return MovabilityTypes.General;
  }

  @Override
  protected List<ArtifactType> relatedArtifactTypes() {
    return List.of(ArtifactTypes.Reflection);
  }

  @Override
  public String generatedArtifactName() {
    return NameConventionFunctions.getObjectTypename(
        sourceArtifact().canonicalName(),
        ReflectionForms.Reflection,
        MovabilityTypes.General,
        false
    );
  }

  @Override
  protected String templateName() {
    return "/general_reflection_type.template";
  }

  @Override
  protected boolean analyzeSourceArtifact(ArtifactGeneratorContext context) {
    addImports(
        Reflection.class,
        Type.class,
        Types.class,
        Channel1.class,
        MappingTraverse.class,
        TraverseException.class
    );

    analyzeDomain();
    analyzeAlias();
    analyzeObjectFormMethods(sourceArtifact(), context);

    addVariable("simpleReflectionName", getSimpleReflectionName());
    addVariable("movableClassSimpleName", movableClassSimpleName());
    addVariable("reflectionTypeParamsFull", typeParamsFull);
    addVariable("reflectionTypeParamsBrief", typeParamsBrief);
    addVariable("domainTypeParamsBrief", domainTypeParamsBrief);
    addVariable("domainType", domainTypename);
    addVariable("domainMethods", methods);
    addVariable("isAlias", isAlias);
    addVariable("primaryReflection", baseReflection);
    addVariable("primaryDomainTypeArguments", primaryDomainTypeArguments);
    addVariable("primaryDomainSimpleName", primaryDomainSimpleName);
    addVariable("simpleObject", getSimpleObjectClassName());
    addVariable("parents", getParents(context));
    return true;
  }

  private List<String> getParents(ArtifactGeneratorContext context) {
    var parents = new ArrayList<String>();
    for (CustomTypeReference parent : sourceArtifact().parentTypes()) {
      parents.add(
          ReflectionFunctions.getObjectFormDeclaration(
              parent, ReflectionForms.Reflection, MovabilityTypes.General, false, this::addImportAndGetSimpleName)
      );
    }
    addCustomizerParents(parents, context);
    return parents;
  }

  private void addCustomizerParents(List<String> parents, ArtifactGeneratorContext context) {
    List<CustomType> customizers = findCustomizers(sourceArtifact(), context.initialRoundEnvironment());
    for (CustomType customizer : customizers) {
      AnnotationInstance annotation = customizer.selectAnnotation(Customizer.class.getCanonicalName()).orElseThrow();
      CustomType domain = annotation.valueOf("origin").orElseThrow().asClass().orElseThrow().type();
      Map<String, NotPrimitiveReference> parentTypeArgumentMapping = TypeReferenceFunctions.getTypeArgumentMapping(
          sourceArtifact(), domain
      );
      for (CustomTypeReference customizerParent : customizer.parentTypes()) {
        parents.add(
            customizerParent.effective(parentTypeArgumentMapping).actualDeclaration(this::addImportAndGetSimpleName)
        );
      }
    }
  }

  private String getSimpleObjectClassName() {
    return addImportAndGetSimpleName(
        NameConventionFunctions.getGeneralRegularFormClassName(sourceArtifact().className(), false));
  }

  private String getSimpleReflectionName() {
    return StringFunctions.lowercaseFirstLetter(
        StringFunctions.removeTailOrElseThrow(sourceArtifactSimpleName(), "Domain"));
  }

  @SuppressWarnings("unchecked,rawtypes")
  private void analyzeAlias() {
    List<CustomTypeReference> equivalentDomains = DomainFunctions.getEquivalentDomains(sourceArtifact());
    isAlias = !equivalentDomains.isEmpty();
    if (isAlias) {
      CustomTypeReference nearEquivalentDomain = equivalentDomains.get(0);
      CustomTypeReference mainEquivalentDomain = equivalentDomains.get(equivalentDomains.size() - 1);

      baseReflection = buildObjectFormDeclaration(nearEquivalentDomain, ReflectionForms.Reflection, MovabilityTypes.General, true);
      primaryDomainTypeArguments = getDomainTypeParamsBrief(nearEquivalentDomain);
      primaryDomainSimpleName = addImportAndGetSimpleName(mainEquivalentDomain.targetType().canonicalName());
      domainTypename = buildDomainType(mainEquivalentDomain.targetType(), mainEquivalentDomain.typeArguments());
    } else {
      domainTypename = buildDomainType(sourceArtifact(), (List) sourceArtifact().typeParameters());
    }
  }

  @Override
  protected Stream<MethodStatement> getObjectFormMethods(
      CustomType customType, ArtifactGeneratorContext context
  ) {
    return buildActualType(customType, context)
        .actualMethods().stream()
        .filter(DomainFunctions::isNotDomainClassGetter)
        .filter(m -> excludeDeepConversionMethods(m, customType))
        .filter(m -> !ChannelFunctions.isChannelMethod(m)
            || ChannelFunctions.getTraverseTypes(m).stream().noneMatch(TraverseType::isMovingBased));
  }

  @Override
  protected Map<String, String> generateMethod(
          MethodStatement method, ReflectionForm targetForm, int methodOrdinal
  ) {
    if (method.hasAnnotation(Channel.class)) {
      return super.generateMethod(method, targetForm, methodOrdinal);
    } else {
      return buildCustomizerMethod(method);
    }
  }

  private Map<String, String> buildCustomizerMethod(MethodStatement method) {
    var sb = new StringBuilder();
    appendMethodTypeParameters(sb, method);
    appendMethodReturnType(sb, method);
    sb.append(" ");
    sb.append(method.name());
    sb.append("(");
    appendMethodParams(sb, method);
    sb.append(")");
    appendMethodExceptions(sb, method);
    return Map.of(
        "javadoc", buildGeneratedMethodJavadoc(method.owner().canonicalName(), method),
        "declaration", sb.toString()
    );
  }
}
