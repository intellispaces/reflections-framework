package tech.intellispaces.reflections.framework.annotationprocessor.domain;

import tech.intellispaces.jstatements.customtype.CustomType;
import tech.intellispaces.reflections.framework.naming.NameConventionFunctions;

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
