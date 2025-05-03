package tech.intellispaces.reflections.annotationprocessor.object.handle;

import java.util.List;
import java.util.Map;

import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.commons.type.PrimitiveFunctions;
import tech.intellispaces.commons.type.Type;
import tech.intellispaces.commons.type.Types;
import tech.intellispaces.reflections.ArtifactType;
import tech.intellispaces.reflections.annotation.Ordinal;
import tech.intellispaces.reflections.annotation.Wrapper;
import tech.intellispaces.reflections.artifact.ArtifactTypes;
import tech.intellispaces.reflections.channel.Channel1;
import tech.intellispaces.reflections.channel.ChannelFunction0;
import tech.intellispaces.reflections.channel.ChannelFunction1;
import tech.intellispaces.reflections.engine.JaquariusEngines;
import tech.intellispaces.reflections.engine.ObjectHandleBroker;
import tech.intellispaces.reflections.engine.description.ObjectHandleMethodPurposes;
import tech.intellispaces.reflections.engine.description.ObjectHandleTypeDescription;
import tech.intellispaces.reflections.exception.TraverseException;
import tech.intellispaces.reflections.object.reference.MovabilityType;
import tech.intellispaces.reflections.object.reference.MovabilityTypes;
import tech.intellispaces.reflections.object.reference.MovableObjectHandle;
import tech.intellispaces.reflections.object.reference.ObjectHandle;
import tech.intellispaces.reflections.object.reference.ObjectReferenceForm;
import tech.intellispaces.reflections.object.reference.ObjectReferenceForms;
import tech.intellispaces.reflections.object.reference.OverlyingHandleController;
import tech.intellispaces.reflections.object.reference.UnmovableObjectHandle;
import tech.intellispaces.reflections.space.channel.ChannelFunctions;
import tech.intellispaces.reflections.system.Modules;
import tech.intellispaces.reflections.system.ObjectHandleWrapper;
import tech.intellispaces.reflections.traverse.MappingTraverse;
import tech.intellispaces.reflections.traverse.TraverseTypes;
import tech.intellispaces.jstatements.customtype.CustomType;
import tech.intellispaces.jstatements.method.MethodStatement;

public class UnmovableObjectWrapperGenerator extends AbstractObjectWrapperGenerator {

  public UnmovableObjectWrapperGenerator(CustomType objectHandleType) {
    super(objectHandleType);
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
    return "/unmovable_object_wrapper.template";
  }

  @Override
  protected ObjectReferenceForm getForm() {
    return ObjectReferenceForms.ObjectHandle;
  }

  @Override
  protected MovabilityType getMovabilityType() {
    return MovabilityTypes.Unmovable;
  }

  @Override
  protected List<ArtifactType> relatedArtifactTypes() {
    return List.of(ArtifactTypes.UnmovableObjectWrapper, ArtifactTypes.ObjectHandle, ArtifactTypes.RegularObject);
  }

  @Override
  protected boolean analyzeSourceArtifact(ArtifactGeneratorContext context) {
    addImports(
        Ordinal.class,
        Wrapper.class,
        Modules.class,
        Type.class,
        Types.class,
        ObjectHandle.class,
        ObjectHandleWrapper.class,
        MovableObjectHandle.class,
        UnmovableObjectHandle.class,
        OverlyingHandleController.class,
        Channel1.class,
        ChannelFunction0.class,
        ChannelFunction1.class,
        MappingTraverse.class,
        PrimitiveFunctions.class,
        ObjectHandleTypeDescription.class,
        ObjectHandleBroker.class,
        JaquariusEngines.class,
        TraverseTypes.class,
        ObjectHandleMethodPurposes.class,
        TraverseException.class,
        UnexpectedExceptions.class,
        NotImplementedExceptions.class
    );

    analyzeDomain();
    analyzeAlias();
    analyzeTypeParams();
    analyzeConstructors();
    analyzeInjectedGuides();
    analyzeObjectHandleMethods();
    analyzeConversionMethods(domainType);
    analyzeUnbindMethod();

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
    addVariable("handleMethods", traverseMethods);
    addVariable("domainMethods", rawDomainMethods);
    addVariable("injectionMethods", injectionMethods);
    addVariable("conversionMethods", conversionMethods);
    addVariable("notImplRelease", !implRelease);
    addVariable("objectHandleClassSimpleName", getObjectHandleSimpleName());
    addVariable("movableObjectHandleClassSimpleName", getMovableObjectHandleSimpleName());
    return true;
  }

  @Override
  protected Map<String, String> generateMethod(
      MethodStatement domainMethod, ObjectReferenceForm targetForm, int methodOrdinal
  ) {
    if (ChannelFunctions.isMovingBasedChannel(domainMethod)) {
      return Map.of();
    }
    return super.generateMethod(domainMethod,  targetForm, methodOrdinal);
  }
}
