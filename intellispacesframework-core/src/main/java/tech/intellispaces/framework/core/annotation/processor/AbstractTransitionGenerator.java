package tech.intellispaces.framework.core.annotation.processor;

import tech.intellispaces.framework.core.annotation.Transition;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;
import tech.intellispaces.framework.javastatements.statement.method.MethodStatement;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractTransitionGenerator extends AbstractGenerator {
  protected final MethodStatement transitionMethod;
  private String transitionMethodSignature;

  public AbstractTransitionGenerator(CustomType annotatedType, MethodStatement transitionMethod) {
    super(annotatedType);
    this.transitionMethod = transitionMethod;
  }

  protected abstract String getTransitionClassCanonicalName();

  protected abstract String getTransitionMethodSignature();

  @Override
  protected String templateName() {
    return "/transition.template";
  }

  @Override
  protected Map<String, Object> templateVariables() {
    Map<String, Object> vars = new HashMap<>();
    vars.put("generatedAnnotation", generatedAnnotation());
    vars.put("packageName", context.packageName());
    vars.put("sourceClassName", sourceClassCanonicalName());
    vars.put("sourceClassSimpleName", sourceClassSimpleName());
    vars.put("targetClassLink", ProcessorFunctions.getDomainClassLink(transitionMethod.returnType().orElseThrow()));
    vars.put("classSimpleName", context.generatedClassSimpleName());
    vars.put("importedClasses", context.getImports());
    vars.put("methodSignature", transitionMethodSignature);
    vars.put("transitionMethodName", transitionMethod.name());
    vars.put("tid", getTid());
    return vars;
  }

  @Override
  protected boolean analyzeAnnotatedType() {
    context.generatedClassCanonicalName(getTransitionClassCanonicalName());
    if (annotatedType.isNested()) {
      context.addImport(sourceClassCanonicalName());
    }
    context.addImport(Transition.class);

    transitionMethodSignature = getTransitionMethodSignature();

    return true;
  }

  private String getTid() {
    return transitionMethod.selectAnnotation(Transition.class).orElseThrow().value();
  }
}
