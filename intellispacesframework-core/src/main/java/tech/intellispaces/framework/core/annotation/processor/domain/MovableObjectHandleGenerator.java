package tech.intellispaces.framework.core.annotation.processor.domain;

import tech.intellispaces.framework.core.common.NameFunctions;
import tech.intellispaces.framework.core.object.MovableObjectHandle;
import tech.intellispaces.framework.core.object.ObjectHandleTypes;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;

import java.util.Map;

public class MovableObjectHandleGenerator extends AbstractObjectHandleGenerator {
  private String commonHandleSimpleName;

  public MovableObjectHandleGenerator(CustomType domainType) {
    super(domainType);
  }

  @Override
  protected ObjectHandleTypes getObjectHandleType() {
    return ObjectHandleTypes.Movable;
  }

  @Override
  protected String templateName() {
    return "/movable_object_handle.template";
  }

  protected Map<String, Object> templateVariables() {
    return Map.of(
        "generatedAnnotation", generatedAnnotation(),
        "packageName", context.packageName(),
        "sourceClassName", sourceClassCanonicalName(),
        "sourceClassSimpleName", sourceClassSimpleName(),
        "commonHandleSimpleName", commonHandleSimpleName,
        "classSimpleName", context.generatedClassSimpleName(),
        "domainTypeParamsFull", domainTypeParamsFull,
        "domainTypeParamsBrief", domainTypeParamsBrief,
        "methods", methods,
        "importedClasses", context.getImports()
    );
  }

  @Override
  protected boolean analyzeAnnotatedType() {
    context.generatedClassCanonicalName(NameFunctions.getMovableObjectHandleTypename(annotatedType.className()));
    if (annotatedType.isNested()) {
      context.addImport(sourceClassCanonicalName());
    }

    commonHandleSimpleName = NameFunctions.getCommonObjectHandleTypename(annotatedType.className());
    context.addImport(commonHandleSimpleName);
    commonHandleSimpleName = context.simpleNameOf(commonHandleSimpleName);

    domainTypeParamsFull = annotatedType.typeParametersFullDeclaration();
    domainTypeParamsBrief = annotatedType.typeParametersBriefDeclaration();

    analyzeObjectHandleMethods(annotatedType);

    context.addImport(MovableObjectHandle.class);

    return true;
  }
}
