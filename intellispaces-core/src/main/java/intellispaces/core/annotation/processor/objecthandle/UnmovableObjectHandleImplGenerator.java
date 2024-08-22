package intellispaces.core.annotation.processor.objecthandle;

import intellispaces.actions.Actions;
import intellispaces.actions.getter.ResettableGetter;
import intellispaces.core.annotation.Ordinal;
import intellispaces.core.exception.TraverseException;
import intellispaces.core.guide.n0.Mapper0;
import intellispaces.core.guide.n1.Mapper1;
import intellispaces.core.object.ObjectFunctions;
import intellispaces.core.object.ObjectHandleTypes;
import intellispaces.core.space.transition.TransitionFunctions;
import intellispaces.core.system.Modules;
import intellispaces.core.system.ObjectHandleWrapper;
import intellispaces.core.system.shadow.ShadowModules;
import intellispaces.core.system.shadow.ShadowObjectHandle;
import intellispaces.core.transition.TransitionMethod0;
import intellispaces.core.transition.TransitionMethod1;
import intellispaces.javastatements.customtype.CustomType;

import javax.annotation.processing.RoundEnvironment;
import java.util.HashMap;
import java.util.Map;

public class UnmovableObjectHandleImplGenerator extends AbstractObjectHandleImplGenerator {

  public UnmovableObjectHandleImplGenerator(CustomType objectHandleType) {
    super(objectHandleType);
  }

  @Override
  public String getArtifactName() {
    return getGeneratedClassCanonicalName();
  }

  @Override
  protected String templateName() {
    return "/unmovable_object_handle_impl.template";
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
    return ObjectHandleTypes.Unmovable;
  }

  @Override
  protected boolean analyzeAnnotatedType(RoundEnvironment roundEnv) {
    context.generatedClassCanonicalName(getGeneratedClassCanonicalName());

    context.addImport(Modules.class);
    context.addImport(ShadowModules.class);
    context.addImport(TraverseException.class);
    context.addImport(ResettableGetter.class);
    context.addImport(Actions.class);
    context.addImport(Ordinal.class);
    context.addImport(ObjectHandleWrapper.class);
    context.addImport(ShadowObjectHandle.class);

    context.addImport(Mapper0.class);
    context.addImport(Mapper1.class);
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
