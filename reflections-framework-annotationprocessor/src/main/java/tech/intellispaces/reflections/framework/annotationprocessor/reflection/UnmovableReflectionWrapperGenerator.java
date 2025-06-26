package tech.intellispaces.reflections.framework.annotationprocessor.reflection;

import java.util.List;
import java.util.Map;

import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.commons.data.Base64Functions;
import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.commons.type.PrimitiveFunctions;
import tech.intellispaces.commons.type.Type;
import tech.intellispaces.commons.type.Types;
import tech.intellispaces.core.Domain;
import tech.intellispaces.core.Domains;
import tech.intellispaces.core.Projection;
import tech.intellispaces.core.ReflectionType;
import tech.intellispaces.core.ReflectionTypes;
import tech.intellispaces.core.Rid;
import tech.intellispaces.core.Rids;
import tech.intellispaces.javareflection.customtype.CustomType;
import tech.intellispaces.javareflection.method.MethodStatement;
import tech.intellispaces.reflections.framework.ArtifactType;
import tech.intellispaces.reflections.framework.annotation.Ordinal;
import tech.intellispaces.reflections.framework.annotation.Wrapper;
import tech.intellispaces.reflections.framework.artifact.ArtifactTypes;
import tech.intellispaces.reflections.framework.channel.Channel1;
import tech.intellispaces.reflections.framework.channel.ChannelFunction0;
import tech.intellispaces.reflections.framework.channel.ChannelFunction1;
import tech.intellispaces.reflections.framework.engine.EngineRegistrar;
import tech.intellispaces.reflections.framework.engine.Engines;
import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.node.ReflectionsNodeFunctions;
import tech.intellispaces.reflections.framework.reflection.MovabilityType;
import tech.intellispaces.reflections.framework.reflection.MovabilityTypes;
import tech.intellispaces.reflections.framework.reflection.MovableReflection;
import tech.intellispaces.reflections.framework.reflection.OverlyingReflectionController;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.reflection.ReflectionForms;
import tech.intellispaces.reflections.framework.reflection.ReflectionHandle;
import tech.intellispaces.reflections.framework.reflection.ReflectionRealizationMethodPurposes;
import tech.intellispaces.reflections.framework.reflection.ReflectionRealizationType;
import tech.intellispaces.reflections.framework.reflection.ReflectionRealizationTypes;
import tech.intellispaces.reflections.framework.reflection.SystemReflection;
import tech.intellispaces.reflections.framework.space.channel.ChannelFunctions;
import tech.intellispaces.reflections.framework.space.domain.DomainFunctions;
import tech.intellispaces.reflections.framework.system.Modules;
import tech.intellispaces.reflections.framework.system.ReflectionWrapper;
import tech.intellispaces.reflections.framework.traverse.MappingTraverse;
import tech.intellispaces.reflections.framework.traverse.TraverseTypes;

public class UnmovableReflectionWrapperGenerator extends AbstractReflectionWrapperGenerator {

  public UnmovableReflectionWrapperGenerator(CustomType reflectionType) {
    super(reflectionType);
  }

  @Override
  public boolean isRelevant(ArtifactGeneratorContext context) {
    return true;
  }

  @Override
  public String generatedArtifactName() {
    return getGeneratedClassCanonicalName();
  }

  @Override
  protected String templateName() {
    return "/unmovable_reflection_wrapper.template";
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
    return List.of(ArtifactTypes.UnmovableObjectWrapper, ArtifactTypes.Reflection);
  }

  @Override
  protected boolean analyzeSourceArtifact(ArtifactGeneratorContext context) {
    addImports(
        Ordinal.class,
        Wrapper.class,
        Modules.class,
        Type.class,
        Types.class,
        ReflectionWrapper.class,
        MovableReflection.class,
        SystemReflection.class,
        OverlyingReflectionController.class,
        Channel1.class,
        ChannelFunction0.class,
        ChannelFunction1.class,
        MappingTraverse.class,
        PrimitiveFunctions.class,
        ReflectionRealizationType.class,
        ReflectionHandle.class,
        Engines.class,
        TraverseTypes.class,
        ReflectionRealizationMethodPurposes.class,
        TraverseException.class,
        UnexpectedExceptions.class,
        NotImplementedExceptions.class,
        ReflectionRealizationTypes.class,
        EngineRegistrar.class,
        ReflectionsNodeFunctions.class,
        Rid.class,
        Domain.class,
        Rids.class,
        Domains.class,
        Base64Functions.class,
        Projection.class,
        ReflectionType.class,
        ReflectionTypes.class
    );

    analyzeDomain();
    analyzeAlias();
    analyzeTypeParams();
    analyzeConstructors();
    analyzeInjectedGuides();
    analyzeReflectionMethods();
    analyzeUnbindMethod();

    addVariable("didBase64", Base64Functions.createUrlNoPadding(domainRid.raw()));
    addVariable("didOrigin", domainRid.toString());
    addVariable("domainName", DomainFunctions.getDomainName(domainType));
    addVariable("typeParamsFull", typeParamsFull);
    addVariable("typeParamsBrief", typeParamsBrief);
    addVariable("isAlias", isAlias);
    addVariable("primaryDomainSimpleName", primaryDomainSimpleName);
    addVariable("primaryDomainTypeArguments", primaryDomainTypeArguments);
    addVariable("domainClassSimpleName", domainSimpleClassName);
    addVariable("primaryDomainTypeDeclaration", primaryDomainTypeDeclaration);
    addVariable("domainTypeDeclaration", domainTypeDeclaration);
    addVariable("domainTypeParamsBrief", domainTypeParamsBrief);
    addVariable("constructors", constructors);
    addVariable("methodDescriptions", methodDescriptions);
    addVariable("guideActionMethods", guideMethods);
    addVariable("traverseMethods", traverseMethods);
    addVariable("domainMethods", rawDomainMethods);
    addVariable("injectionMethods", injectionMethods);
    addVariable("conversionMethods", conversionMethods);
    addVariable("notImplRelease", !implRelease);
    addVariable("reflectionClassSimpleName", getReflectionSimpleName());
    addVariable("movableReflectionClassSimpleName", getMovableReflectionSimpleName());
    return true;
  }

  @Override
  protected Map<String, String> generateMethod(
          MethodStatement domainMethod, ReflectionForm targetForm, int methodOrdinal
  ) {
    if (ChannelFunctions.isMovingBasedChannel(domainMethod)) {
      return Map.of();
    }
    return super.generateMethod(domainMethod,  targetForm, methodOrdinal);
  }
}
