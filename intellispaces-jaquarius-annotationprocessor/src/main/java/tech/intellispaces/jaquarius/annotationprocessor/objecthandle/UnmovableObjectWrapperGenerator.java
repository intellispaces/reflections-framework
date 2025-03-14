package tech.intellispaces.jaquarius.annotationprocessor.objecthandle;

import tech.intellispaces.commons.annotation.processor.ArtifactGeneratorContext;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.commons.reflection.customtype.CustomType;
import tech.intellispaces.commons.reflection.method.MethodStatement;
import tech.intellispaces.commons.type.PrimitiveFunctions;
import tech.intellispaces.commons.type.Type;
import tech.intellispaces.commons.type.Types;
import tech.intellispaces.jaquarius.annotation.Ordinal;
import tech.intellispaces.jaquarius.annotation.Wrapper;
import tech.intellispaces.jaquarius.channel.Channel1;
import tech.intellispaces.jaquarius.channel.ChannelFunction0;
import tech.intellispaces.jaquarius.channel.ChannelFunction1;
import tech.intellispaces.jaquarius.engine.JaquariusEngines;
import tech.intellispaces.jaquarius.engine.ObjectHandleBroker;
import tech.intellispaces.jaquarius.engine.description.ObjectHandleMethodPurposes;
import tech.intellispaces.jaquarius.engine.description.ObjectHandleTypeDescription;
import tech.intellispaces.jaquarius.exception.TraverseException;
import tech.intellispaces.jaquarius.object.reference.MovabilityType;
import tech.intellispaces.jaquarius.object.reference.MovabilityTypes;
import tech.intellispaces.jaquarius.object.reference.MovableObjectHandle;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForm;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForms;
import tech.intellispaces.jaquarius.space.channel.ChannelFunctions;
import tech.intellispaces.jaquarius.system.Modules;
import tech.intellispaces.jaquarius.system.ObjectHandleWrapper;
import tech.intellispaces.jaquarius.traverse.MappingTraverse;
import tech.intellispaces.jaquarius.traverse.TraverseTypes;

import java.util.Map;

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
  protected boolean analyzeSourceArtifact(ArtifactGeneratorContext context) {
    addImports(
        Ordinal.class,
        Wrapper.class,
        Modules.class,
        Type.class,
        Types.class,
        ObjectHandleWrapper.class,
        MovableObjectHandle.class,
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
        UnexpectedExceptions.class
    );

    analyzeDomain();
    analyzeAlias();
    analyzeTypeParams();
    analyzeConstructors();
    analyzeInjectedGuides();
    analyzeObjectHandleMethods();
    analyzeConversionMethods(domainType);
    analyzeReleaseMethod();

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
