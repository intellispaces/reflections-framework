package tech.intellispaces.jaquarius.annotationprocessor.domain;

import tech.intellispaces.commons.java.reflection.customtype.CustomType;
import tech.intellispaces.jaquarius.naming.NameConventionFunctions;

public abstract class PureObjectGenerator extends ObjectHandleGenerator {

  public PureObjectGenerator(CustomType domainType) {
    super(domainType);
  }

  protected String getUndefinedOriginHandleClassName() {
    return addImportAndGetSimpleName(NameConventionFunctions.getUndefinedPureObjectTypename(sourceArtifact().className()));
  }
}
