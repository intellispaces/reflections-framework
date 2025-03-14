package tech.intellispaces.jaquarius.annotationprocessor.domain;

import tech.intellispaces.commons.reflection.customtype.CustomType;
import tech.intellispaces.jaquarius.naming.NameConventionFunctions;

public abstract class AbstractPlainObjectGenerator extends AbstractObjectGenerator {

  public AbstractPlainObjectGenerator(CustomType domainType) {
    super(domainType);
  }

  protected String getUndefinedOriginHandleClassName() {
    return addImportAndGetSimpleName(NameConventionFunctions.getUndefinedPlainObjectTypename(sourceArtifact().className()));
  }
}
