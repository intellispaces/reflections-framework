package intellispaces.core.annotation.processor.domain;

import intellispaces.core.annotation.ObjectHandle;
import intellispaces.core.common.NameConventionFunctions;
import intellispaces.core.object.ObjectFunctions;
import intellispaces.core.object.ObjectHandleTypes;
import intellispaces.core.space.domain.DomainFunctions;
import intellispaces.javastatements.customtype.CustomType;
import intellispaces.javastatements.method.MethodStatement;
import intellispaces.javastatements.reference.CustomTypeReference;
import intellispaces.javastatements.type.Type;
import intellispaces.javastatements.type.Types;

import javax.annotation.processing.RoundEnvironment;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class CommonObjectHandleGenerator extends AbstractDomainObjectHandleGenerator {
  private String objectHandleBunch;
  private boolean isAlias;
  private String primaryObjectHandle;
  private String mainPrimaryDomainSimpleName;
  private String primaryDomainTypeArguments;

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
    vars.put("importedClasses", context.getImports());
    vars.put("packageName", context.packageName());
    vars.put("sourceClassName", sourceClassCanonicalName());
    vars.put("sourceClassSimpleName", sourceClassSimpleName());
    vars.put("classSimpleName", context.generatedClassSimpleName());
    vars.put("movableClassSimpleName", movableClassSimpleName());
    vars.put("domainTypeParamsFull", domainTypeParamsFull);
    vars.put("domainTypeParamsBrief", domainTypeParamsBrief);
    vars.put("objectHandleBunch", objectHandleBunch);
    vars.put("domainMethods", methods);
    vars.put("isAlias", isAlias);
    vars.put("primaryObjectHandle", primaryObjectHandle);
    vars.put("primaryDomainTypeArguments", primaryDomainTypeArguments);
    vars.put("mainPrimaryDomainSimpleName", mainPrimaryDomainSimpleName);
    return vars;
  }

  @Override
  protected boolean analyzeAnnotatedType(RoundEnvironment roundEnv) {
    context.generatedClassCanonicalName(getArtifactName());
    context.addImport(sourceClassCanonicalName());
    context.addImport(Type.class);
    context.addImport(Types.class);
    context.addImport(ObjectHandle.class);

    domainTypeParamsFull = annotatedType.typeParametersFullDeclaration();
    domainTypeParamsBrief = annotatedType.typeParametersBriefDeclaration();
    objectHandleBunch = context.addToImportAndGetSimpleName(
        NameConventionFunctions.getBunchObjectHandleTypename(annotatedType.className())
    );
    analyzeObjectHandleMethods(annotatedType, roundEnv);

    Optional<CustomTypeReference> primaryDomain = DomainFunctions.getPrimaryDomainForAliasDomain(annotatedType);
    isAlias = primaryDomain.isPresent();
    if (isAlias) {
      primaryObjectHandle = getObjectHandleDeclaration(primaryDomain.get(), ObjectHandleTypes.Common);
      primaryDomainTypeArguments = primaryDomain.get().typeArgumentsDeclaration(context::addToImportAndGetSimpleName);
      Optional<CustomTypeReference> mainPrimaryDomain = DomainFunctions.getMainPrimaryDomainForAliasDomain(annotatedType);
      mainPrimaryDomainSimpleName = context.addToImportAndGetSimpleName(
          mainPrimaryDomain.orElseThrow().targetType().canonicalName()
      );
    }
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
