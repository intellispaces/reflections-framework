package tech.intellispaces.jaquarius.annotationprocessor.objecthandle;

import tech.intellispaces.commons.annotation.processor.ArtifactGeneratorContext;
import tech.intellispaces.commons.base.exception.NotImplementedExceptions;
import tech.intellispaces.commons.base.type.PrimitiveFunctions;
import tech.intellispaces.commons.java.reflection.customtype.CustomType;
import tech.intellispaces.commons.java.reflection.method.MethodStatement;
import tech.intellispaces.commons.java.reflection.reference.TypeReference;
import tech.intellispaces.jaquarius.annotation.Movable;
import tech.intellispaces.jaquarius.annotation.Ordinal;
import tech.intellispaces.jaquarius.annotation.Unmovable;
import tech.intellispaces.jaquarius.annotation.Wrapper;
import tech.intellispaces.jaquarius.channel.Channel0;
import tech.intellispaces.jaquarius.channel.Channel1;
import tech.intellispaces.jaquarius.channel.ChannelFunction0;
import tech.intellispaces.jaquarius.channel.ChannelFunction1;
import tech.intellispaces.jaquarius.channel.MappingChannel;
import tech.intellispaces.jaquarius.channel.MappingOfMovingChannel;
import tech.intellispaces.jaquarius.engine.JaquariusEngines;
import tech.intellispaces.jaquarius.engine.ObjectHandleBroker;
import tech.intellispaces.jaquarius.engine.description.ObjectHandleMethodPurposes;
import tech.intellispaces.jaquarius.engine.description.ObjectHandleTypeDescription;
import tech.intellispaces.jaquarius.exception.TraverseException;
import tech.intellispaces.jaquarius.naming.NameConventionFunctions;
import tech.intellispaces.jaquarius.object.reference.ObjectHandleType;
import tech.intellispaces.jaquarius.object.reference.ObjectHandleTypes;
import tech.intellispaces.jaquarius.space.channel.ChannelFunctions;
import tech.intellispaces.jaquarius.system.Modules;
import tech.intellispaces.jaquarius.system.ObjectHandleWrapper;
import tech.intellispaces.jaquarius.traverse.TraverseType;
import tech.intellispaces.jaquarius.traverse.TraverseTypes;

public class MovableObjectHandleWrapperGenerator extends ObjectHandleWrapperGenerator {

  public MovableObjectHandleWrapperGenerator(CustomType objectHandleType) {
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
    return "/movable_object_handle_wrapper.template";
  }

  @Override
  protected ObjectHandleType getObjectHandleType() {
    return ObjectHandleTypes.Movable;
  }

  @Override
  protected boolean analyzeSourceArtifact(ArtifactGeneratorContext context) {
    addImports(
        Ordinal.class,
        Wrapper.class,
        Modules.class,
        ObjectHandleWrapper.class,
        Channel0.class,
        Channel1.class,
        ChannelFunction0.class,
        ChannelFunction1.class,
        ChannelFunctions.class,
        MappingChannel.class,
        MappingOfMovingChannel.class,
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
    analyzeTypeParams();
    analyzeConstructors();
    analyzeInjectedGuides();
    analyzeObjectHandleMethods();
    analyzeConversionMethods(domainType);
    analyzeReleaseMethod();

    addVariable("typeParamsFull", typeParamsFull);
    addVariable("typeParamsBrief", typeParamsBrief);
    addVariable("domainClassSimpleName", domainSimpleClassName);
    addVariable("isAlias", isAlias);
    addVariable("primaryDomainSimpleName", primaryDomainSimpleName);
    addVariable("primaryDomainTypeArguments", primaryDomainTypeArguments);
    addVariable("constructors", constructors);
    addVariable("methodDescriptions", methodDescriptions);
    addVariable("guideActionMethods", guideMethods);
    addVariable("handleMethods", traverseMethods);
    addVariable("domainMethods", rawDomainMethods);
    addVariable("injectionMethods", injectionMethods);
    addVariable("conversionMethods", conversionMethods);
    addVariable("notImplRelease", !implRelease);
    return true;
  }

  protected void appendObjectFormMethodReturnType(StringBuilder sb, MethodStatement method) {
    TypeReference domainReturnType = method.returnType().orElseThrow();
    if (
        ChannelFunctions.getTraverseTypes(method).stream().anyMatch(TraverseType::isMoving) ||
            method.hasAnnotation(Movable.class) ||
            NameConventionFunctions.isConversionMethod(method)
    ) {
      sb.append(buildObjectHandleDeclaration(domainReturnType, ObjectHandleTypes.Movable));
    } else if (method.hasAnnotation(Unmovable.class)) {
      sb.append(buildObjectHandleDeclaration(domainReturnType, ObjectHandleTypes.Unmovable));
    } else {
      sb.append(buildObjectHandleDeclaration(domainReturnType, ObjectHandleTypes.General));
    }
  }
}
