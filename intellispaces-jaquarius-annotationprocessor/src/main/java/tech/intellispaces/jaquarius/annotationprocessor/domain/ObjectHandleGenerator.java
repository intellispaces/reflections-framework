package tech.intellispaces.jaquarius.annotationprocessor.domain;

import tech.intellispaces.jaquarius.annotationprocessor.AbstractObjectHandleGenerator;
import tech.intellispaces.jaquarius.naming.NameConventionFunctions;
import tech.intellispaces.jaquarius.space.domain.DomainFunctions;
import tech.intellispaces.java.reflection.customtype.CustomType;
import tech.intellispaces.java.reflection.reference.CustomTypeReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

abstract class ObjectHandleGenerator extends AbstractObjectHandleGenerator {
  protected boolean isAlias;
  protected String domainType;
  protected String generalObjectHandle;
  protected String primaryObjectHandle;
  protected String domainTypeParamsFull;
  protected String domainTypeParamsBrief;
  protected String primaryDomainSimpleName;
  protected String primaryDomainTypeArguments;
  protected final List<Map<String, String>> conversionMethods = new ArrayList<>();

  public ObjectHandleGenerator(CustomType domainType) {
    super(domainType);
  }

  protected void analyzeDomain() {
    domainTypeParamsFull = sourceArtifact().typeParametersFullDeclaration();
    domainTypeParamsBrief = sourceArtifact().typeParametersBriefDeclaration();
    generalObjectHandle = addToImportAndGetSimpleName(
        NameConventionFunctions.getGeneralObjectHandleTypename(sourceArtifact().className())
    );
  }

  protected void analyzeConversionMethods(CustomType domainType) {
    List<CustomTypeReference> parents = DomainFunctions.getEffectiveParents(domainType);
    parents.stream()
        .map(this::buildConversionMethod)
        .forEach(conversionMethods::add);
  }

  private Map<String, String> buildConversionMethod(CustomTypeReference parent) {
    var sb = new StringBuilder();
    sb.append(buildObjectHandleDeclaration(parent, getObjectHandleType()));
    sb.append(" ");
    sb.append(NameConventionFunctions.getConversionMethodName(parent));
    sb.append("()");
    return Map.of(
        "declaration", sb.toString()
    );
  }
}
