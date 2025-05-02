package tech.intellispaces.reflectionsj.annotationprocessor.object.handle;

import java.util.List;

import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.commons.type.PrimitiveFunctions;
import tech.intellispaces.commons.type.Type;
import tech.intellispaces.commons.type.Types;
import tech.intellispaces.reflectionsj.ArtifactType;
import tech.intellispaces.reflectionsj.annotation.Movable;
import tech.intellispaces.reflectionsj.annotation.Ordinal;
import tech.intellispaces.reflectionsj.annotation.Unmovable;
import tech.intellispaces.reflectionsj.annotation.Wrapper;
import tech.intellispaces.reflectionsj.artifact.ArtifactTypes;
import tech.intellispaces.reflectionsj.channel.Channel0;
import tech.intellispaces.reflectionsj.channel.Channel1;
import tech.intellispaces.reflectionsj.channel.ChannelFunction0;
import tech.intellispaces.reflectionsj.channel.ChannelFunction1;
import tech.intellispaces.reflectionsj.engine.JaquariusEngines;
import tech.intellispaces.reflectionsj.engine.ObjectHandleBroker;
import tech.intellispaces.reflectionsj.engine.description.ObjectHandleMethodPurposes;
import tech.intellispaces.reflectionsj.engine.description.ObjectHandleTypeDescription;
import tech.intellispaces.reflectionsj.exception.TraverseException;
import tech.intellispaces.reflectionsj.naming.NameConventionFunctions;
import tech.intellispaces.reflectionsj.object.reference.MovabilityType;
import tech.intellispaces.reflectionsj.object.reference.MovabilityTypes;
import tech.intellispaces.reflectionsj.object.reference.MovableObjectHandle;
import tech.intellispaces.reflectionsj.object.reference.ObjectHandle;
import tech.intellispaces.reflectionsj.object.reference.ObjectReferenceForm;
import tech.intellispaces.reflectionsj.object.reference.ObjectReferenceForms;
import tech.intellispaces.reflectionsj.object.reference.OverlyingHandleController;
import tech.intellispaces.reflectionsj.object.reference.PostRegistrationHandleProcessor;
import tech.intellispaces.reflectionsj.space.channel.ChannelFunctions;
import tech.intellispaces.reflectionsj.system.Modules;
import tech.intellispaces.reflectionsj.system.ObjectHandleWrapper;
import tech.intellispaces.reflectionsj.traverse.MappingOfMovingTraverse;
import tech.intellispaces.reflectionsj.traverse.MappingTraverse;
import tech.intellispaces.reflectionsj.traverse.TraverseType;
import tech.intellispaces.reflectionsj.traverse.TraverseTypes;
import tech.intellispaces.statementsj.customtype.CustomType;
import tech.intellispaces.statementsj.method.MethodStatement;
import tech.intellispaces.statementsj.reference.CustomTypeReferences;
import tech.intellispaces.statementsj.reference.TypeReference;

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
