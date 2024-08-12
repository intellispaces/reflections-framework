package tech.intellispaces.core.annotation.processor.domain;

import tech.intellispaces.core.common.NameConventionFunctions;
import tech.intellispaces.core.object.MovableObjectHandle;
import tech.intellispaces.core.object.ObjectHandleTypes;
import tech.intellispaces.core.space.domain.DomainFunctions;
import tech.intellispaces.javastatements.customtype.CustomType;
import tech.intellispaces.javastatements.reference.CustomTypeReference;

import javax.annotation.processing.RoundEnvironment;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MovableObjectHandleGenerator extends AbstractDomainObjectHandleGenerator {
  private String baseObjectHandle;
  private boolean isAlias;
  private String primaryObjectHandle;

  public MovableObjectHandleGenerator(CustomType domainType) {
    super(domainType);
  }

  @Override
  public String getArtifactName() {
    return NameConventionFunctions.getMovableObjectHandleTypename(annotatedType.className());
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
    vars.put("generatedAnnotation", makeGeneratedAnnotation());
    vars.put("packageName", context.packageName());
    vars.put("importedClasses", context.getImports());
    vars.put("sourceClassName", sourceClassCanonicalName());
    vars.put("sourceClassSimpleName", sourceClassSimpleName());
    vars.put("classSimpleName", context.generatedClassSimpleName());
    vars.put("domainTypeParamsFull", domainTypeParamsFull);
    vars.put("domainTypeParamsBrief", domainTypeParamsBrief);
    vars.put("baseObjectHandle", baseObjectHandle);
    vars.put("domainMethods", methods);
    vars.put("movableObjectHandleName", context.addToImportAndGetSimpleName(MovableObjectHandle.class));
    vars.put("isAlias", isAlias);
    vars.put("primaryObjectHandle", primaryObjectHandle);
    return vars;
  }

  @Override
  protected boolean analyzeAnnotatedType(RoundEnvironment roundEnv) {
    context.generatedClassCanonicalName(getArtifactName());
    if (annotatedType.isNested()) {
      context.addImport(sourceClassCanonicalName());
    }
    context.addImport(MovableObjectHandle.class);

    domainTypeParamsFull = annotatedType.typeParametersFullDeclaration();
    domainTypeParamsBrief = annotatedType.typeParametersBriefDeclaration();
    baseObjectHandle = context.addToImportAndGetSimpleName(
        NameConventionFunctions.getBaseObjectHandleTypename(annotatedType.className())
    );
    analyzeObjectHandleMethods(annotatedType, roundEnv);

    Optional<CustomTypeReference> primaryDomain = DomainFunctions.getPrimaryDomainForAliasDomain(annotatedType);
    isAlias = primaryDomain.isPresent();
    if (isAlias) {
      primaryObjectHandle = getObjectHandleDeclaration(primaryDomain.get(), ObjectHandleTypes.Movable);
    }
    return true;
  }
}
