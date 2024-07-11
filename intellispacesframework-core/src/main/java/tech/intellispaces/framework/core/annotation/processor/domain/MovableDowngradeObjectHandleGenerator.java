package tech.intellispaces.framework.core.annotation.processor.domain;

import tech.intellispaces.framework.commons.string.StringFunctions;
import tech.intellispaces.framework.core.annotation.ObjectHandle;
import tech.intellispaces.framework.core.common.NameFunctions;
import tech.intellispaces.framework.core.exception.TraverseException;
import tech.intellispaces.framework.core.object.ObjectHandleTypes;
import tech.intellispaces.framework.core.transition.TransitionMethod0;
import tech.intellispaces.framework.core.transition.TransitionMethod1;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;
import tech.intellispaces.framework.javastatements.statement.method.MethodParam;
import tech.intellispaces.framework.javastatements.statement.method.MethodStatement;
import tech.intellispaces.framework.javastatements.statement.reference.CustomTypeReference;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class MovableDowngradeObjectHandleGenerator extends AbstractObjectHandleGenerator {
  private final CustomTypeReference baseDomainType;
  private String classTypeParams;
  private String classTypeParamsBrief;
  private String movableObjectHandleName;
  private String childDomainClassSimpleName;
  private String childObjectHandleType;
  private String childField;
  private String domainClassSimpleName;

  public MovableDowngradeObjectHandleGenerator(CustomType annotatedType, CustomTypeReference baseDomainType) {
    super(annotatedType);
    this.baseDomainType = baseDomainType;
  }

  @Override
  protected ObjectHandleTypes getObjectHandleType() {
    return ObjectHandleTypes.Movable;
  }

  @Override
  protected String templateName() {
    return "/movable_downgrade_object_handle.template";
  }

  @Override
  protected Map<String, Object> templateVariables() {
    Map<String, Object> vars = new HashMap<>();
    vars.put("generatedAnnotation", generatedAnnotation());
    vars.put("packageName", context.packageName());
    vars.put("sourceClassName", sourceClassCanonicalName());
    vars.put("sourceClassSimpleName", sourceClassSimpleName());
    vars.put("classSimpleName", context.generatedClassSimpleName());
    vars.put("classTypeParams", classTypeParams);
    vars.put("classTypeParamsBrief", classTypeParamsBrief);
    vars.put("childDomainClassSimpleName", childDomainClassSimpleName);
    vars.put("childObjectHandleType", childObjectHandleType);
    vars.put("childField", childField);
    vars.put("methods", methods);
    vars.put("importedClasses", context.getImports());
    vars.put("movableObjectHandleName", movableObjectHandleName);
    vars.put("domainClassSimpleName", domainClassSimpleName);
    return vars;
  }

  @Override
  protected boolean analyzeAnnotatedType() {
    context.generatedClassCanonicalName(
        NameFunctions.getMovableDowngradeObjectHandleTypename(annotatedType, baseDomainType.targetType()));

    context.addImport(ObjectHandle.class);
    context.addImport(TraverseException.class);
    context.addImport(TransitionMethod0.class);
    context.addImport(TransitionMethod1.class);

    movableObjectHandleName = context.addToImportAndGetSimpleName(
        NameFunctions.getMovableObjectHandleTypename(baseDomainType.targetType().className()));
    domainClassSimpleName = context.addToImportAndGetSimpleName(baseDomainType.targetType().canonicalName());

    classTypeParams = annotatedType.typeParametersFullDeclaration();
    classTypeParamsBrief = baseDomainType.typeArgumentsDeclaration(context::addToImportAndGetSimpleName);
    childField = StringFunctions.lowercaseFirstLetter(annotatedType.simpleName());
    childObjectHandleType = getChildObjectHandleType();
    childDomainClassSimpleName = annotatedType.simpleName();

    analyzeObjectHandleMethods(baseDomainType.effectiveTargetType());
    return true;
  }

  @Override
  protected String buildMethod(MethodStatement method) {
    var sb = new StringBuilder();
    sb.append("public ");
    addMethodTypeParameters(sb, method);
    addMethodReturnType(sb, method);
    sb.append(" ");
    sb.append(method.name());
    sb.append("(");
    addMethodParameters(sb, method);
    sb.append(")");
    addMethodExceptions(sb, method);
    sb.append(" {\n");
    sb.append("    return (");
    addMethodReturnType(sb, method);
    sb.append(") this.").append(childField).append(".");
    sb.append(method.name());
    sb.append("(");
    sb.append(method.params().stream()
        .map(MethodParam::name)
        .collect(Collectors.joining(", ")));
    sb.append(");\n");
    sb.append("}\n");
    return sb.toString();
  }

  private String getChildObjectHandleType() {
    return context.addToImportAndGetSimpleName(
        NameFunctions.getMovableObjectHandleTypename(annotatedType.className())
    );
  }
}
