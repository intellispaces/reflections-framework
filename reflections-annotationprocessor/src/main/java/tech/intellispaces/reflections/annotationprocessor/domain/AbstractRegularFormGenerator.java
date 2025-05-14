package tech.intellispaces.reflections.annotationprocessor.domain;

import tech.intellispaces.reflections.framework.naming.NameConventionFunctions;
import tech.intellispaces.jstatements.customtype.CustomType;

public abstract class AbstractRegularFormGenerator extends AbstractReflectionFormGenerator {

  public AbstractRegularFormGenerator(CustomType domainType) {
    super(domainType);
  }

  protected String getGeneralRegularFormClassName() {
    return addImportAndGetSimpleName(
        NameConventionFunctions.getGeneralRegularFormClassname(sourceArtifact().className(), false)
    );
  }
}
