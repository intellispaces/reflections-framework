package tech.intellispaces.core.annotation.processor.domain;

import tech.intellispaces.commons.string.StringFunctions;
import tech.intellispaces.core.annotation.MovableObjectHandle;
import tech.intellispaces.core.annotation.Transition;
import tech.intellispaces.core.common.NameConventionFunctions;
import tech.intellispaces.core.exception.TraverseException;
import tech.intellispaces.core.object.ObjectFunctions;
import tech.intellispaces.core.object.ObjectHandleTypes;
import tech.intellispaces.core.space.domain.DomainFunctions;
import tech.intellispaces.core.transition.TransitionMethod0;
import tech.intellispaces.core.transition.TransitionMethod1;
import tech.intellispaces.javastatements.customtype.CustomType;
import tech.intellispaces.javastatements.method.MethodStatement;
import tech.intellispaces.javastatements.reference.CustomTypeReference;
import tech.intellispaces.javastatements.type.Type;
import tech.intellispaces.javastatements.type.Types;

import javax.annotation.processing.RoundEnvironment;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MovableDownwardObjectHandleGenerator extends AbstractConversionDomainObjectHandleGenerator {
  private String classTypeParams;
  private String classTypeParamsBrief;
  private String movableObjectHandleName;
  private String childDomainClassSimpleName;
  private String childObjectHandleType;
  private String parentDomainClassSimpleName;
  private String domainClassSimpleName;
  protected String domainTypeParamsBrief;
  private boolean isAlias;
  private String mainPrimaryDomainSimpleName;
  private String primaryDomainTypeArguments;

  public MovableDownwardObjectHandleGenerator(CustomType annotatedType, CustomTypeReference parentDomainType) {
    super(annotatedType, parentDomainType);
  }

  @Override
  public String getArtifactName() {
    return NameConventionFunctions.getMovableDownwardObjectHandleTypename(annotatedType, parentDomainType.targetType());
  }

  @Override
  protected ObjectHandleTypes getObjectHandleType() {
    return ObjectHandleTypes.Movable;
  }

  @Override
  protected String templateName() {
    return "/movable_downward_object_handle.template";
  }

  @Override
  protected Map<String, Object> templateVariables() {
    Map<String, Object> vars = new HashMap<>();
    vars.put("generatedAnnotation", makeGeneratedAnnotation());
    vars.put("packageName", context.packageName());
    vars.put("sourceClassName", sourceClassCanonicalName());
    vars.put("sourceClassSimpleName", sourceClassSimpleName());
    vars.put("classSimpleName", context.generatedClassSimpleName());
    vars.put("classTypeParams", classTypeParams);
    vars.put("classTypeParamsBrief", classTypeParamsBrief);
    vars.put("domainTypeParamsBrief", domainTypeParamsBrief);
    vars.put("childDomainClassSimpleName", childDomainClassSimpleName);
    vars.put("parentDomainClassSimpleName", parentDomainClassSimpleName);
    vars.put("childObjectHandleType", childObjectHandleType);
    vars.put("childField", childFieldName);
    vars.put("methods", methods);
    vars.put("importedClasses", context.getImports());
    vars.put("movableObjectHandleName", movableObjectHandleName);
    vars.put("domainClassSimpleName", domainClassSimpleName);
    vars.put("isAlias", isAlias);
    vars.put("mainPrimaryDomainSimpleName", mainPrimaryDomainSimpleName);
    vars.put("primaryDomainTypeArguments", primaryDomainTypeArguments);
    return vars;
  }

  @Override
  protected boolean analyzeAnnotatedType(RoundEnvironment roundEnv) {
    context.generatedClassCanonicalName(getArtifactName());

    context.addImport(Type.class);
    context.addImport(Types.class);
    context.addImport(MovableObjectHandle.class);
    context.addImport(ObjectFunctions.class);
    context.addImport(TraverseException.class);
    context.addImport(TransitionMethod0.class);
    context.addImport(TransitionMethod1.class);

    movableObjectHandleName = context.addToImportAndGetSimpleName(
        NameConventionFunctions.getMovableObjectHandleTypename(parentDomainType.targetType().className()));
    domainClassSimpleName = context.addToImportAndGetSimpleName(parentDomainType.targetType().canonicalName());

    domainTypeParamsBrief = parentDomainType.targetType().typeParametersBriefDeclaration();

    classTypeParams = annotatedType.typeParametersFullDeclaration();
    classTypeParamsBrief = parentDomainType.typeArgumentsDeclaration(context::addToImportAndGetSimpleName);
    childFieldName = StringFunctions.lowercaseFirstLetter(annotatedType.simpleName());
    childObjectHandleType = getChildObjectHandleType();
    childDomainClassSimpleName = annotatedType.simpleName();
    parentDomainClassSimpleName = context.addToImportAndGetSimpleName(parentDomainType.targetType().canonicalName());

    analyzeObjectHandleMethods(parentDomainType.effectiveTargetType(), roundEnv);

    Optional<CustomTypeReference> primaryDomain = DomainFunctions.getPrimaryDomainForAliasDomain(annotatedType);
    isAlias = primaryDomain.isPresent();
    if (isAlias) {
      primaryDomainTypeArguments = primaryDomain.get().typeArgumentsDeclaration(context::addToImportAndGetSimpleName);
      Optional<CustomTypeReference> mainPrimaryDomain = DomainFunctions.getMainPrimaryDomainForAliasDomain(annotatedType);
      mainPrimaryDomainSimpleName = context.addToImportAndGetSimpleName(
          mainPrimaryDomain.orElseThrow().targetType().canonicalName()
      );
    }
    return true;
  }

  @Override
  protected Map<String, String> buildMethod(MethodStatement method) {
    if (method.hasAnnotation(Transition.class)) {
      return buildNormalMethod(method);
    } else {
      return buildAdditionalMethod(method);
    }
  }

  private Map<String, String> buildNormalMethod(MethodStatement method) {
    var sb = new StringBuilder();
    sb.append("public ");
    appendMethodTypeParameters(sb, method);
    appendMethodReturnHandleType(sb, method);
    sb.append(" ");
    sb.append(method.name());
    sb.append("(");
    appendMethodParameters(sb, method);
    sb.append(")");
    appendMethodExceptions(sb, method);
    sb.append(" {\n");
    sb.append("    ");
    buildReturnStatement(sb, method);
    sb.append("\n}\n");
    return Map.of(
        "javadoc", buildGeneratedMethodJavadoc(method.owner().canonicalName(), method),
        "declaration", sb.toString()
    );
  }

  private Map<String, String> buildAdditionalMethod(MethodStatement method) {
    var sb = new StringBuilder();
    sb.append("public ");
    appendMethodTypeParameters(sb, method);
    appendMethodReturnType(sb, method);
    sb.append(" ");
    sb.append(method.name());
    sb.append("(");
    appendMethodParameters(sb, method);
    sb.append(")");
    appendMethodExceptions(sb, method);
    sb.append(" {\n");
    sb.append("  return this.")
        .append(childFieldName)
        .append(".")
//        .append(NameConventionFunctions.getConversionMethodName(parentDomainType.targetType()))
//        .append("().")
        .append(method.name())
        .append("();\n");
    sb.append("}");
    return Map.of(
        "javadoc", buildGeneratedMethodJavadoc(method.owner().canonicalName(), method),
        "declaration", sb.toString()
    );
  }

  private String getChildObjectHandleType() {
    return context.addToImportAndGetSimpleName(
        NameConventionFunctions.getMovableObjectHandleTypename(annotatedType.className())
    );
  }
}
