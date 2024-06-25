package tech.intellispaces.framework.core.annotation.processor.domain;

import tech.intellispaces.framework.core.common.NameFunctions;
import tech.intellispaces.framework.core.object.ObjectHandleTypes;
import tech.intellispaces.framework.core.object.UnmovableObjectHandle;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;

import java.util.Map;

public class UnmovableObjectHandleGenerator extends AbstractObjectHandleGenerator {
  private String commonHandleSimpleName;

  public UnmovableObjectHandleGenerator(CustomType domainType) {
    super(domainType);
  }

  @Override
  protected ObjectHandleTypes getObjectHandleType() {
    return ObjectHandleTypes.Unmovable;
  }

  @Override
  protected String templateName() {
    return "/UnmovableObjectHandle.template";
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
    context.generatedClassCanonicalName(NameFunctions.getUnmovableObjectHandleClassCanonicalName(annotatedType.className()));
    if (annotatedType.isNested()) {
      context.addImport(sourceClassCanonicalName());
    }

    commonHandleSimpleName = NameFunctions.getCommonObjectHandleClassCanonicalName(annotatedType.className());
    context.addImport(commonHandleSimpleName);
    commonHandleSimpleName = context.simpleNameOf(commonHandleSimpleName);

    domainTypeParamsFull = annotatedType.typeParametersFullDeclaration();
    domainTypeParamsBrief = annotatedType.typeParametersBriefDeclaration();

    analyzeObjectHandleMethods(annotatedType);

    context.addImport(UnmovableObjectHandle.class);

    return true;
  }
}
