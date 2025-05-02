package tech.intellispaces.jaquarius.annotationprocessor.domain;

import tech.intellispaces.jaquarius.naming.NameConventionFunctions;
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
