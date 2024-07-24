package tech.intellispaces.framework.core.annotation.processor.domain;

import tech.intellispaces.framework.core.common.NameFunctions;
import tech.intellispaces.framework.core.object.MovableObjectHandle;
import tech.intellispaces.framework.core.object.ObjectHandleTypes;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;

import javax.annotation.processing.RoundEnvironment;
import java.util.HashMap;
import java.util.Map;

public class MovableObjectHandleGenerator extends AbstractDomainObjectHandleGenerator {
  private String commonHandleSimpleName;

  public MovableObjectHandleGenerator(CustomType domainType) {
    super(domainType);
  }

  @Override
  public String getArtifactName() {
    return NameFunctions.getMovableObjectHandleTypename(annotatedType.className());
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
    Map<String, Object> vars = new HashMap<>();
    vars.put("generatedAnnotation", generatedAnnotation());
    vars.put("packageName", context.packageName());
    vars.put("sourceClassName", sourceClassCanonicalName());
    vars.put("sourceClassSimpleName", sourceClassSimpleName());
    vars.put("commonHandleSimpleName", commonHandleSimpleName);
    vars.put("classSimpleName", context.generatedClassSimpleName());
    vars.put("domainTypeParamsFull", domainTypeParamsFull);
    vars.put("domainTypeParamsBrief", domainTypeParamsBrief);
    vars.put("methods", methods);
    vars.put("importedClasses", context.getImports());
    vars.put("movableObjectHandleName", context.simpleNameOf(MovableObjectHandle.class));
    return vars;
  }

  @Override
  protected boolean analyzeAnnotatedType(RoundEnvironment roundEnv) {
    context.generatedClassCanonicalName(getArtifactName());
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
