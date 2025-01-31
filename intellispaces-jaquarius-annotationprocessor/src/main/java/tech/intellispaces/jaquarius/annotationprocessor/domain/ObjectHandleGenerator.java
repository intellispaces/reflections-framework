package tech.intellispaces.jaquarius.annotationprocessor.domain;

import tech.intellispaces.action.runnable.RunnableAction;
import tech.intellispaces.action.text.StringActions;
import tech.intellispaces.general.exception.NotImplementedExceptions;
import tech.intellispaces.jaquarius.annotationprocessor.AbstractObjectHandleGenerator;
import tech.intellispaces.jaquarius.naming.NameConventionFunctions;
import tech.intellispaces.jaquarius.object.reference.ObjectHandleFunctions;
import tech.intellispaces.jaquarius.object.reference.ObjectHandleTypes;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForms;
import tech.intellispaces.jaquarius.space.domain.DomainFunctions;
import tech.intellispaces.java.reflection.customtype.CustomType;
import tech.intellispaces.java.reflection.reference.CustomTypeReference;
import tech.intellispaces.java.reflection.reference.NamedReference;
import tech.intellispaces.java.reflection.reference.NotPrimitiveReference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

abstract class ObjectHandleGenerator extends AbstractObjectHandleGenerator {
  protected boolean isAlias;
  protected String domainType;
  protected String generalObjectHandle;
  protected String baseObjectHandle;
  protected String primaryObjectHandle;
  protected String typeParamsFull;
  protected String typeParamsBrief;
  protected String domainTypeParamsFull;
  protected String domainTypeParamsBrief;
  protected String primaryDomainSimpleName;
  protected String primaryDomainTypeArguments;
  protected final List<Map<String, String>> conversionMethods = new ArrayList<>();

  public ObjectHandleGenerator(CustomType domainType) {
    super(domainType);
  }

  protected void analyzeDomain() {
    typeParamsFull = ObjectHandleFunctions.getObjectHandleTypeParams(
        sourceArtifact(), ObjectHandleTypes.General, ObjectReferenceForms.Object, this::addImportAndGetSimpleName, true
    );
    typeParamsBrief = ObjectHandleFunctions.getObjectHandleTypeParams(
        sourceArtifact(), ObjectHandleTypes.General, ObjectReferenceForms.Object, this::addImportAndGetSimpleName, false
    );
    domainTypeParamsFull = sourceArtifact().typeParametersFullDeclaration();
    domainTypeParamsBrief = sourceArtifact().typeParametersBriefDeclaration();
    generalObjectHandle = addImportAndGetSimpleName(
        NameConventionFunctions.getGeneralObjectHandleTypename(sourceArtifact().className())
    );
  }

  protected String getDomainTypeParamsBrief(CustomTypeReference domainType) {
    if (domainType.typeArguments().isEmpty()) {
      return "";
    }

    var sb = new StringBuilder();
    RunnableAction commaAppender = StringActions.skipFirstTimeCommaAppender(sb);
    sb.append("<");
    for (NotPrimitiveReference typeArgument : domainType.typeArguments()) {
      commaAppender.run();
      if (typeArgument.isCustomTypeReference()) {
        CustomTypeReference customTypeReference = typeArgument.asCustomTypeReferenceOrElseThrow();
        sb.append(addImportAndGetSimpleName(customTypeReference.targetType().canonicalName()));
      } else if (typeArgument.isNamedReference()) {
        NamedReference namedReference = typeArgument.asNamedReferenceOrElseThrow();
        sb.append(namedReference.name());
      } else {
        throw NotImplementedExceptions.withCode("xG3KKaWv");
      }
    }
    sb.append(">");
    return sb.toString();
  }

  protected void analyzeConversionMethods(CustomType domainType) {
    Collection<CustomTypeReference> parents = DomainFunctions.getEffectiveSuperDomains(domainType);
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
