package intellispaces.framework.core.annotation.processor.objecthandle;

import intellispaces.common.action.Actions;
import intellispaces.common.action.functional.FunctionActions;
import intellispaces.common.annotationprocessor.context.AnnotationProcessingContext;
import intellispaces.common.base.collection.ArraysFunctions;
import intellispaces.common.base.math.MathFunctions;
import intellispaces.common.base.type.Type;
import intellispaces.common.javastatement.customtype.CustomType;
import intellispaces.common.javastatement.method.MethodStatement;
import intellispaces.common.javastatement.reference.TypeReference;
import intellispaces.framework.core.annotation.Channel;
import intellispaces.framework.core.annotation.Ordinal;
import intellispaces.framework.core.annotation.TargetSpecification;
import intellispaces.framework.core.annotation.Wrapper;
import intellispaces.framework.core.exception.TraverseException;
import intellispaces.framework.core.guide.GuideForms;
import intellispaces.framework.core.guide.n0.Mover0;
import intellispaces.framework.core.guide.n1.Mover1;
import intellispaces.framework.core.object.ObjectHandleTypes;
import intellispaces.framework.core.space.channel.Channel0;
import intellispaces.framework.core.space.channel.Channel1;
import intellispaces.framework.core.space.channel.ChannelFunctions;
import intellispaces.framework.core.space.channel.ChannelMethod0;
import intellispaces.framework.core.space.channel.ChannelMethod1;
import intellispaces.framework.core.space.channel.MappingChannel;
import intellispaces.framework.core.system.Modules;
import intellispaces.framework.core.system.ObjectHandleWrapper;
import intellispaces.framework.core.system.injection.AutoGuideInjections;
import intellispaces.framework.core.system.injection.GuideInjections;
import intellispaces.framework.core.system.kernel.KernelFunctions;
import intellispaces.framework.core.system.kernel.KernelObjectHandle;
import intellispaces.framework.core.traverse.TraverseType;

import javax.annotation.processing.RoundEnvironment;
import java.util.HashMap;
import java.util.Map;

public class MovableObjectHandleWrapperGenerator extends AbstractObjectHandleWrapperGenerator {

  public MovableObjectHandleWrapperGenerator(CustomType initiatorType, CustomType objectHandleType) {
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
    return "/movable_object_handle_wrapper.template";
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
    vars.put("isAlias", isAlias);
    vars.put("primaryDomainSimpleName", primaryDomainSimpleName);
    vars.put("primaryDomainTypeArguments", primaryDomainTypeArguments);
    vars.put("constructors", constructors);
    vars.put("importedClasses", context.getImports());
    vars.put("methodActions", methodActions);
    vars.put("guideActions", guideActions);
    vars.put("guideActionMethods", guideMethods);
    vars.put("domainMethods", methods);
    vars.put("injections", injections);
    vars.put("injectionMethods", injectionMethods);
    return vars;
  }

  @Override
  protected ObjectHandleTypes getObjectHandleType() {
    return ObjectHandleTypes.Movable;
  }

  @Override
  protected boolean analyzeAnnotatedType(RoundEnvironment roundEnv) {
    context.generatedClassCanonicalName(artifactName());

    context.addImport(Modules.class);
    context.addImport(KernelFunctions.class);
    context.addImport(TraverseException.class);
    context.addImport(Actions.class);
    context.addImport(FunctionActions.class);
    context.addImport(Type.class);
    context.addImport(Ordinal.class);
    context.addImport(Wrapper.class);
    context.addImport(ObjectHandleWrapper.class);
    context.addImport(KernelObjectHandle.class);
    context.addImport(MathFunctions.class);
    context.addImport(GuideForms.class);

    context.addImport(Mover0.class);
    context.addImport(Mover1.class);
    context.addImport(Channel0.class);
    context.addImport(Channel1.class);
    context.addImport(ChannelMethod0.class);
    context.addImport(ChannelMethod1.class);
    context.addImport(ChannelFunctions.class);
    context.addImport(GuideInjections.class);
    context.addImport(AutoGuideInjections.class);
    context.addImport(MappingChannel.class);

    analyzeDomain();
    analyzeTypeParams(annotatedType);
    analyzeConstructors(annotatedType);
    analyzeInjectedGuides(annotatedType);
    analyzeObjectHandleMethods(annotatedType, roundEnv);
    return true;
  }

  protected void appendMethodReturnHandleType(StringBuilder sb, MethodStatement method) {
    TypeReference domainReturnType = method.returnType().orElseThrow();
    if (
        ChannelFunctions.getTraverseTypes(method).stream().anyMatch(TraverseType::isMoving)
          || ArraysFunctions.contains(method.selectAnnotation(Channel.class).orElseThrow().targetSpecifications(), TargetSpecification.Movable)
    ) {
      sb.append(getObjectHandleDeclaration(domainReturnType, ObjectHandleTypes.Movable));
    } else {
      sb.append(getObjectHandleDeclaration(domainReturnType, ObjectHandleTypes.Common));
    }
  }
}
