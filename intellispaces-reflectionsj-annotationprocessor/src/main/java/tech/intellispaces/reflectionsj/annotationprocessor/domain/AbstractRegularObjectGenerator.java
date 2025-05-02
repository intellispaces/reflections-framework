package tech.intellispaces.reflectionsj.annotationprocessor.domain;

import tech.intellispaces.reflectionsj.naming.NameConventionFunctions;
import tech.intellispaces.statementsj.customtype.CustomType;

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
