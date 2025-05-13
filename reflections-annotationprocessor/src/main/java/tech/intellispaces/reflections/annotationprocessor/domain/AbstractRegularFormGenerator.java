package tech.intellispaces.reflections.annotationprocessor.domain;

import tech.intellispaces.reflections.framework.naming.NameConventionFunctions;
import tech.intellispaces.jstatements.customtype.CustomType;

public abstract class AbstractRegularFormGenerator extends AbstractReflectionFormGenerator {

  public AbstractRegularFormGenerator(CustomType domainType) {
    super(domainType);
  }

  protected String getGeneralOriginHandleClassName() {
    return addImportAndGetSimpleName(
        NameConventionFunctions.getGeneralRegularObjectTypename(sourceArtifact().className(), false)
    );
  }
}
