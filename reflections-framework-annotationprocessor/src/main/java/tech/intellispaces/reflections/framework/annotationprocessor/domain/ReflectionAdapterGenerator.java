package tech.intellispaces.reflections.framework.annotationprocessor.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.jetbrains.annotations.Nullable;

import tech.intellispaces.actions.runnable.RunnableAction;
import tech.intellispaces.actions.text.StringActions;
import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.commons.data.Base64Functions;
import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.commons.type.Type;
import tech.intellispaces.commons.type.Types;
import tech.intellispaces.core.Domains;
import tech.intellispaces.core.Projection;
import tech.intellispaces.core.Reflection;
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
import tech.intellispaces.reflections.framework.ArtifactType;
import tech.intellispaces.reflections.framework.annotation.Channel;
import tech.intellispaces.reflections.framework.annotation.Name;
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
import tech.intellispaces.reflections.framework.space.channel.ChannelFunctions;
import tech.intellispaces.reflections.framework.space.domain.DomainFunctions;
import tech.intellispaces.reflections.framework.system.Modules;
import tech.intellispaces.reflections.framework.traverse.MappingTraverse;
import tech.intellispaces.reflections.framework.traverse.TraverseType;

public class ReflectionAdapterGenerator extends AbstractReflectionFormGenerator {
  private String typeParamsBrief;
  private boolean isAlias;
  private String domainTypename;

  public ReflectionAdapterGenerator(CustomType domainType) {
    super(domainType);
  }

  @Override
  public boolean isRelevant(ArtifactGeneratorContext context) {
    return true;
  }

  @Override
  public String generatedArtifactName() {
    return NameConventionFunctions.getReflectionAdapterClassName(sourceArtifact().className());
  }

  @Override
  protected String templateName() {
    return "/reflection_adapter.template";
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
  protected boolean analyzeSourceArtifact(ArtifactGeneratorContext context) {
    if (sourceArtifact().isNested()) {
      addImport(sourceArtifactName());
    }
    addImports(
        DomainFunctions.class,
        Name.class,
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
        ReflectionDomain.class,
        Rids.class,
        Domains.class,
        Base64Functions.class,
        Projection.class,
        Reflection.class,
        ReflectionPoint.class,
        ReflectionSpace.class,
        ReflectionChannel.class,
        List.class,
        Nullable.class
    );

    analyzeAlias();
    analyzeDomain();
    analyzeTypeParams();
    analyzeObjectFormMethods(sourceArtifact(), context);

    addVariable("didBase64", Base64Functions.createUrlNoPadding(domainRid.raw()));
    addVariable("didOrigin", domainRid.toString());
    addVariable("isAbstract", hasAbstractCustomizerParentMethods(context));
    addVariable("domainName", DomainFunctions.getDomainName(domainType));
    addVariable("reflectionClassName", NameConventionFunctions.getUnmovableReflectionTypeName(sourceArtifact().className(), false));
    addVariable("movableReflectionClassName", NameConventionFunctions.getMovableReflectionTypeName(sourceArtifact().className(), false));
    addVariable("typeParamsBrief", typeParamsBrief);
    addVariable("domainMethods", methods);
    addVariable("domainType", domainTypename);
    addVariable("typeParamsFull", typeParamsFull);
    addVariable("typeParamsBrief", typeParamsBrief);
    return true;
  }

  private boolean hasAbstractCustomizerParentMethods(ArtifactGeneratorContext context) {
    // It is not known how to implement the abstract methods of customizer class parents
    return hasAbstractCustomizerParentMethods(sourceArtifact(), context, new HashSet<>());
  }

  private boolean hasAbstractCustomizerParentMethods(
      CustomType domainType, ArtifactGeneratorContext context, Set<String> history
  ) {
    if (history.contains(domainType.canonicalName())) {
      return false;
    }
    List<CustomType> customizers = findCustomizers(domainType, context.initialRoundEnvironment());
    for (CustomType customizer : customizers) {
      for (CustomTypeReference customizerParent : customizer.parentTypes()) {
        for (MethodStatement method : customizerParent.targetType().actualMethods()) {
          if (method.isAbstract()) {
            return true;
          }
        }
      }
    }
    for (CustomTypeReference parent : domainType.parentTypes()) {
      if (hasAbstractCustomizerParentMethods(parent.targetType(), context, history)) {
        return true;
      }
    }
    history.add(domainType.canonicalName());
    return false;
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
      return generateChannelMethod(method, targetForm, methodOrdinal);
    } else {
      return buildCustomizerMethod(method);
    }
  }

  private Map<String, String> generateChannelMethod(
      MethodStatement method, ReflectionForm targetForm, int methodOrdinal
  ) {
    var sb = new StringBuilder();
    sb.append("public ");
    appendMethodTypeParameters(sb, method);
    appendMethodReturnTypeDeclaration(sb, method, targetForm);
    sb.append(" ");
    sb.append(getObjectFormMethodName(method, targetForm));
    sb.append("(");
    appendMethodParams(sb, method);
    sb.append(")");
    appendMethodExceptions(sb, method);
    sb.append(" {\n");
    if (ReflectionForms.Reflection.is(targetForm)) {
      Rid cid = ChannelFunctions.getChannelId(method);
      sb.append("""
        %s projection = projectionThru(%s.create("%s"));
        if (projection.isFocused()) {
          return (%s) projection.asFocused().target();
        } else if (projection.isFuzzy()) {
          return (%s) projection.asFuzzy().mostLikelyProjection().target();
        }
        throw %s.withCode("hmTi6A");
        """.formatted(
            addImportAndGetSimpleName(Projection.class),
            addImportAndGetSimpleName(Rids.class),
            cid,
            buildReturnType(method, targetForm),
            buildReturnType(method, targetForm),
            addImportAndGetSimpleName(NotImplementedExceptions.class)
      ));
    } else {
      sb.append("    throw ")
          .append(addImportAndGetSimpleName((NotImplementedExceptions.class))).append(
              ".withCode(\"1J9WMA\");\n");
    }
    sb.append("}");

    return Map.of("declaration", sb.toString());
  }

  private String buildReturnType(MethodStatement method, ReflectionForm targetForm) {
    var sb = new StringBuilder();
    appendMethodReturnTypeDeclaration(sb, method, targetForm);
    return sb.toString();
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
    return Map.of("declaration", sb.toString());
  }

  private void analyzeTypeParams() {
    if (sourceArtifact().typeParameters().isEmpty()) {
      typeParamsFull = typeParamsBrief = "";
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
    typeParamsFull = typeParamsFullBuilder.toString();
    typeParamsBrief = typeParamsBriefBuilder.toString();
  }
}
