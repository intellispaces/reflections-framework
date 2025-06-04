package tech.intellispaces.reflections.framework.annotationprocessor.domain;

import tech.intellispaces.javareflection.customtype.CustomType;
import tech.intellispaces.reflections.framework.naming.NameConventionFunctions;

public abstract class AbstractRegularFormGenerator extends AbstractReflectionFormGenerator {

  public AbstractRegularFormGenerator(CustomType domainType) {
    super(domainType);
  }

  protected String getGeneralRegularFormClassName() {
    return addImportAndGetSimpleName(
        NameConventionFunctions.getGeneralRegularFormClassName(sourceArtifact().className(), false)
    );
  }
}
