package tech.intellispaces.reflectionsframework.annotationprocessor.object.handle;

import java.util.List;

import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.commons.type.PrimitiveFunctions;
import tech.intellispaces.commons.type.Type;
import tech.intellispaces.commons.type.Types;
import tech.intellispaces.reflectionsframework.ArtifactType;
import tech.intellispaces.reflectionsframework.annotation.Movable;
import tech.intellispaces.reflectionsframework.annotation.Ordinal;
import tech.intellispaces.reflectionsframework.annotation.Unmovable;
import tech.intellispaces.reflectionsframework.annotation.Wrapper;
import tech.intellispaces.reflectionsframework.artifact.ArtifactTypes;
import tech.intellispaces.reflectionsframework.channel.Channel0;
import tech.intellispaces.reflectionsframework.channel.Channel1;
import tech.intellispaces.reflectionsframework.channel.ChannelFunction0;
import tech.intellispaces.reflectionsframework.channel.ChannelFunction1;
import tech.intellispaces.reflectionsframework.engine.JaquariusEngines;
import tech.intellispaces.reflectionsframework.engine.ObjectHandleBroker;
import tech.intellispaces.reflectionsframework.engine.description.ObjectHandleMethodPurposes;
import tech.intellispaces.reflectionsframework.engine.description.ObjectHandleTypeDescription;
import tech.intellispaces.reflectionsframework.exception.TraverseException;
import tech.intellispaces.reflectionsframework.naming.NameConventionFunctions;
import tech.intellispaces.reflectionsframework.object.reference.MovabilityType;
import tech.intellispaces.reflectionsframework.object.reference.MovabilityTypes;
import tech.intellispaces.reflectionsframework.object.reference.MovableObjectHandle;
import tech.intellispaces.reflectionsframework.object.reference.ObjectHandle;
import tech.intellispaces.reflectionsframework.object.reference.ObjectReferenceForm;
import tech.intellispaces.reflectionsframework.object.reference.ObjectReferenceForms;
import tech.intellispaces.reflectionsframework.object.reference.OverlyingHandleController;
import tech.intellispaces.reflectionsframework.object.reference.PostRegistrationHandleProcessor;
import tech.intellispaces.reflectionsframework.space.channel.ChannelFunctions;
import tech.intellispaces.reflectionsframework.system.Modules;
import tech.intellispaces.reflectionsframework.system.ObjectHandleWrapper;
import tech.intellispaces.reflectionsframework.traverse.MappingOfMovingTraverse;
import tech.intellispaces.reflectionsframework.traverse.MappingTraverse;
import tech.intellispaces.reflectionsframework.traverse.TraverseType;
import tech.intellispaces.reflectionsframework.traverse.TraverseTypes;
import tech.intellispaces.jstatements.customtype.CustomType;
import tech.intellispaces.jstatements.method.MethodStatement;
import tech.intellispaces.jstatements.reference.CustomTypeReferences;
import tech.intellispaces.jstatements.reference.TypeReference;

public class MovableObjectWrapperGenerator extends AbstractObjectWrapperGenerator {

  public MovableObjectWrapperGenerator(CustomType objectHandleType) {
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
    return "/movable_object_wrapper.template";
  }

  @Override
  protected ObjectReferenceForm getForm() {
    return ObjectReferenceForms.ObjectHandle;
  }

  @Override
  protected MovabilityType getMovabilityType() {
    return MovabilityTypes.Movable;
  }

  @Override
  protected List<ArtifactType> relatedArtifactTypes() {
    return List.of(ArtifactTypes.MovableObjectWrapper, ArtifactTypes.ObjectHandle, ArtifactTypes.RegularObject);
  }

  @Override
  protected boolean analyzeSourceArtifact(ArtifactGeneratorContext context) {
    addImports(
        Ordinal.class,
        Wrapper.class,
        Type.class,
        Types.class,
        Modules.class,
        ObjectHandle.class,
        ObjectHandleWrapper.class,
        MovableObjectHandle.class,
        OverlyingHandleController.class,
        PostRegistrationHandleProcessor.class,
        Channel0.class,
        Channel1.class,
        ChannelFunction0.class,
        ChannelFunction1.class,
        ChannelFunctions.class,
        MappingTraverse.class,
        MappingOfMovingTraverse.class,
        PrimitiveFunctions.class,
        ObjectHandleTypeDescription.class,
        ObjectHandleBroker.class,
        JaquariusEngines.class,
        TraverseTypes.class,
        ObjectHandleMethodPurposes.class,
        TraverseException.class,
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
    addVariable("domainClassSimpleName", domainSimpleClassName);
    addVariable("isAlias", isAlias);
    addVariable("primaryDomainSimpleName", primaryDomainSimpleName);
    addVariable("primaryDomainTypeArguments", primaryDomainTypeArguments);
    addVariable("primaryDomainTypeDeclaration", primaryDomainTypeDeclaration);
    addVariable("domainTypeDeclaration", domainTypeDeclaration);
    addVariable("domainTypeParamsBrief", domainTypeParamsBrief);
    addVariable("constructors", constructors);
    addVariable("methodDescriptions", methodDescriptions);
    addVariable("guideActionMethods", guideMethods);
    addVariable("handleMethods", traverseMethods);
    addVariable("injectionMethods", injectionMethods);
    addVariable("conversionMethods", conversionMethods);
    addVariable("notImplRelease", !implRelease);
    addVariable("objectHandleClassSimpleName", getObjectHandleSimpleName());
    return true;
  }

  protected void appendObjectFormMethodReturnType(StringBuilder sb, MethodStatement method) {
    TypeReference domainReturnType = method.returnType().orElseThrow();
    if (ChannelFunctions.getTraverseTypes(method).stream().anyMatch(TraverseType::isMoving)) {
      TypeReference dDomainTypeReference = CustomTypeReferences.get(domainType);
      sb.append(buildObjectFormDeclaration(dDomainTypeReference, ObjectReferenceForms.ObjectHandle, MovabilityTypes.Movable, true));
    } else if (method.hasAnnotation(Movable.class) || NameConventionFunctions.isConversionMethod(method)) {
      sb.append(buildObjectFormDeclaration(domainReturnType, ObjectReferenceForms.ObjectHandle, MovabilityTypes.Movable, true));
    } else if (method.hasAnnotation(Unmovable.class)) {
      sb.append(buildObjectFormDeclaration(domainReturnType, ObjectReferenceForms.ObjectHandle, MovabilityTypes.Unmovable, true));
    } else {
      sb.append(buildObjectFormDeclaration(domainReturnType, ObjectReferenceForms.ObjectHandle, MovabilityTypes.General, true));
    }
  }
}
