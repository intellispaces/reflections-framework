package tech.intellispaces.core.annotation.processor.objecthandle;

import tech.intellispaces.actions.Action;
import tech.intellispaces.actions.Actions;
import tech.intellispaces.actions.getter.ResettableGetter;
import tech.intellispaces.core.annotation.Order;
import tech.intellispaces.core.exception.TraverseException;
import tech.intellispaces.core.guide.n0.Mapper0;
import tech.intellispaces.core.guide.n1.Mapper1;
import tech.intellispaces.core.object.ObjectHandleWrapper;
import tech.intellispaces.core.object.ObjectFunctions;
import tech.intellispaces.core.object.ObjectHandleTypes;
import tech.intellispaces.core.space.transition.TransitionFunctions;
import tech.intellispaces.core.system.Modules;
import tech.intellispaces.core.transition.TransitionMethod0;
import tech.intellispaces.core.transition.TransitionMethod1;
import tech.intellispaces.javastatements.customtype.CustomType;

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
    vars.put("guideGetters", guideGetters);
    vars.put("actionGetters", actionGetters);
    vars.put("actionGetterSuppliers", actionGetterSuppliers);
    vars.put("methods", methods);
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
    context.addImport(TraverseException.class);
    context.addImport(ResettableGetter.class);
    context.addImport(Action.class);
    context.addImport(Actions.class);
    context.addImport(Order.class);
    context.addImport(ObjectHandleWrapper.class);

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
    analyzeGuideGetters(annotatedType, roundEnv);
    analyzeActionGetters(annotatedType, roundEnv);
    analyzeObjectHandleMethods(annotatedType, roundEnv);
    return true;
  }
}
