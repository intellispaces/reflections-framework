package tech.intellispaces.reflections.framework.annotationprocessor.dataset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import tech.intellispaces.actions.runnable.RunnableAction;
import tech.intellispaces.actions.text.StringActions;
import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.commons.collection.ArraysFunctions;
import tech.intellispaces.commons.data.Base64Functions;
import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.commons.type.Type;
import tech.intellispaces.commons.type.Types;
import tech.intellispaces.core.Domains;
import tech.intellispaces.core.Projection;
import tech.intellispaces.core.Projections;
import tech.intellispaces.core.ReflectionChannel;
import tech.intellispaces.core.ReflectionDomain;
import tech.intellispaces.core.ReflectionPoint;
import tech.intellispaces.core.ReflectionSpace;
import tech.intellispaces.core.Rid;
import tech.intellispaces.core.Rids;
import tech.intellispaces.javareflection.customtype.CustomType;
import tech.intellispaces.javareflection.method.MethodStatement;
import tech.intellispaces.javareflection.reference.CustomTypeReference;
import tech.intellispaces.javareflection.reference.NamedReference;
import tech.intellispaces.javareflection.reference.TypeReference;
import tech.intellispaces.reflections.framework.ArtifactType;
import tech.intellispaces.reflections.framework.annotation.Channel;
import tech.intellispaces.reflections.framework.annotation.Name;
import tech.intellispaces.reflections.framework.annotation.Reflection;
import tech.intellispaces.reflections.framework.annotationprocessor.domain.AbstractReflectionFormGenerator;
import tech.intellispaces.reflections.framework.artifact.ArtifactTypes;
import tech.intellispaces.reflections.framework.channel.Channel1;
import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.naming.NameConventionFunctions;
import tech.intellispaces.reflections.framework.reflection.MovabilityType;
import tech.intellispaces.reflections.framework.reflection.MovabilityTypes;
import tech.intellispaces.reflections.framework.reflection.MovableReflection;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.reflection.ReflectionForms;
import tech.intellispaces.reflections.framework.reflection.SystemReflection;
import tech.intellispaces.reflections.framework.space.domain.DomainFunctions;
import tech.intellispaces.reflections.framework.system.Modules;
import tech.intellispaces.reflections.framework.traverse.MappingTraverse;
import tech.intellispaces.reflections.framework.traverse.TraverseTypes;

public class DatasetGenerator extends AbstractReflectionFormGenerator {
  private String typeParamsBrief;
  private boolean isAlias;
  private String domainTypename;
  private final List<Map<String, String>> projectionProperties = new ArrayList<>();

  public DatasetGenerator(CustomType dataType) {
    super(dataType);
  }

  @Override
  public boolean isRelevant(ArtifactGeneratorContext context) {
    return true;
  }

  @Override
  public String generatedArtifactName() {
    return NameConventionFunctions.getUnmovableDatasetClassName(sourceArtifact().className());
  }

  @Override
  protected String templateName() {
    return "/unmovable_dataset.template";
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
    return List.of(ArtifactTypes.UnmovableDataset, ArtifactTypes.Reflection);
  }

  @Override
  protected boolean analyzeSourceArtifact(ArtifactGeneratorContext context) {
    if (sourceArtifact().isNested()) {
      addImport(sourceArtifactName());
    }
    addImports(
        Name.class,
        Reflection.class,
        Objects.class,
        Modules.class,
        Type.class,
        Types.class,
        Channel1.class,
        MappingTraverse.class,
        TraverseException.class,
        SystemReflection.class,
        MovableReflection.class,
        NotImplementedExceptions.class,
        UnexpectedExceptions.class,
        Rid.class,
        Rids.class,
        Domains.class,
        Base64Functions.class,
        Projection.class,
        ReflectionPoint.class,
        ReflectionSpace.class,
        ReflectionDomain.class,
        ReflectionChannel.class,
        Projections.class,
        List.class
    );

    analyzeAlias();
    analyzeDomain();
    analyzeTypeParams();
    analyzeProjections();

    addVariable("didBase64", Base64Functions.createUrlNoPadding(domainRid.raw()));
    addVariable("didOrigin", domainRid.toString());
    addVariable("domainName", DomainFunctions.getDomainName(domainType));
    addVariable("reflectionClassName", NameConventionFunctions.getUnmovableReflectionTypeName(sourceArtifact().className(), true));
    addVariable("movableReflectionClassName", NameConventionFunctions.getMovableReflectionTypeName(sourceArtifact().className(), true));
    addVariable("typeParamsBrief", typeParamsBrief);
    addVariable("projections", projectionProperties);
    addVariable("domainType", domainTypename);
    return true;
  }

  @SuppressWarnings("unchecked,rawtypes")
  private void analyzeAlias() {
    Optional<CustomTypeReference> mainEquivalentDomain = DomainFunctions.getAliasBaseDomain(sourceArtifact());
    isAlias = mainEquivalentDomain.isPresent();
    if (isAlias) {
      domainTypename = buildDomainType(mainEquivalentDomain.get().targetType(), mainEquivalentDomain.get().typeArguments());
    } else {
      domainTypename = buildDomainType(sourceArtifact(), (List) sourceArtifact().typeParameters());
    }
  }

  @Override
  protected void analyzeDomain() {
    domainType = sourceArtifact();
    domainRid = DomainFunctions.getDomainId(domainType);
  }

  private void analyzeProjections() {
    for (MethodStatement method : sourceArtifact().actualMethods()) {
      if (isMovingMethod(method)) {
        continue;
      }
      TypeReference type = method.returnType().orElseThrow();
      String reflectionType = buildObjectFormDeclaration(type, ReflectionForms.Reflection, MovabilityTypes.General, true);

      Map<String, String> properties = new HashMap<>();
      properties.put("type", reflectionType);
      properties.put("name", method.name());
      projectionProperties.add(properties);
    }
  }

  private boolean isMovingMethod(MethodStatement method) {
    Optional<Channel> channel = method.selectAnnotation(Channel.class);
    if (channel.isEmpty()) {
      return true;
    }
    return ArraysFunctions.containsAny(
        channel.orElseThrow().allowedTraverse(), TraverseTypes.Moving, TraverseTypes.MappingOfMoving
    );
  }

  protected void analyzeTypeParams() {
    if (sourceArtifact().typeParameters().isEmpty()) {
      typeParamsBrief = "";
      return;
    }

    var typeParamsFullBuilder = new StringBuilder();
    var typeParamsBriefBuilder = new StringBuilder();
    RunnableAction commaAppender = StringActions.skipFirstTimeCommaAppender(typeParamsFullBuilder, typeParamsBriefBuilder);

    typeParamsFullBuilder.append("<");
    typeParamsBriefBuilder.append("<");
    for (NamedReference typeParam : sourceArtifact().typeParameters()) {
      commaAppender.run();
      typeParamsFullBuilder.append(typeParam.formalFullDeclaration());
      typeParamsBriefBuilder.append(typeParam.formalBriefDeclaration());
    }
    typeParamsFullBuilder.append(">");
    typeParamsBriefBuilder.append(">");
    typeParamsBrief = typeParamsBriefBuilder.toString();
  }
}
