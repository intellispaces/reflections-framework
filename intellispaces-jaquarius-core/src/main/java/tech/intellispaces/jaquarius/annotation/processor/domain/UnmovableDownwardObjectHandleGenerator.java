package tech.intellispaces.jaquarius.annotation.processor.domain;

import tech.intellispaces.jaquarius.annotation.Channel;
import tech.intellispaces.jaquarius.annotation.ObjectHandle;
import tech.intellispaces.jaquarius.channel.Channel0;
import tech.intellispaces.jaquarius.channel.Channel1;
import tech.intellispaces.jaquarius.channel.ChannelMethod0;
import tech.intellispaces.jaquarius.channel.ChannelMethod1;
import tech.intellispaces.jaquarius.channel.MappingChannel;
import tech.intellispaces.jaquarius.common.NameConventionFunctions;
import tech.intellispaces.jaquarius.exception.TraverseException;
import tech.intellispaces.jaquarius.object.ObjectHandleFunctions;
import tech.intellispaces.jaquarius.object.reference.ObjectHandleTypes;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForm;
import tech.intellispaces.jaquarius.space.domain.DomainFunctions;
import tech.intellispaces.jaquarius.traverse.TraverseTypes;
import tech.intellispaces.entity.collection.ArraysFunctions;
import tech.intellispaces.entity.text.StringFunctions;
import tech.intellispaces.entity.type.Type;
import tech.intellispaces.entity.type.Types;
import tech.intellispaces.java.annotation.context.AnnotationProcessingContext;
import tech.intellispaces.java.reflection.customtype.CustomType;
import tech.intellispaces.java.reflection.method.MethodStatement;
import tech.intellispaces.java.reflection.reference.CustomTypeReference;
import tech.intellispaces.java.reflection.reference.CustomTypeReferences;

