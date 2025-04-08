package tech.intellispaces.jaquarius.annotationprocessor.domain;

import tech.intellispaces.jaquarius.naming.NameConventionFunctions;
import tech.intellispaces.reflection.customtype.CustomType;

public abstract class AbstractPlainObjectGenerator extends AbstractObjectGenerator {

  public AbstractPlainObjectGenerator(CustomType domainType) {
    super(domainType);
  }

  protected String getUndefinedOriginHandleClassName() {
    return addImportAndGetSimpleName(NameConventionFunctions.getUndefinedPlainObjectTypename(sourceArtifact().className()));
  }
}
