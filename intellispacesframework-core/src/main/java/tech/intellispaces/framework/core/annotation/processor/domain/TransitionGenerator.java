package tech.intellispaces.framework.core.annotation.processor.domain;

import tech.intellispaces.framework.core.annotation.Transition;
import tech.intellispaces.framework.core.annotation.processor.AbstractGenerator;
import tech.intellispaces.framework.core.common.ActionFunctions;
import tech.intellispaces.framework.commons.action.Action;
import tech.intellispaces.framework.commons.string.StringFunctions;
import tech.intellispaces.framework.commons.type.TypeFunctions;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;
import tech.intellispaces.framework.javastatements.statement.custom.MethodStatement;
import tech.intellispaces.framework.javastatements.statement.reference.NamedTypeReference;

import java.util.List;
import java.util.Map;

public class TransitionGenerator extends AbstractGenerator {
  private final MethodStatement method;
  private String classTypeParams;
  private String methodSignature;
  private String tid;

  public TransitionGenerator(CustomType annotatedType, MethodStatement method) {
    super(annotatedType);
    this.method = method;
  }

  @Override
  protected String templateName() {
    return "/Transition.template";
  }

  @Override
  protected Map<String, Object> templateVariables() {
    return Map.of(
        "generatedAnnotation", generatedAnnotation(),
        "packageName", context.packageName(),
        "sourceClassName", sourceClassCanonicalName(),
        "sourceClassSimpleName", sourceClassSimpleName(),
        "classSimpleName", context.generatedClassSimpleName(),
        "classTypeParams", classTypeParams,
        "importedClasses", context.getImports(),
        "methodSignature", methodSignature,
        "tid", tid
    );
  }

  @Override
  protected boolean analyzeAnnotatedType() {
    context.generatedClassCanonicalName(getGeneratedClassCanonicalName());
    context.addImport(Transition.class);

    tid = getTid();
    classTypeParams = getClassTypeParams();
    methodSignature = getMethodSignature(method, getTransitionMethodName(), List.of(getSourceParamDeclaration()));
    return true;
  }

  private String getSourceParamDeclaration() {
    var sb = new StringBuilder();
    sb.append(annotatedType.simpleName());
    if (!annotatedType.typeParameters().isEmpty()) {
      sb.append("<");
      Action addCommaAction = ActionFunctions.buildAppendSeparatorAction(sb, ", ");
      for (NamedTypeReference typeParam : annotatedType.typeParameters()) {
        addCommaAction.execute();
        sb.append(typeParam.formalBriefDeclaration());
      }
      sb.append(">");
    }
    sb.append(" source");
    return sb.toString();
  }

  private String getTid() {
    return method.selectAnnotation(Transition.class).orElseThrow().value();
  }

  private String getClassTypeParams() {
    if (annotatedType.typeParameters().isEmpty()) {
      return "";
    }

    var sb = new StringBuilder();
    Action addCommaAction = ActionFunctions.buildAppendSeparatorAction(sb, ", ");
    sb.append("<");
    for (NamedTypeReference typeParam : annotatedType.typeParameters()) {
      addCommaAction.execute();
      sb.append(typeParam.actualDeclaration());
    }
    sb.append(">");
    return sb.toString();
  }

  private String getTransitionMethodName() {
    return StringFunctions.lowercaseFirstLetter(annotatedType.simpleName()) + "To" +
        StringFunctions.capitalizeFirstLetter(method.name());
  }

  private String getGeneratedClassCanonicalName() {
    String packageName = TypeFunctions.getPackageName(annotatedType.canonicalName());
    String simpleName = StringFunctions.capitalizeFirstLetter(annotatedType.simpleName()) + "To" +
        StringFunctions.capitalizeFirstLetter(method.name()) + "Transition";
    return TypeFunctions.joinPackageAndClassname(packageName, simpleName);
  }
}