import javax.annotation.processing.RoundEnvironment;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class UnmovableDownwardObjectHandleGenerator extends AbstractConversionDomainObjectHandleGenerator {
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
      CustomType initiatorType, CustomType annotatedType, CustomTypeReference parentDomainType
  ) {
    super(initiatorType, annotatedType, parentDomainType);
  }

  @Override
  public boolean isRelevant(AnnotationProcessingContext context) {
    return true;
  }

  @Override
  protected ObjectHandleTypes getObjectHandleType() {
    return ObjectHandleTypes.Unmovable;
  }

  @Override
  public String artifactName() {
    return NameConventionFunctions.getUnmovableDownwardObjectHandleTypename(annotatedType, parentDomainType.targetType());
  }

  @Override
  protected String templateName() {
    return "/unmovable_downward_object_handle.template";
  }

  @Override
  protected Map<String, Object> templateVariables() {
    Map<String, Object> vars = new HashMap<>();
    vars.put("generatedAnnotation", makeGeneratedAnnotation());
    vars.put("packageName", context.packageName());
    vars.put("sourceClassName", sourceClassCanonicalName());
    vars.put("sourceClassSimpleName", sourceClassSimpleName());
    vars.put("classSimpleName", context.generatedClassSimpleName());
    vars.put("classTypeParams", classTypeParams);
    vars.put("classTypeParamsBrief", classTypeParamsBrief);
    vars.put("domainTypeParams", domainTypeParams);
    vars.put("domainTypeParamsBrief", domainTypeParamsBrief);
    vars.put("domainTypeArguments", domainTypeArguments);
    vars.put("childDomainClassSimpleName", childDomainClassSimpleName);
    vars.put("parentDomainClassSimpleName", parentDomainClassSimpleName);
    vars.put("childObjectHandleType", childObjectHandleType);
    vars.put("childField", childFieldName);
    vars.put("methods", methods);
    vars.put("importedClasses", context.getImports());
    vars.put("unmovableObjectHandleName", unmovableObjectHandleName);
    vars.put("domainClassSimpleName", domainClassSimpleName);
    vars.put("isAlias", isAlias);
    vars.put("primaryDomainSimpleName", primaryDomainSimpleName);
    vars.put("primaryDomainTypeArguments", primaryDomainTypeArguments);
    vars.put("domainType", domainType);
    return vars;
  }

  @Override
  protected boolean analyzeAnnotatedType(RoundEnvironment roundEnv) {
    context.generatedClassCanonicalName(artifactName());
    context.addImport(Type.class);
    context.addImport(Types.class);
    context.addImport(ObjectHandle.class);
    context.addImport(ObjectHandleFunctions.class);
    context.addImport(TraverseException.class);
    context.addImport(Channel0.class);
    context.addImport(Channel1.class);
    context.addImport(ChannelMethod0.class);
    context.addImport(ChannelMethod1.class);
    context.addImport(MappingChannel.class);

    unmovableObjectHandleName = context.addToImportAndGetSimpleName(
        NameConventionFunctions.getUnmovableObjectHandleTypename(parentDomainType.targetType().className()));
    domainClassSimpleName = context.addToImportAndGetSimpleName(parentDomainType.targetType().canonicalName());

    domainTypeParams = parentDomainType.targetType().typeParametersFullDeclaration();
    domainTypeParamsBrief = parentDomainType.targetType().typeParametersBriefDeclaration();

    classTypeParams = annotatedType.typeParametersFullDeclaration();
    classTypeParamsBrief = annotatedType.typeParametersBriefDeclaration();
    domainTypeArguments = parentDomainType.typeArgumentsDeclaration(context::addToImportAndGetSimpleName);
    childFieldName = StringFunctions.lowercaseFirstLetter(StringFunctions.removeTailOrElseThrow(annotatedType.simpleName(), "Domain"));
    childObjectHandleType = getChildObjectHandleType();
    childDomainClassSimpleName = annotatedType.simpleName();
    parentDomainClassSimpleName = context.addToImportAndGetSimpleName(parentDomainType.targetType().canonicalName());

    CustomType actualParentDomainType = buildActualType(parentDomainType.targetType(), roundEnv);
    CustomTypeReference actualParentDomainTypeReference = CustomTypeReferences.get(actualParentDomainType, parentDomainType.typeArguments());
    CustomType effectiveActualParentDomainType = actualParentDomainTypeReference.effectiveTargetType();

    analyzeObjectHandleMethods(effectiveActualParentDomainType, roundEnv);

    Optional<CustomTypeReference> mainEquivalentDomain = DomainFunctions.getAliasBaseDomain(annotatedType);
    isAlias = mainEquivalentDomain.isPresent();
    if (isAlias) {
      primaryDomainSimpleName = context.addToImportAndGetSimpleName(
          mainEquivalentDomain.get().targetType().canonicalName()
      );
      primaryDomainTypeArguments = mainEquivalentDomain.get().typeArgumentsDeclaration(
          context::addToImportAndGetSimpleName
      );
      domainType = buildDomainType(mainEquivalentDomain.get().targetType(), mainEquivalentDomain.get().typeArguments());
    } else {
      domainType = buildDomainType(parentDomainType.targetType(), (List) annotatedType.typeParameters());
    }
    return true;
  }

  @Override
  protected Stream<MethodStatement> getObjectHandleMethods(
      CustomType customType, RoundEnvironment roundEnv
  ) {
    return buildActualType(parentDomainType.targetType(), roundEnv).actualMethods().stream()
        .filter(m -> excludeDeepConversionMethods(m, customType))
        .filter(this::isNotMovingMethod)
        .filter(this::isNotDomainClassGetter);
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
  protected Map<String, String> generateMethod(MethodStatement method, ObjectReferenceForm targetForm, int methodIndex) {
    if (method.hasAnnotation(Channel.class)) {
      return generateMethodNormal(method, targetForm, methodIndex);
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
    return context.addToImportAndGetSimpleName(
        NameConventionFunctions.getUnmovableObjectHandleTypename(annotatedType.className())
    );
  }
}
