package tech.intellispacesframework.core.annotation.processor.domain;

import tech.intellispacesframework.commons.action.Action;
import tech.intellispacesframework.commons.string.StringFunctions;
import tech.intellispacesframework.commons.type.TypeFunctions;
import tech.intellispacesframework.core.annotation.Transition;
import tech.intellispacesframework.core.annotation.processor.AbstractGenerator;
import tech.intellispacesframework.javastatements.statement.custom.CustomType;
import tech.intellispacesframework.javastatements.statement.custom.MethodStatement;
import tech.intellispacesframework.javastatements.statement.reference.NamedTypeReference;

import java.util.List;
import java.util.Map;

import static tech.intellispacesframework.core.util.Actions.addSeparatorAction;

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
      Action addCommaAction = addSeparatorAction(sb, ", ");
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
    Action addCommaAction = addSeparatorAction(sb, ", ");
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
