package tech.intellispaces.framework.core.annotation.processor.domain;

import tech.intellispaces.framework.core.annotation.processor.AbstractGenerator;
import tech.intellispaces.framework.core.util.Actions;
import tech.intellispaces.framework.commons.action.Action;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;
import tech.intellispaces.framework.javastatements.statement.custom.MethodParam;
import tech.intellispaces.framework.javastatements.statement.custom.MethodStatement;
import tech.intellispaces.framework.javastatements.statement.reference.TypeReference;

import java.util.List;
import java.util.stream.Collectors;

abstract class AbstractObjectHandleGenerator extends AbstractGenerator {
  protected String domainTypeParamsFull;
  protected String domainTypeParamsBrief;
  protected List<String> methods;

  AbstractObjectHandleGenerator(CustomType domainType) {
    super(domainType);
  }

  protected void analyzeObjectHandleMethods(CustomType domainType) {
    this.methods = domainType.declaredMethods().stream()
        .map(this::buildMethodSignature)
        .toList();
  }

  private String buildMethodSignature(MethodStatement method) {
    var signature = new StringBuilder();
    if (method.returnType().isEmpty()) {
      signature.append("void");
    } else {
      TypeReference domainReturnType = method.returnType().get();
      signature.append(getHandleTypename(domainReturnType, context.getImportConsumer()));
      signature.append(" ");
      signature.append(method.name());
      signature.append("(");

      Action addCommaAction = Actions.addSeparatorAction(signature, ", ");
      for (MethodParam param : method.params()) {
        addCommaAction.execute();
        signature.append(getHandleTypename(param.type(), context.getImportConsumer()));
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
