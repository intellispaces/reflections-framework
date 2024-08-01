package tech.intellispaces.core.annotation.processor.domain;

import tech.intellispaces.core.common.NameConventionFunctions;
import tech.intellispaces.core.object.ObjectFunctions;
import tech.intellispaces.core.object.ObjectHandle;
import tech.intellispaces.core.object.ObjectHandleTypes;
import tech.intellispaces.javastatements.customtype.CustomType;
import tech.intellispaces.javastatements.method.MethodStatement;
import tech.intellispaces.javastatements.type.Type;
import tech.intellispaces.javastatements.type.Types;

import javax.annotation.processing.RoundEnvironment;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class CommonObjectHandleGenerator extends AbstractDomainObjectHandleGenerator {
  private String objectHandleBunch;

  public CommonObjectHandleGenerator(CustomType domainType) {
    super(domainType);
  }

  @Override
  public String getArtifactName() {
    return NameConventionFunctions.getObjectHandleTypename(annotatedType.className(), ObjectHandleTypes.Common);
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
    vars.put("generatedAnnotation", makeGeneratedAnnotation());
    vars.put("packageName", context.packageName());
    vars.put("sourceClassName", sourceClassCanonicalName());
    vars.put("sourceClassSimpleName", sourceClassSimpleName());
    vars.put("classSimpleName", context.generatedClassSimpleName());
    vars.put("movableClassSimpleName", movableClassSimpleName());
    vars.put("domainTypeParamsFull", domainTypeParamsFull);
    vars.put("domainTypeParamsBrief", domainTypeParamsBrief);
    vars.put("objectHandleBunch", objectHandleBunch);
    vars.put("domainMethods", methods);
    vars.put("importedClasses", context.getImports());
    return vars;
  }

  @Override
  protected boolean analyzeAnnotatedType(RoundEnvironment roundEnv) {
    context.generatedClassCanonicalName(getArtifactName());
    context.addImport(sourceClassCanonicalName());
    context.addImport(ObjectHandle.class);
    context.addImport(Type.class);
    context.addImport(Types.class);

    domainTypeParamsFull = annotatedType.typeParametersFullDeclaration();
    domainTypeParamsBrief = annotatedType.typeParametersBriefDeclaration();
    objectHandleBunch = context.addToImportAndGetSimpleName(
        NameConventionFunctions.getBunchObjectHandleTypename(annotatedType.className())
    );
    analyzeObjectHandleMethods(annotatedType, roundEnv);
    return true;
  }

  @Override
  protected Stream<MethodStatement> getObjectHandleMethods(
      CustomType customType, RoundEnvironment roundEnv
  ) {
    return super.getObjectHandleMethods(customType, roundEnv)
        .filter(this::isNotGetDomainMethod)
        .filter(m -> m.returnType().isPresent() &&
            !m.returnType().get().isNamedReference() &&
            !ObjectFunctions.isDefaultObjectHandleType(m.returnType().get())
        );
  }
}
