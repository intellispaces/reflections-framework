package intellispaces.framework.core.annotation.processor.objecthandle;

import intellispaces.common.action.Actions;
import intellispaces.common.annotationprocessor.context.AnnotationProcessingContext;
import intellispaces.common.base.type.Type;
import intellispaces.common.javastatement.customtype.CustomType;
import intellispaces.framework.core.annotation.Ordinal;
import intellispaces.framework.core.annotation.Wrapper;
import intellispaces.framework.core.exception.TraverseException;
import intellispaces.framework.core.guide.n0.Mover0;
import intellispaces.framework.core.guide.n1.Mover1;
import intellispaces.framework.core.object.ObjectFunctions;
import intellispaces.framework.core.object.ObjectHandleTypes;
import intellispaces.framework.core.space.transition.Transition0;
import intellispaces.framework.core.space.transition.Transition1;
import intellispaces.framework.core.space.transition.TransitionFunctions;
import intellispaces.framework.core.system.Modules;
import intellispaces.framework.core.system.ObjectHandleWrapper;
import intellispaces.framework.core.system.injection.AutoGuideInjections;
import intellispaces.framework.core.system.injection.GuideInjections;
import intellispaces.framework.core.system.kernel.KernelFunctions;
import intellispaces.framework.core.system.kernel.KernelObjectHandle;
import intellispaces.framework.core.transition.TransitionMethod0;
import intellispaces.framework.core.transition.TransitionMethod1;

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
    vars.put("constructors", constructors);
    vars.put("importedClasses", context.getImports());
    vars.put("transitionActions", transitionActions);
    vars.put("guideActions", guideActions);
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
    context.addImport(Type.class);
    context.addImport(Ordinal.class);
    context.addImport(Wrapper.class);
    context.addImport(ObjectHandleWrapper.class);
    context.addImport(KernelObjectHandle.class);

    context.addImport(Mover0.class);
    context.addImport(Mover1.class);
    context.addImport(Transition0.class);
    context.addImport(Transition1.class);
    context.addImport(TransitionMethod0.class);
    context.addImport(TransitionMethod1.class);
    context.addImport(TransitionFunctions.class);
    context.addImport(GuideInjections.class);
    context.addImport(AutoGuideInjections.class);

    CustomType domainType = ObjectFunctions.getDomainTypeOfObjectHandle(annotatedType);
    context.addImport(domainType.canonicalName());

    domainSimpleClassName = context.simpleNameOf(domainType.canonicalName());

    analyzeTypeParams(annotatedType);
    analyzeConstructors(annotatedType);
    analyzeGuideActions(annotatedType);
    analyzeInjectedGuides(annotatedType);
    analyzeTransitionActions();
    analyzeObjectHandleMethods(annotatedType, roundEnv);
    return true;
  }
}
