package tech.intellispaces.framework.core.annotation.processor.domain;

import tech.intellispaces.framework.core.common.NameFunctions;
import tech.intellispaces.framework.core.object.ObjectHandle;
import tech.intellispaces.framework.core.object.ObjectHandleTypes;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;

import javax.annotation.processing.RoundEnvironment;
import java.util.HashMap;
import java.util.Map;

public class CommonObjectHandleGenerator extends AbstractDomainObjectHandleGenerator {

  public CommonObjectHandleGenerator(CustomType domainType) {
    super(domainType);
  }

  @Override
  public String getArtifactName() {
    return NameFunctions.getCommonObjectHandleTypename(annotatedType.className());
  }

  @Override
  protected ObjectHandleTypes getObjectHandleType() {
    return ObjectHandleTypes.Common;
  }

  @Override
  protected String templateName() {
    return "/common_object_handle.template";
  }

  protected Map<String, Object> templateVariables() {
    Map<String, Object> vars = new HashMap<>();
    vars.put("generatedAnnotation", generatedAnnotation());
    vars.put("packageName", context.packageName());
    vars.put("sourceClassName", sourceClassCanonicalName());
    vars.put("sourceClassSimpleName", sourceClassSimpleName());
    vars.put("classSimpleName", context.generatedClassSimpleName());
    vars.put("movableClassSimpleName", movableClassSimpleName());
    vars.put("domainTypeParamsFull", domainTypeParamsFull);
    vars.put("domainTypeParamsBrief", domainTypeParamsBrief);
    vars.put("methods", methods);
    vars.put("importedClasses", context.getImports());
    vars.put("objectHandleName", context.simpleNameOf(ObjectHandle.class));
    return vars;
  }

  @Override
  protected boolean analyzeAnnotatedType(RoundEnvironment roundEnv) {
    context.generatedClassCanonicalName(getArtifactName());
    if (annotatedType.isNested()) {
      context.addImport(sourceClassCanonicalName());
    }

    domainTypeParamsFull = annotatedType.typeParametersFullDeclaration();
    domainTypeParamsBrief = annotatedType.typeParametersBriefDeclaration();

    analyzeObjectHandleMethods(annotatedType);

    context.addImport(ObjectHandle.class);

    return true;
  }
}
