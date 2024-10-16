package intellispaces.framework.core.annotation.processor.domain;

import intellispaces.common.annotationprocessor.context.AnnotationProcessingContext;
import intellispaces.common.base.exception.UnexpectedViolationException;
import intellispaces.common.javastatement.customtype.CustomType;
import intellispaces.common.javastatement.method.MethodStatement;
import intellispaces.common.javastatement.reference.CustomTypeReference;
import intellispaces.framework.core.annotation.ObjectHandle;
import intellispaces.framework.core.common.NameConventionFunctions;
import intellispaces.framework.core.object.ObjectHandleTypes;
import intellispaces.framework.core.object.UnmovableObjectHandle;
import intellispaces.framework.core.space.channel.ChannelFunctions;
import intellispaces.framework.core.space.domain.DomainFunctions;
import intellispaces.framework.core.traverse.TraverseType;

import javax.annotation.processing.RoundEnvironment;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class UnmovableObjectHandleGenerator extends AbstractDomainObjectHandleGenerator {
  private String baseObjectHandle;
  private boolean isAlias;
  private String primaryObjectHandle;

  public UnmovableObjectHandleGenerator(CustomType initiatorType, CustomType domainType) {
    super(initiatorType, domainType);
  }

  @Override
  public boolean isRelevant(AnnotationProcessingContext context) {
    return true;
  }

  @Override
  protected ObjectHandleTypes getObjectHandleType() {
    return ObjectHandleTypes.Unmovable;
  }

  @Override
  public String artifactName() {
    return NameConventionFunctions.getUnmovableObjectHandleTypename(annotatedType.className());
  }

  @Override
  protected String templateName() {
    return "/unmovable_object_handle.template";
  }

  protected Map<String, Object> templateVariables() {
    Map<String, Object> vars = new HashMap<>();
    vars.put("generatedAnnotation", makeGeneratedAnnotation());
    vars.put("packageName", context.packageName());
    vars.put("importedClasses", context.getImports());
    vars.put("sourceClassName", sourceClassCanonicalName());
    vars.put("sourceClassSimpleName", sourceClassSimpleName());
    vars.put("movableClassSimpleName", movableClassSimpleName());
    vars.put("classSimpleName", context.generatedClassSimpleName());
    vars.put("domainTypeParamsFull", domainTypeParamsFull);
    vars.put("domainTypeParamsBrief", domainTypeParamsBrief);
    vars.put("baseObjectHandle", baseObjectHandle);
    vars.put("domainMethods", methods);
    vars.put("unmovableObjectHandleName", context.addToImportAndGetSimpleName(UnmovableObjectHandle.class));
    vars.put("isAlias", isAlias);
    vars.put("primaryObjectHandle", primaryObjectHandle);
    return vars;
  }

  @Override
  protected boolean analyzeAnnotatedType(RoundEnvironment roundEnv) {
    context.generatedClassCanonicalName(artifactName());
    if (annotatedType.isNested()) {
      context.addImport(sourceClassCanonicalName());
    }
    context.addImport(UnexpectedViolationException.class);
    context.addImport(UnmovableObjectHandle.class);
    context.addImport(ObjectHandle.class);

    domainTypeParamsFull = annotatedType.typeParametersFullDeclaration();
    domainTypeParamsBrief = annotatedType.typeParametersBriefDeclaration();
    baseObjectHandle = context.addToImportAndGetSimpleName(
        NameConventionFunctions.getCommonObjectHandleTypename(annotatedType.className())
    );
    analyzeObjectHandleMethods(annotatedType, roundEnv);

    Optional<CustomTypeReference> equivalentDomain = DomainFunctions.getNearEquivalentDomain(annotatedType);
    isAlias = equivalentDomain.isPresent();
    if (isAlias) {
      primaryObjectHandle = getObjectHandleDeclaration(equivalentDomain.get(), ObjectHandleTypes.Unmovable);
    }
    return true;
  }

  @Override
  protected Stream<MethodStatement> getObjectHandleMethods(CustomType customType, RoundEnvironment roundEnv) {
    return super.getObjectHandleMethods(customType, roundEnv)
        .filter(m -> ChannelFunctions.getTraverseTypes(m).stream().noneMatch(TraverseType::isMovingBased));
  }
}
