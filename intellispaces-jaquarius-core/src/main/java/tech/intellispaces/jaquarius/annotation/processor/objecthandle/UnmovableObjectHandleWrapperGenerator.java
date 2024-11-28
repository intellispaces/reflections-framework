package tech.intellispaces.jaquarius.annotation.processor.objecthandle;

import tech.intellispaces.jaquarius.annotation.Ordinal;
import tech.intellispaces.jaquarius.annotation.Wrapper;
import tech.intellispaces.jaquarius.channel.Channel1;
import tech.intellispaces.jaquarius.channel.ChannelMethod0;
import tech.intellispaces.jaquarius.channel.ChannelMethod1;
import tech.intellispaces.jaquarius.channel.MappingChannel;
import tech.intellispaces.jaquarius.engine.JaquariusEngines;
import tech.intellispaces.jaquarius.engine.descriptor.ObjectHandleInstance;
import tech.intellispaces.jaquarius.engine.descriptor.ObjectHandleType;
import tech.intellispaces.jaquarius.exception.TraverseException;
import tech.intellispaces.jaquarius.guide.GuideForms;
import tech.intellispaces.jaquarius.guide.n0.Mapper0;
import tech.intellispaces.jaquarius.guide.n1.Mapper1;
import tech.intellispaces.jaquarius.object.ObjectHandleTypes;
import tech.intellispaces.jaquarius.space.channel.ChannelFunctions;
import tech.intellispaces.jaquarius.system.Modules;
import tech.intellispaces.jaquarius.system.ObjectHandleWrapper;
import tech.intellispaces.jaquarius.system.injection.AutoGuideInjections;
import tech.intellispaces.jaquarius.system.injection.GuideInjections;
import tech.intellispaces.jaquarius.system.kernel.InnerObjectHandle;
import tech.intellispaces.jaquarius.system.kernel.KernelFunctions;
import tech.intellispaces.action.Actions;
import tech.intellispaces.action.functional.FunctionActions;
import tech.intellispaces.action.supplier.ResettableSupplierAction;
import tech.intellispaces.entity.type.PrimitiveFunctions;
import tech.intellispaces.java.annotation.context.AnnotationProcessingContext;
import tech.intellispaces.java.reflection.customtype.CustomType;

import javax.annotation.processing.RoundEnvironment;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnmovableObjectHandleWrapperGenerator extends AbstractObjectHandleWrapperGenerator {

  public UnmovableObjectHandleWrapperGenerator(CustomType initiatorType, CustomType objectHandleType) {
    super(initiatorType, objectHandleType);
  }

  @Override
  public boolean isRelevant(AnnotationProcessingContext context) {
    return true;
  }

  @Override
  public String artifactName() {
    return getGeneratedClassCanonicalName();
  }

  @Override
  protected String templateName() {
    return "/unmovable_object_handle_wrapper.template";
  }

  protected Map<String, Object> templateVariables() {
    Map<String, Object> vars = new HashMap<>();
    vars.put("generatedAnnotation", makeGeneratedAnnotation());
    vars.put("packageName", context.packageName());
    vars.put("sourceClassName", sourceClassCanonicalName());
    vars.put("sourceClassSimpleName", sourceClassSimpleName());
    vars.put("classSimpleName", context.generatedClassSimpleName());
    vars.put("typeParamsFull", typeParamsFull);
    vars.put("typeParamsBrief", typeParamsBrief);
    vars.put("domainClassSimpleName", domainSimpleClassName);
    vars.put("constructors", constructors);
    vars.put("importedClasses", context.getImports());
    vars.put("methodActions", methodActions);
    vars.put("guideActions", guideActions);
    vars.put("objectHandleMethods", objectHandleMethods);
    vars.put("guideActionMethods", guideMethods);
    vars.put("domainMethods", methods);
    vars.put("injections", injections);
    vars.put("injectionMethods", injectionMethods);
    vars.put("conversionMethods", conversionMethods);
    vars.put("notImplRelease", !implRelease);
    return vars;
  }

  @Override
  protected ObjectHandleTypes getObjectHandleType() {
    return ObjectHandleTypes.Unmovable;
  }

  @Override
  protected boolean analyzeAnnotatedType(RoundEnvironment roundEnv) {
    context.generatedClassCanonicalName(artifactName());

    context.addImport(List.class);

    context.addImport(Modules.class);
    context.addImport(KernelFunctions.class);
    context.addImport(TraverseException.class);
    context.addImport(ResettableSupplierAction.class);
    context.addImport(Actions.class);
    context.addImport(FunctionActions.class);
    context.addImport(Ordinal.class);
    context.addImport(Wrapper.class);
    context.addImport(ObjectHandleWrapper.class);
    context.addImport(InnerObjectHandle.class);
    context.addImport(GuideForms.class);

    context.addImport(Mapper0.class);
    context.addImport(Mapper1.class);
    context.addImport(Channel1.class);
    context.addImport(ChannelMethod0.class);
    context.addImport(ChannelMethod1.class);
    context.addImport(ChannelFunctions.class);
    context.addImport(GuideInjections.class);
    context.addImport(AutoGuideInjections.class);
    context.addImport(MappingChannel.class);
    context.addImport(PrimitiveFunctions.class);
    context.addImport(ObjectHandleType.class);
    context.addImport(ObjectHandleInstance.class);
    context.addImport(JaquariusEngines.class);

    analyzeDomain();
    analyzeTypeParams(annotatedType);
    analyzeConstructors(annotatedType);
    analyzeInjectedGuides(annotatedType);
    analyzeObjectHandleMethods(annotatedType, roundEnv);
    analyzeConversionMethods(domainType, roundEnv);
    analyzeReleaseMethod(annotatedType);
    return true;
  }
}
