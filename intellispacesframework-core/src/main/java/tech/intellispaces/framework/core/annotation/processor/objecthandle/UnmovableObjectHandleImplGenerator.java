package tech.intellispaces.framework.core.annotation.processor.objecthandle;

import tech.intellispaces.framework.core.exception.TraverseException;
import tech.intellispaces.framework.core.guide.n0.Mapper0;
import tech.intellispaces.framework.core.guide.n1.Mapper1;
import tech.intellispaces.framework.core.object.ObjectHandleTypes;
import tech.intellispaces.framework.core.space.transition.TransitionFunctions;
import tech.intellispaces.framework.core.system.Modules;
import tech.intellispaces.framework.core.transition.TransitionMethod0;
import tech.intellispaces.framework.core.transition.TransitionMethod1;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;

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
    vars.put("generatedAnnotation", generatedAnnotation());
    vars.put("packageName", context.packageName());
    vars.put("sourceClassName", sourceClassCanonicalName());
    vars.put("sourceClassSimpleName", sourceClassSimpleName());
    vars.put("classSimpleName", context.generatedClassSimpleName());
    vars.put("typeParamsFull", typeParamsFull);
    vars.put("typeParamsBrief", typeParamsBrief);
    vars.put("constructors", constructors);
    vars.put("importedClasses", context.getImports());
    vars.put("guideGetters", guideGetters);
    vars.put("guideImplementationMethods", guideImplementationMethods);
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

    context.addImport(Mapper0.class);
    context.addImport(Mapper1.class);
    context.addImport(TransitionMethod0.class);
    context.addImport(TransitionMethod1.class);
    context.addImport(TransitionFunctions.class);

    analyzeTypeParams(annotatedType);
    analyzeConstructors(annotatedType);
    analyzeGuideGetters(annotatedType);
    analyzeGuideImplementationMethods(annotatedType);
    analyzeObjectHandleMethods(annotatedType);
    return true;
  }
}
