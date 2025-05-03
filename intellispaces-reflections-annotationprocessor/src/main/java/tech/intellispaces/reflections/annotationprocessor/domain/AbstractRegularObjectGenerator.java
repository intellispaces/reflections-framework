package tech.intellispaces.reflections.annotationprocessor.domain;

import tech.intellispaces.reflections.naming.NameConventionFunctions;
import tech.intellispaces.jstatements.customtype.CustomType;

public abstract class AbstractRegularObjectGenerator extends AbstractObjectGenerator {

  public AbstractRegularObjectGenerator(CustomType domainType) {
    super(domainType);
  }

  protected String getGeneralOriginHandleClassName() {
    return addImportAndGetSimpleName(
        NameConventionFunctions.getGeneralRegularObjectTypename(sourceArtifact().className(), false)
    );
  }
}
