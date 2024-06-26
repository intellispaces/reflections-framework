package tech.intellispaces.framework.core.annotation.processor.ontology;

import tech.intellispaces.framework.commons.string.StringFunctions;
import tech.intellispaces.framework.commons.type.TypeFunctions;
import tech.intellispaces.framework.core.annotation.Transition;
import tech.intellispaces.framework.core.annotation.processor.AbstractGenerator;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;
import tech.intellispaces.framework.javastatements.statement.custom.MethodStatement;

import java.util.Map;

public class TransitionGenerator extends AbstractGenerator {
  private final MethodStatement method;
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
        "classTypeParams", "",
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
    methodSignature = getMethodSignature(method);
    return true;
  }

  private String getTid() {
    return method.selectAnnotation(Transition.class).orElseThrow().value();
  }

  private String getGeneratedClassCanonicalName() {
    String packageName = TypeFunctions.getPackageName(annotatedType.canonicalName());
    String simpleName = StringFunctions.capitalizeFirstLetter(method.name()) + "Transition";
    return TypeFunctions.joinPackageAndClassname(packageName, simpleName);
  }
}
