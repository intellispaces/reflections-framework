package tech.intellispaces.framework.core.annotation.processor.objecthandle;

import tech.intellispaces.framework.core.exception.TraverseException;
import tech.intellispaces.framework.core.guide.n1.Mover1;
import tech.intellispaces.framework.core.space.transition.TransitionFunctions;
import tech.intellispaces.framework.core.system.Modules;
import tech.intellispaces.framework.core.transition.TransitionMethod1;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;

import java.util.Map;

public class UnmovableObjectHandleImplGenerator extends AbstractObjectHandleImplGenerator {

  public UnmovableObjectHandleImplGenerator(CustomType objectHandleType) {
    super(objectHandleType);
  }

  @Override
  protected String templateName() {
    return "/unmovable_object_handle_impl.template";
  }

  protected Map<String, Object> templateVariables() {
    return Map.of(
        "generatedAnnotation", generatedAnnotation(),
        "packageName", context.packageName(),
        "sourceClassName", sourceClassCanonicalName(),
        "sourceClassSimpleName", sourceClassSimpleName(),
        "classSimpleName", context.generatedClassSimpleName(),
        "typeParamsFull", typeParamsFull,
        "typeParamsBrief", typeParamsBrief,
        "constructors", constructors,
        "importedClasses", context.getImports()
    );
  }

  @Override
  protected boolean analyzeAnnotatedType() {
    context.generatedClassCanonicalName(getGeneratedClassCanonicalName());

    analyzeTypeParams(annotatedType);
    analyzeConstructors(annotatedType);

    context.addImport(Modules.class);
    context.addImport(TraverseException.class);

    context.addImport(Mover1.class);
    context.addImport(TransitionMethod1.class);
    context.addImport(TransitionFunctions.class);

    return true;
  }
}
