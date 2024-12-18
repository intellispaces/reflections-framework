package tech.intellispaces.jaquarius.annotationprocessor.domain;

import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.general.collection.ArraysFunctions;
import tech.intellispaces.general.text.StringFunctions;
import tech.intellispaces.general.type.Type;
import tech.intellispaces.general.type.Types;
import tech.intellispaces.jaquarius.annotation.Channel;
import tech.intellispaces.jaquarius.annotation.ObjectHandle;
import tech.intellispaces.jaquarius.channel.Channel0;
import tech.intellispaces.jaquarius.channel.Channel1;
import tech.intellispaces.jaquarius.channel.ChannelMethod0;
import tech.intellispaces.jaquarius.channel.ChannelMethod1;
import tech.intellispaces.jaquarius.channel.MappingChannel;
import tech.intellispaces.jaquarius.exception.TraverseException;
import tech.intellispaces.jaquarius.naming.NameConventionFunctions;
import tech.intellispaces.jaquarius.object.ObjectHandleFunctions;
import tech.intellispaces.jaquarius.object.reference.ObjectHandleType;
import tech.intellispaces.jaquarius.object.reference.ObjectHandleTypes;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForm;
import tech.intellispaces.jaquarius.space.domain.DomainFunctions;
import tech.intellispaces.jaquarius.traverse.TraverseTypes;
import tech.intellispaces.java.reflection.customtype.CustomType;
import tech.intellispaces.java.reflection.method.MethodStatement;
import tech.intellispaces.java.reflection.reference.CustomTypeReference;
import tech.intellispaces.java.reflection.reference.CustomTypeReferences;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class UnmovableDownwardObjectHandleGenerator extends ConversionObjectHandleGenerator {
  private String classTypeParams;
  private String classTypeParamsBrief;
  private String unmovableObjectHandleName;
  private String childDomainClassSimpleName;
  private String childObjectHandleType;
  private String parentDomainClassSimpleName;
  private String domainClassSimpleName;
  protected String domainTypeParams;
  protected String domainTypeParamsBrief;
  protected String domainTypeArguments;
  private boolean isAlias;
  private String primaryDomainSimpleName;
  private String primaryDomainTypeArguments;
  private String domainType;

  public UnmovableDownwardObjectHandleGenerator(
      CustomType annotatedType, CustomTypeReference parentDomainType
  ) {
    super(annotatedType, parentDomainType);
  }

  @Override
  public boolean isRelevant(ArtifactGeneratorContext context) {
    return true;
  }

  @Override
  protected ObjectHandleType getObjectHandleType() {
    return ObjectHandleTypes.Unmovable;
  }

  @Override
  public String generatedArtifactName() {
    return NameConventionFunctions.getUnmovableDownwardObjectHandleTypename(sourceArtifact(), parentDomainType.targetType());
  }

  @Override
  protected String templateName() {
    return "/unmovable_downward_object_handle.template";
  }

  @Override
  protected boolean analyzeSourceArtifact(ArtifactGeneratorContext context) {
    addImport(Type.class);
    addImport(Types.class);
    addImport(ObjectHandle.class);
    addImport(ObjectHandleFunctions.class);
    addImport(TraverseException.class);
    addImport(Channel0.class);
    addImport(Channel1.class);
    addImport(ChannelMethod0.class);
    addImport(ChannelMethod1.class);
    addImport(MappingChannel.class);

    unmovableObjectHandleName = addToImportAndGetSimpleName(
        NameConventionFunctions.getUnmovableObjectHandleTypename(parentDomainType.targetType().className()));
    domainClassSimpleName = addToImportAndGetSimpleName(parentDomainType.targetType().canonicalName());

    domainTypeParams = parentDomainType.targetType().typeParametersFullDeclaration();
    domainTypeParamsBrief = parentDomainType.targetType().typeParametersBriefDeclaration();

    classTypeParams = sourceArtifact().typeParametersFullDeclaration();
    classTypeParamsBrief = sourceArtifact().typeParametersBriefDeclaration();
    domainTypeArguments = parentDomainType.typeArgumentsDeclaration(this::addToImportAndGetSimpleName);
    childFieldName = StringFunctions.lowercaseFirstLetter(StringFunctions.removeTailOrElseThrow(sourceArtifact().simpleName(), "Domain"));
    childObjectHandleType = getChildObjectHandleType();
    childDomainClassSimpleName = sourceArtifact().simpleName();
    parentDomainClassSimpleName = addToImportAndGetSimpleName(parentDomainType.targetType().canonicalName());

    CustomType actualParentDomainType = buildActualType(parentDomainType.targetType(), context);
    CustomTypeReference actualParentDomainTypeReference = CustomTypeReferences.get(actualParentDomainType, parentDomainType.typeArguments());
    CustomType effectiveActualParentDomainType = actualParentDomainTypeReference.effectiveTargetType();

    analyzeObjectHandleMethods(effectiveActualParentDomainType, context);

    Optional<CustomTypeReference> mainEquivalentDomain = DomainFunctions.getAliasBaseDomain(sourceArtifact());
    isAlias = mainEquivalentDomain.isPresent();
    if (isAlias) {
      primaryDomainSimpleName = addToImportAndGetSimpleName(
          mainEquivalentDomain.get().targetType().canonicalName()
      );
      primaryDomainTypeArguments = mainEquivalentDomain.get().typeArgumentsDeclaration(
          this::addToImportAndGetSimpleName
      );
      domainType = buildDomainType(mainEquivalentDomain.get().targetType(), mainEquivalentDomain.get().typeArguments());
    } else {
      domainType = buildDomainType(parentDomainType.targetType(), (List) sourceArtifact().typeParameters());
    }

    addVariable("classTypeParams", classTypeParams);
    addVariable("classTypeParamsBrief", classTypeParamsBrief);
    addVariable("domainTypeParams", domainTypeParams);
    addVariable("domainTypeParamsBrief", domainTypeParamsBrief);
    addVariable("domainTypeArguments", domainTypeArguments);
    addVariable("childDomainClassSimpleName", childDomainClassSimpleName);
    addVariable("parentDomainClassSimpleName", parentDomainClassSimpleName);
    addVariable("childObjectHandleType", childObjectHandleType);
    addVariable("childField", childFieldName);
    addVariable("methods", methods);
    addVariable("unmovableObjectHandleName", unmovableObjectHandleName);
    addVariable("domainClassSimpleName", domainClassSimpleName);
    addVariable("isAlias", isAlias);
    addVariable("primaryDomainSimpleName", primaryDomainSimpleName);
    addVariable("primaryDomainTypeArguments", primaryDomainTypeArguments);
    addVariable("domainType", domainType);
    return true;
  }

  @Override
  protected Stream<MethodStatement> getObjectHandleMethods(
      CustomType customType, ArtifactGeneratorContext context
  ) {
    return buildActualType(parentDomainType.targetType(), context).actualMethods().stream()
        .filter(m -> excludeDeepConversionMethods(m, customType))
        .filter(this::isNotMovingMethod)
        .filter(DomainFunctions::isNotDomainClassGetter);
  }

  private boolean isNotMovingMethod(MethodStatement method) {
    Optional<Channel> channel = method.selectAnnotation(Channel.class);
    if (channel.isEmpty()) {
      return true;
    }
    return !ArraysFunctions.containsAny(
        channel.orElseThrow().allowedTraverse(), TraverseTypes.Moving, TraverseTypes.MappingOfMoving
    );
  }

  @Override
  protected Map<String, String> generateMethod(MethodStatement method, ObjectReferenceForm targetForm, int methodOrdinal) {
    if (method.hasAnnotation(Channel.class)) {
      return generateMethodNormal(method, targetForm, methodOrdinal);
    } else {
      return generateAdditionalMethod(convertMethodBeforeGenerate(method));
    }
  }

  protected Map<String, String> generateMethodNormal(
      MethodStatement method, ObjectReferenceForm targetForm, int methodIndex
  ) {
    var sb = new StringBuilder();
    sb.append("public ");
    appendMethodTypeParameters(sb, method);
    appendMethodReturnHandleType(sb, method, targetForm);
    sb.append(" ");
    sb.append(getMethodName(method, targetForm));
    sb.append("(");
    appendMethodParameters(sb, method);
    sb.append(")");
    appendMethodExceptions(sb, method);
    sb.append(" {\n");
    sb.append("  ");
    buildReturnStatement(sb, method, targetForm);
    sb.append("\n}\n");
    return Map.of(
        "javadoc", buildGeneratedMethodJavadoc(method.owner().canonicalName(), method),
        "declaration", sb.toString()
    );
  }

  private Map<String, String> generateAdditionalMethod(MethodStatement method) {
    var sb = new StringBuilder();
    sb.append("public ");
    appendMethodTypeParameters(sb, method);
    appendMethodReturnType(sb, method);
    sb.append(" ");
    sb.append(method.name());
    sb.append("(");
    appendMethodParameters(sb, method);
    sb.append(")");
    appendMethodExceptions(sb, method);
    sb.append(" {\n");
    sb.append("  return this.")
        .append(childFieldName)
        .append(".")
        .append(method.name())
        .append("();\n");
    sb.append("}");
    return Map.of(
        "javadoc", buildGeneratedMethodJavadoc(method.owner().canonicalName(), method),
        "declaration", sb.toString()
    );
  }

  private String getChildObjectHandleType() {
    return addToImportAndGetSimpleName(
        NameConventionFunctions.getUnmovableObjectHandleTypename(sourceArtifact().className())
    );
  }
}
