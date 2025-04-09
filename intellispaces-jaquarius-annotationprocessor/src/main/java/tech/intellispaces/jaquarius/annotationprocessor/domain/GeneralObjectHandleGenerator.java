package tech.intellispaces.jaquarius.annotationprocessor.domain;

import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.commons.text.StringFunctions;
import tech.intellispaces.commons.type.Type;
import tech.intellispaces.commons.type.Types;
import tech.intellispaces.jaquarius.ArtifactType;
import tech.intellispaces.jaquarius.annotation.Channel;
import tech.intellispaces.jaquarius.annotation.ObjectHandle;
import tech.intellispaces.jaquarius.artifact.ArtifactTypes;
import tech.intellispaces.jaquarius.channel.Channel1;
import tech.intellispaces.jaquarius.exception.TraverseException;
import tech.intellispaces.jaquarius.naming.NameConventionFunctions;
import tech.intellispaces.jaquarius.object.reference.MovabilityType;
import tech.intellispaces.jaquarius.object.reference.MovabilityTypes;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForm;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForms;
import tech.intellispaces.jaquarius.space.channel.ChannelFunctions;
import tech.intellispaces.jaquarius.space.domain.DomainFunctions;
import tech.intellispaces.jaquarius.traverse.MappingTraverse;
import tech.intellispaces.jaquarius.traverse.TraverseType;
import tech.intellispaces.reflection.customtype.CustomType;
import tech.intellispaces.reflection.method.MethodStatement;
import tech.intellispaces.reflection.reference.CustomTypeReference;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class GeneralObjectHandleGenerator extends AbstractObjectGenerator {

  public GeneralObjectHandleGenerator(CustomType domainType) {
    super(domainType);
    addHiddenImport(NameConventionFunctions.getGeneralPlainObjectTypename(domainType.className()));
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
    return MovabilityTypes.General;
  }

  @Override
  protected List<ArtifactType> relatedArtifactTypes() {
    return List.of(ArtifactTypes.ObjectHandle);
  }

  @Override
  public String generatedArtifactName() {
    return NameConventionFunctions.getObjectTypename(sourceArtifact().className(), ObjectReferenceForms.ObjectHandle, MovabilityTypes.General, false);
  }

  @Override
  protected String templateName() {
    return "/general_object_handle.template";
  }

  @Override
  protected boolean analyzeSourceArtifact(ArtifactGeneratorContext context) {
    addImports(
        ObjectHandle.class,
        Type.class,
        Types.class,
        Channel1.class,
        MappingTraverse.class,
        TraverseException.class
    );
    addHiddenImports(context);

    analyzeDomain();
    analyzeAlias();
    analyzeObjectFormMethods(sourceArtifact(), context);

    addVariable("simpleHandleName", getSimpleHandleName());
    addVariable("movableClassSimpleName", movableClassSimpleName());
    addVariable("handleTypeParamsFull", typeParamsFull);
    addVariable("handleTypeParamsBrief", typeParamsBrief);
    addVariable("domainTypeParamsBrief", domainTypeParamsBrief);
    addVariable("domainType", domainType);
    addVariable("domainMethods", methods);
    addVariable("isAlias", isAlias);
    addVariable("primaryObjectHandle", baseObjectHandle);
    addVariable("primaryDomainTypeArguments", primaryDomainTypeArguments);
    addVariable("primaryDomainSimpleName", primaryDomainSimpleName);
    addVariable("simpleObject", getSimpleObjectClassName());
    return true;
  }

  private String getSimpleObjectClassName() {
    return addImportAndGetSimpleName(NameConventionFunctions.getGeneralPlainObjectTypename(sourceArtifact().className()));
  }

  private String getSimpleHandleName() {
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

      baseObjectHandle = buildObjectFormDeclaration(nearEquivalentDomain, ObjectReferenceForms.ObjectHandle, MovabilityTypes.General, true);
      primaryDomainTypeArguments = getDomainTypeParamsBrief(nearEquivalentDomain);
      primaryDomainSimpleName = addImportAndGetSimpleName(mainEquivalentDomain.targetType().canonicalName());
      domainType = buildDomainType(mainEquivalentDomain.targetType(), mainEquivalentDomain.typeArguments());
    } else {
      domainType = buildDomainType(sourceArtifact(), (List) sourceArtifact().typeParameters());
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
      MethodStatement method, ObjectReferenceForm targetForm, int methodOrdinal
  ) {
    if (method.hasAnnotation(Channel.class)) {
      return super.generateMethod(method, targetForm, methodOrdinal);
    } else {
      return buildExtensionMethod(method);
    }
  }

  private Map<String, String> buildExtensionMethod(MethodStatement method) {
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
