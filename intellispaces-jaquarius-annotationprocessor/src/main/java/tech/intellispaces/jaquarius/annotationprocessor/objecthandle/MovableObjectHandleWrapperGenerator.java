package tech.intellispaces.jaquarius.annotationprocessor.objecthandle;

import tech.intellispaces.action.Actions;
import tech.intellispaces.action.functional.FunctionActions;
import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.general.exception.NotImplementedExceptions;
import tech.intellispaces.general.type.PrimitiveFunctions;
import tech.intellispaces.general.type.Type;
import tech.intellispaces.jaquarius.annotation.Movable;
import tech.intellispaces.jaquarius.annotation.Ordinal;
import tech.intellispaces.jaquarius.annotation.Unmovable;
import tech.intellispaces.jaquarius.annotation.Wrapper;
import tech.intellispaces.jaquarius.channel.Channel0;
import tech.intellispaces.jaquarius.channel.Channel1;
import tech.intellispaces.jaquarius.channel.ChannelMethod0;
import tech.intellispaces.jaquarius.channel.ChannelMethod1;
import tech.intellispaces.jaquarius.channel.MappingChannel;
import tech.intellispaces.jaquarius.channel.MappingOfMovingChannel;
import tech.intellispaces.jaquarius.engine.JaquariusEngines;
import tech.intellispaces.jaquarius.engine.ObjectHandleBroker;
import tech.intellispaces.jaquarius.engine.descriptor.ObjectHandleMethodPurposes;
import tech.intellispaces.jaquarius.engine.descriptor.ObjectHandleTypeDescriptor;
import tech.intellispaces.jaquarius.exception.TraverseException;
import tech.intellispaces.jaquarius.guide.n0.Mover0;
import tech.intellispaces.jaquarius.guide.n1.Mover1;
import tech.intellispaces.jaquarius.naming.NameConventionFunctions;
import tech.intellispaces.jaquarius.object.reference.ObjectHandleTypes;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForms;
import tech.intellispaces.jaquarius.space.channel.ChannelFunctions;
import tech.intellispaces.jaquarius.system.Modules;
import tech.intellispaces.jaquarius.system.ObjectHandleWrapper;
import tech.intellispaces.jaquarius.system.injection.AutoGuideInjections;
import tech.intellispaces.jaquarius.system.injection.GuideInjections;
import tech.intellispaces.jaquarius.traverse.TraverseType;
import tech.intellispaces.jaquarius.traverse.TraverseTypes;
import tech.intellispaces.java.reflection.customtype.CustomType;
import tech.intellispaces.java.reflection.method.MethodStatement;
import tech.intellispaces.java.reflection.reference.TypeReference;

import java.util.List;

public class MovableObjectHandleWrapperGenerator extends AbstractObjectHandleWrapperGenerator {

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
  protected ObjectHandleTypes getObjectHandleType() {
    return ObjectHandleTypes.Movable;
  }

  @Override
  protected boolean analyzeSourceArtifact(ArtifactGeneratorContext context) {
    addImport(List.class);

    addImport(Modules.class);
    addImport(TraverseException.class);
    addImport(Actions.class);
    addImport(FunctionActions.class);
    addImport(Type.class);
    addImport(Ordinal.class);
    addImport(Wrapper.class);
    addImport(ObjectHandleWrapper.class);
    addImport(ObjectReferenceForms.class);

    addImport(Mover0.class);
    addImport(Mover1.class);
    addImport(Channel0.class);
    addImport(Channel1.class);
    addImport(ChannelMethod0.class);
    addImport(ChannelMethod1.class);
    addImport(ChannelFunctions.class);
    addImport(GuideInjections.class);
    addImport(AutoGuideInjections.class);
    addImport(MappingChannel.class);
    addImport(MappingOfMovingChannel.class);
    addImport(NotImplementedExceptions.class);
    addImport(PrimitiveFunctions.class);
    addImport(ObjectHandleTypeDescriptor.class);
    addImport(ObjectHandleBroker.class);
    addImport(JaquariusEngines.class);
    addImport(TraverseTypes.class);
    addImport(ObjectHandleMethodPurposes.class);

    analyzeDomain();
    analyzeTypeParams();
    analyzeConstructors();
    analyzeInjectedGuides();
    analyzeObjectHandleMethods(sourceArtifact(), context);
    analyzeConversionMethods(domainType, context);
    analyzeReleaseMethod();

    addVariable("generatedAnnotation", makeGeneratedAnnotation());
    addVariable("typeParamsFull", typeParamsFull);
    addVariable("typeParamsBrief", typeParamsBrief);
    addVariable("domainClassSimpleName", domainSimpleClassName);
    addVariable("isAlias", isAlias);
    addVariable("primaryDomainSimpleName", primaryDomainSimpleName);
    addVariable("primaryDomainTypeArguments", primaryDomainTypeArguments);
    addVariable("constructors", constructors);
    addVariable("wrapperMethods", wrapperMethods);
    addVariable("guideActionMethods", guideMethods);
    addVariable("domainMethods", methods);
    addVariable("injectionMethods", injectionMethods);
    addVariable("conversionMethods", conversionMethods);
    addVariable("notImplRelease", !implRelease);
    return true;
  }

  protected void appendMethodReturnHandleType(StringBuilder sb, MethodStatement method) {
    TypeReference domainReturnType = method.returnType().orElseThrow();
    if (
        ChannelFunctions.getTraverseTypes(method).stream().anyMatch(TraverseType::isMoving)
          || method.hasAnnotation(Movable.class)
          || NameConventionFunctions.isConversionMethod(method)
    ) {
      sb.append(getObjectHandleDeclaration(domainReturnType, ObjectHandleTypes.Movable));
    } else if (method.hasAnnotation(Unmovable.class)) {
      sb.append(getObjectHandleDeclaration(domainReturnType, ObjectHandleTypes.Unmovable));
    } else {
      sb.append(getObjectHandleDeclaration(domainReturnType, ObjectHandleTypes.Common));
    }
  }
}
