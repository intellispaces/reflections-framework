package tech.intellispaces.framework.core.annotation.processor.domain;

import tech.intellispaces.framework.core.annotation.Domain;
import tech.intellispaces.framework.core.annotation.processor.AbstractObjectHandleGenerator;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;
import tech.intellispaces.framework.javastatements.statement.method.MethodStatement;

import java.util.stream.Stream;

abstract class AbstractDomainObjectHandleGenerator extends AbstractObjectHandleGenerator {

  public AbstractDomainObjectHandleGenerator(CustomType domainType) {
    super(domainType);
  }

  protected Stream<MethodStatement> getObjectHandleMethods(CustomType customType) {
    return customType.actualMethods().stream()
        .filter(m -> m.owner().hasAnnotation(Domain.class))
        .filter(m -> !m.isDefault());
  }
}
