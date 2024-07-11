package tech.intellispaces.framework.core.annotation.processor.domain;

import tech.intellispaces.framework.commons.exception.UnexpectedViolationException;
import tech.intellispaces.framework.core.common.NameFunctions;
import tech.intellispaces.framework.core.object.ObjectHandleTypes;
import tech.intellispaces.framework.core.object.UnmovableObjectHandle;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;

import java.util.HashMap;
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
    return "/unmovable_object_handle.template";
  }

  protected Map<String, Object> templateVariables() {
    Map<String, Object> vars = new HashMap<>();
    vars.put("generatedAnnotation", generatedAnnotation());
    vars.put("packageName", context.packageName());
    vars.put("sourceClassName", sourceClassCanonicalName());
    vars.put("sourceClassSimpleName", sourceClassSimpleName());
    vars.put("movableClassSimpleName", movableClassSimpleName());
    vars.put("commonHandleSimpleName", commonHandleSimpleName);
    vars.put("classSimpleName", context.generatedClassSimpleName());
    vars.put("domainTypeParamsFull", domainTypeParamsFull);
    vars.put("domainTypeParamsBrief", domainTypeParamsBrief);
    vars.put("methods", methods);
    vars.put("importedClasses", context.getImports());
    vars.put("unmovableObjectHandleName", context.simpleNameOf(UnmovableObjectHandle.class));
    return vars;
  }

  @Override
  protected boolean analyzeAnnotatedType() {
    context.generatedClassCanonicalName(
        NameFunctions.getUnmovableObjectHandleTypename(annotatedType.className()));
    if (annotatedType.isNested()) {
      context.addImport(sourceClassCanonicalName());
    }

    context.addImport(UnexpectedViolationException.class);

    commonHandleSimpleName = NameFunctions.getCommonObjectHandleTypename(annotatedType.className());
    context.addImport(commonHandleSimpleName);
    commonHandleSimpleName = context.simpleNameOf(commonHandleSimpleName);

    domainTypeParamsFull = annotatedType.typeParametersFullDeclaration();
    domainTypeParamsBrief = annotatedType.typeParametersBriefDeclaration();

    analyzeObjectHandleMethods(annotatedType);

    context.addImport(UnmovableObjectHandle.class);

    return true;
  }
}
