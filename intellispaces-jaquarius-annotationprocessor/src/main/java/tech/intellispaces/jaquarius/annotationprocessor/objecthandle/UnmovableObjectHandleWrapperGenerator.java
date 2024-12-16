package tech.intellispaces.jaquarius.annotationprocessor.objecthandle;

import tech.intellispaces.action.Actions;
import tech.intellispaces.action.functional.FunctionActions;
import tech.intellispaces.action.supplier.ResettableSupplierAction;
import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.general.type.PrimitiveFunctions;
import tech.intellispaces.jaquarius.annotation.Ordinal;
import tech.intellispaces.jaquarius.annotation.Wrapper;
import tech.intellispaces.jaquarius.channel.Channel1;
import tech.intellispaces.jaquarius.channel.ChannelMethod0;
import tech.intellispaces.jaquarius.channel.ChannelMethod1;
import tech.intellispaces.jaquarius.channel.MappingChannel;
import tech.intellispaces.jaquarius.engine.JaquariusEngines;
import tech.intellispaces.jaquarius.engine.ObjectHandleBroker;
import tech.intellispaces.jaquarius.engine.descriptor.ObjectHandleMethodPurposes;
import tech.intellispaces.jaquarius.engine.descriptor.ObjectHandleTypeDescriptor;
import tech.intellispaces.jaquarius.exception.TraverseException;
import tech.intellispaces.jaquarius.guide.n0.Mapper0;
import tech.intellispaces.jaquarius.guide.n1.Mapper1;
import tech.intellispaces.jaquarius.object.reference.ObjectHandleTypes;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForms;
import tech.intellispaces.jaquarius.space.channel.ChannelFunctions;
import tech.intellispaces.jaquarius.system.Modules;
import tech.intellispaces.jaquarius.system.ObjectHandleWrapper;
import tech.intellispaces.jaquarius.system.injection.AutoGuideInjections;
import tech.intellispaces.jaquarius.system.injection.GuideInjections;
import tech.intellispaces.jaquarius.traverse.TraverseTypes;
import tech.intellispaces.java.reflection.customtype.CustomType;

import java.util.List;

public class UnmovableObjectHandleWrapperGenerator extends AbstractObjectHandleWrapperGenerator {

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
  protected ObjectHandleTypes getObjectHandleType() {
    return ObjectHandleTypes.Unmovable;
  }

  @Override
  protected boolean analyzeSourceArtifact(ArtifactGeneratorContext context) {
    addImport(List.class);

    addImport(Modules.class);
    addImport(TraverseException.class);
    addImport(ResettableSupplierAction.class);
    addImport(Actions.class);
    addImport(FunctionActions.class);
    addImport(Ordinal.class);
    addImport(Wrapper.class);
    addImport(ObjectHandleWrapper.class);
    addImport(ObjectReferenceForms.class);

    addImport(Mapper0.class);
    addImport(Mapper1.class);
    addImport(Channel1.class);
    addImport(ChannelMethod0.class);
    addImport(ChannelMethod1.class);
    addImport(ChannelFunctions.class);
    addImport(GuideInjections.class);
    addImport(AutoGuideInjections.class);
    addImport(MappingChannel.class);
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

    addVariable("typeParamsFull", typeParamsFull);
    addVariable("typeParamsBrief", typeParamsBrief);
    addVariable("domainClassSimpleName", domainSimpleClassName);
    addVariable("constructors", constructors);
    addVariable("wrapperMethods", wrapperMethods);
    addVariable("guideActionMethods", guideMethods);
    addVariable("domainMethods", methods);
    addVariable("injectionMethods", injectionMethods);
    addVariable("conversionMethods", conversionMethods);
    addVariable("notImplRelease", !implRelease);
    return true;
  }
}
