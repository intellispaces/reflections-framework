package tech.intellispaces.jaquarius.annotationprocessor.objecthandle;

import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.general.type.PrimitiveFunctions;
import tech.intellispaces.jaquarius.annotation.Ordinal;
import tech.intellispaces.jaquarius.annotation.Wrapper;
import tech.intellispaces.jaquarius.channel.Channel1;
import tech.intellispaces.jaquarius.channel.ChannelFunction0;
import tech.intellispaces.jaquarius.channel.ChannelFunction1;
import tech.intellispaces.jaquarius.channel.MappingChannel;
import tech.intellispaces.jaquarius.engine.JaquariusEngines;
import tech.intellispaces.jaquarius.engine.ObjectHandleBroker;
import tech.intellispaces.jaquarius.engine.description.ObjectHandleMethodPurposes;
import tech.intellispaces.jaquarius.engine.description.ObjectHandleTypeDescription;
import tech.intellispaces.jaquarius.exception.TraverseException;
import tech.intellispaces.jaquarius.object.reference.ObjectHandleMethodForm;
import tech.intellispaces.jaquarius.object.reference.ObjectHandleType;
import tech.intellispaces.jaquarius.object.reference.ObjectHandleTypes;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForm;
import tech.intellispaces.jaquarius.space.channel.ChannelFunctions;
import tech.intellispaces.jaquarius.system.Modules;
import tech.intellispaces.jaquarius.system.ObjectHandleWrapper;
import tech.intellispaces.jaquarius.traverse.TraverseTypes;
import tech.intellispaces.java.reflection.customtype.CustomType;
import tech.intellispaces.java.reflection.method.MethodStatement;

import java.util.Map;

public class UnmovableObjectHandleWrapperGenerator extends ObjectHandleWrapperGenerator {

  public UnmovableObjectHandleWrapperGenerator(CustomType objectHandleType) {
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
    return "/unmovable_object_handle_wrapper.template";
  }

  @Override
  protected ObjectHandleType getObjectHandleType() {
    return ObjectHandleTypes.Unmovable;
  }

  @Override
  protected boolean analyzeSourceArtifact(ArtifactGeneratorContext context) {
    addImports(
        Ordinal.class,
        Wrapper.class,
        Modules.class,
        ObjectHandleWrapper.class,
        Channel1.class,
        ChannelFunction0.class,
        ChannelFunction1.class,
        MappingChannel.class,
        PrimitiveFunctions.class,
        ObjectHandleTypeDescription.class,
        ObjectHandleBroker.class,
        JaquariusEngines.class,
        TraverseTypes.class,
        ObjectHandleMethodPurposes.class,
        TraverseException.class
    );

    analyzeDomain();
    analyzeTypeParams();
    analyzeConstructors();
    analyzeInjectedGuides();
    analyzeObjectHandleMethods();
    analyzeConversionMethods(domainType);
    analyzeReleaseMethod();

    addVariable("typeParamsFull", typeParamsFull);
    addVariable("typeParamsBrief", typeParamsBrief);
    addVariable("constructors", generatedConstructors);
    addVariable("methodDescriptions", generatedMethodDescriptions);
    addVariable("guideActionMethods", generatedGuideMethods);
    addVariable("domainMethods", generatedDomainMethods);
    addVariable("injectionMethods", generatedInjectionMethods);
    addVariable("conversionMethods", generatedConversionMethods);
    addVariable("notImplRelease", !implRelease);
    return true;
  }

  @Override
  protected Map<String, String> generateMethod(
      MethodStatement domainMethod, ObjectHandleMethodForm methodForm, ObjectReferenceForm targetForm, int methodOrdinal
  ) {
    if (ChannelFunctions.isMovingBasedChannel(domainMethod)) {
      return Map.of();
    }
    return super.generateMethod(domainMethod, methodForm, targetForm, methodOrdinal);
  }
}
