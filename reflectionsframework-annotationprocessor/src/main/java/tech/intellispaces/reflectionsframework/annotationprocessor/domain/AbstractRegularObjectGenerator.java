package tech.intellispaces.reflectionsframework.annotationprocessor.domain;

import tech.intellispaces.reflectionsframework.naming.NameConventionFunctions;
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
