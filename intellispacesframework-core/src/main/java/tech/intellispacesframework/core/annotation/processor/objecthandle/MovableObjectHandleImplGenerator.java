package tech.intellispacesframework.core.annotation.processor.objecthandle;

import tech.intellispacesframework.core.exception.TraverseException;
import tech.intellispacesframework.core.guide.n1.Mover1;
import tech.intellispacesframework.core.object.ObjectFunctions;
import tech.intellispacesframework.core.space.transition.TransitionFunctions;
import tech.intellispacesframework.core.system.Modules;
import tech.intellispacesframework.core.transition.TransitionMethod1;
import tech.intellispacesframework.javastatements.statement.custom.CustomType;

import java.util.Map;

public class MovableObjectHandleImplGenerator extends AbstractObjectHandleImplGenerator {

  public MovableObjectHandleImplGenerator(CustomType objectHandleType) {
    super(objectHandleType);
  }

  @Override
  protected String templateName() {
    return "/MovableObjectHandleImpl.template";
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
        "domainClassSimpleName", domainSimpleClassName,
        "constructors", constructors,
        "importedClasses", context.getImports()
    );
  }

  @Override
  protected boolean analyzeAnnotatedType() {
    if (!isMovableObjectHandle(annotatedType)) {
      return false;
    }
    context.generatedClassCanonicalName(annotatedType.canonicalName() + "Impl");

    CustomType domainType = ObjectFunctions.getDomainTypeOfObjectHandle(annotatedType);

    context.addImport(Modules.class);
    context.addImport(TraverseException.class);

    context.addImport(Mover1.class);
    context.addImport(TransitionMethod1.class);
    context.addImport(TransitionFunctions.class);
    context.addImport(domainType.canonicalName());

    domainSimpleClassName = context.simpleNameOf(domainType.canonicalName());

    analyzeTypeParams(annotatedType);
    analyzeConstructors(annotatedType);

    return true;
  }
}
