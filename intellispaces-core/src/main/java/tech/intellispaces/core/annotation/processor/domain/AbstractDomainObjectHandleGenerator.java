package tech.intellispaces.core.annotation.processor.domain;

import tech.intellispaces.actions.Executor;
import tech.intellispaces.actions.string.StringActions;
import tech.intellispaces.core.annotation.processor.AbstractObjectHandleGenerator;
import tech.intellispaces.core.common.NameConventionFunctions;
import tech.intellispaces.core.object.ObjectFunctions;
import tech.intellispaces.core.space.domain.DomainFunctions;
import tech.intellispaces.javastatements.customtype.CustomType;
import tech.intellispaces.javastatements.method.MethodStatement;
import tech.intellispaces.javastatements.reference.CustomTypeReference;

import javax.annotation.processing.RoundEnvironment;
import java.util.List;
import java.util.stream.Stream;

abstract class AbstractDomainObjectHandleGenerator extends AbstractObjectHandleGenerator {

  public AbstractDomainObjectHandleGenerator(CustomType domainType) {
    super(domainType);
  }

  protected Stream<MethodStatement> getObjectHandleMethods(
      CustomType customType, RoundEnvironment roundEnv
  ) {
    return customType.actualMethods().stream()
        .filter(m -> DomainFunctions.isDomainType(m.owner()))
        .filter(m -> !m.isDefault());
  }

  protected String buildRootObjectHandlesDeclaration() {
    List<CustomTypeReference> parents = annotatedType.parentTypes().stream()
        .filter(DomainFunctions::isDomainType)
        .toList();
    var sb = new StringBuilder();
    if (parents.isEmpty()) {
      sb.append(context.addToImportAndGetSimpleName(ObjectFunctions.getObjectHandleClass(getObjectHandleType())));
      sb.append("<");
      sb.append(sourceClassSimpleName());
      sb.append(domainTypeParamsBrief);
      sb.append(">");
    } else {
      Executor commaAppender = StringActions.commaAppender(sb);
      for (CustomTypeReference parent : parents) {
        commaAppender.execute();
        sb.append(context.addToImportAndGetSimpleName(
            NameConventionFunctions.getObjectHandleTypename(parent.targetType().className(), getObjectHandleType()))
        );
        sb.append(parent.typeArgumentsDeclaration(this::typeConverter));
      }
    }
    return sb.toString();
  }

  protected String typeConverter(String className) {
    String handleCanonicalName = ObjectFunctions.getObjectHandleTypename(className, getObjectHandleType());
    return context.addToImportAndGetSimpleName(handleCanonicalName);
  }
}
