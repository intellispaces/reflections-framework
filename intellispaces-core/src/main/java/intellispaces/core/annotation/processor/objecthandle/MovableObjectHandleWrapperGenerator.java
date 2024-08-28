package intellispaces.core.annotation.processor.objecthandle;

import intellispaces.actions.Actions;
import intellispaces.core.annotation.Ordinal;
import intellispaces.core.annotation.Wrapper;
import intellispaces.core.exception.TraverseException;
import intellispaces.core.guide.n0.Mover0;
import intellispaces.core.guide.n1.Mover1;
import intellispaces.core.object.ObjectFunctions;
import intellispaces.core.object.ObjectHandleTypes;
import intellispaces.core.space.transition.Transition0;
import intellispaces.core.space.transition.Transition1;
import intellispaces.core.space.transition.TransitionFunctions;
import intellispaces.core.system.Modules;
import intellispaces.core.system.ObjectHandleWrapper;
import intellispaces.core.system.shadow.ShadowModules;
import intellispaces.core.system.shadow.ShadowObjectHandle;
import intellispaces.core.transition.TransitionMethod0;
import intellispaces.core.transition.TransitionMethod1;
import intellispaces.javastatements.customtype.CustomType;
import intellispaces.javastatements.type.Type;

import javax.annotation.processing.RoundEnvironment;
import java.util.HashMap;
import java.util.Map;

public class MovableObjectHandleWrapperGenerator extends AbstractObjectHandleWrapperGenerator {

  public MovableObjectHandleWrapperGenerator(CustomType objectHandleType) {
    super(objectHandleType);
  }

  @Override
  public String getArtifactName() {
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
    return vars;
  }

  @Override
  protected ObjectHandleTypes getObjectHandleType() {
    return ObjectHandleTypes.Movable;
  }

  @Override
  protected boolean analyzeAnnotatedType(RoundEnvironment roundEnv) {
    context.generatedClassCanonicalName(getGeneratedClassCanonicalName());

    context.addImport(Modules.class);
    context.addImport(ShadowModules.class);
    context.addImport(TraverseException.class);
    context.addImport(Actions.class);
    context.addImport(Type.class);
    context.addImport(Ordinal.class);
    context.addImport(Wrapper.class);
    context.addImport(ObjectHandleWrapper.class);
    context.addImport(ShadowObjectHandle.class);

    context.addImport(Mover0.class);
    context.addImport(Mover1.class);
    context.addImport(Transition0.class);
    context.addImport(Transition1.class);
    context.addImport(TransitionMethod0.class);
    context.addImport(TransitionMethod1.class);
    context.addImport(TransitionFunctions.class);

    CustomType domainType = ObjectFunctions.getDomainTypeOfObjectHandle(annotatedType);
    context.addImport(domainType.canonicalName());

    domainSimpleClassName = context.simpleNameOf(domainType.canonicalName());

    analyzeTypeParams(annotatedType);
    analyzeConstructors(annotatedType);
    analyzeGuideActions(annotatedType);
    analyzeTransitionActions();
    analyzeObjectHandleMethods(annotatedType, roundEnv);
    return true;
  }
}
