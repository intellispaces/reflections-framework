package tech.intellispaces.reflectionsj.annotationprocessor.object.handle;

import java.util.List;
import java.util.Map;

import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.commons.type.PrimitiveFunctions;
import tech.intellispaces.commons.type.Type;
import tech.intellispaces.commons.type.Types;
import tech.intellispaces.reflectionsj.ArtifactType;
import tech.intellispaces.reflectionsj.annotation.Ordinal;
import tech.intellispaces.reflectionsj.annotation.Wrapper;
import tech.intellispaces.reflectionsj.artifact.ArtifactTypes;
import tech.intellispaces.reflectionsj.channel.Channel1;
import tech.intellispaces.reflectionsj.channel.ChannelFunction0;
import tech.intellispaces.reflectionsj.channel.ChannelFunction1;
import tech.intellispaces.reflectionsj.engine.JaquariusEngines;
import tech.intellispaces.reflectionsj.engine.ObjectHandleBroker;
import tech.intellispaces.reflectionsj.engine.description.ObjectHandleMethodPurposes;
import tech.intellispaces.reflectionsj.engine.description.ObjectHandleTypeDescription;
import tech.intellispaces.reflectionsj.exception.TraverseException;
import tech.intellispaces.reflectionsj.object.reference.MovabilityType;
import tech.intellispaces.reflectionsj.object.reference.MovabilityTypes;
import tech.intellispaces.reflectionsj.object.reference.MovableObjectHandle;
import tech.intellispaces.reflectionsj.object.reference.ObjectHandle;
import tech.intellispaces.reflectionsj.object.reference.ObjectReferenceForm;
import tech.intellispaces.reflectionsj.object.reference.ObjectReferenceForms;
import tech.intellispaces.reflectionsj.object.reference.OverlyingHandleController;
import tech.intellispaces.reflectionsj.object.reference.UnmovableObjectHandle;
import tech.intellispaces.reflectionsj.space.channel.ChannelFunctions;
import tech.intellispaces.reflectionsj.system.Modules;
import tech.intellispaces.reflectionsj.system.ObjectHandleWrapper;
import tech.intellispaces.reflectionsj.traverse.MappingTraverse;
import tech.intellispaces.reflectionsj.traverse.TraverseTypes;
import tech.intellispaces.statementsj.customtype.CustomType;
import tech.intellispaces.statementsj.method.MethodStatement;

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
