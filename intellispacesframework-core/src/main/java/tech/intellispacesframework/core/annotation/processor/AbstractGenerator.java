package tech.intellispacesframework.core.annotation.processor;

import tech.intellispacesframework.annotationprocessor.artifact.JavaArtifactContext;
import tech.intellispacesframework.annotationprocessor.generator.TemplateBasedJavaArtifactGenerator;
import tech.intellispacesframework.commons.action.Action;
import tech.intellispacesframework.commons.type.TypeFunctions;
import tech.intellispacesframework.javastatements.statement.custom.CustomType;
import tech.intellispacesframework.javastatements.statement.custom.MethodParam;
import tech.intellispacesframework.javastatements.statement.custom.MethodStatement;
import tech.intellispacesframework.javastatements.statement.reference.NamedTypeReference;
import tech.intellispacesframework.javastatements.statement.reference.TypeReference;

import javax.annotation.processing.Generated;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;
import static tech.intellispacesframework.core.util.Actions.addSeparatorAction;

public abstract class AbstractGenerator extends TemplateBasedJavaArtifactGenerator {
  protected String generatedAnnotation;
  protected final JavaArtifactContext context = new JavaArtifactContext();

  public AbstractGenerator(CustomType annotatedType) {
    super(annotatedType);
    context.addImport(Generated.class);
  }

  @Override
  protected String canonicalName() {
    return context.generatedClassCanonicalName();
  }

  protected String sourceClassCanonicalName() {
    return annotatedType.canonicalName();
  }

  protected String sourceClassSimpleName() {
    if (annotatedType.isNested()) {
      return context.simpleNameOf(sourceClassCanonicalName());
    }
    return annotatedType.simpleName();
  }

  protected String generatedAnnotation() {
    if (generatedAnnotation == null) {
      generatedAnnotation = """
        @Generated(
          value = "%s",
          date = "%s",
          comments = "Generated with library %s"
        )""".formatted(
          this.getClass().getCanonicalName(),
          ZonedDateTime.now().format(ISO_OFFSET_DATE_TIME),
          TypeFunctions.getJavaLibraryName(this.getClass())
      );
    }
    return generatedAnnotation;
  }

  protected String getMethodSignature(MethodStatement method) {
    return getMethodSignature(method, method.name(), List.of());
  }

  protected String getMethodSignature(MethodStatement method, String methodName) {
    return getMethodSignature(method, methodName, List.of());
  }

  protected String getMethodSignature(MethodStatement method, String methodName, List<String> additionalParams) {
    var signature = new StringBuilder();

    if (!method.typeParameters().isEmpty()) {
      Action addCommaAction = addSeparatorAction(signature, ", ");
      signature.append("<");
      addCommaAction.execute();
      for (NamedTypeReference typeParam : method.typeParameters()) {
        signature.append(typeParam.actualDeclaration());
      }
      signature.append("> ");
    }

    if (method.returnType().isEmpty()) {
      signature.append("void");
    } else {
      TypeReference returnType = method.returnType().get();
      context.addImports(returnType.dependencyTypenames());
      signature.append(returnType.actualDeclaration());
      signature.append(" ");
      signature.append(methodName);
      signature.append("(");

      Action addCommaAction = addSeparatorAction(signature, ", ");
      for (String additionalParam : additionalParams) {
        addCommaAction.execute();
        signature.append(additionalParam);
      }
      for (MethodParam param : method.params()) {
        addCommaAction.execute();
        context.addImports(param.type().dependencyTypenames());
        signature.append(param.type().actualDeclaration());
        signature.append(" ");
        signature.append(param.name());
      }
      signature.append(")");

      String exceptions = method.exceptions().stream()
          .map(e -> e.asCustomTypeReference().orElseThrow().targetType())
          .peek(e -> context.addImport(e.canonicalName()))
          .map(e -> context.simpleNameOf(e.canonicalName()))
          .collect(Collectors.joining(", "));
      if (!exceptions.isEmpty()) {
        signature.append(" throws ").append(exceptions);
      }
    }
    return signature.toString();
  }
}
