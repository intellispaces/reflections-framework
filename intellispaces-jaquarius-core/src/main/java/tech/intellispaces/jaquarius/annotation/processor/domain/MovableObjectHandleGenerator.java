package tech.intellispaces.jaquarius.annotation.processor.domain;

import tech.intellispaces.jaquarius.annotation.Movable;
import tech.intellispaces.jaquarius.annotation.ObjectHandle;
import tech.intellispaces.jaquarius.annotation.Unmovable;
import tech.intellispaces.jaquarius.channel.Channel0;
import tech.intellispaces.jaquarius.channel.Channel1;
import tech.intellispaces.jaquarius.channel.ChannelMethod0;
import tech.intellispaces.jaquarius.channel.ChannelMethod1;
import tech.intellispaces.jaquarius.channel.MappingOfMovingChannel;
import tech.intellispaces.jaquarius.common.NameConventionFunctions;
import tech.intellispaces.jaquarius.exception.TraverseException;
import tech.intellispaces.jaquarius.object.MovableObjectHandle;
import tech.intellispaces.jaquarius.object.ObjectHandleTypes;
import tech.intellispaces.jaquarius.space.channel.ChannelFunctions;
import tech.intellispaces.jaquarius.space.domain.DomainFunctions;
import tech.intellispaces.jaquarius.traverse.TraverseType;
import tech.intellispaces.java.annotation.context.AnnotationProcessingContext;
import tech.intellispaces.java.reflection.customtype.CustomType;
import tech.intellispaces.java.reflection.method.MethodStatement;
import tech.intellispaces.java.reflection.reference.CustomTypeReference;
import tech.intellispaces.java.reflection.reference.TypeReference;

import javax.annotation.processing.RoundEnvironment;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class MovableObjectHandleGenerator extends AbstractDomainObjectHandleGenerator {
  private String baseObjectHandle;
  private boolean isAlias;
  private String primaryDomainSimpleName;
  private String primaryDomainTypeArguments;
  private String primaryObjectHandle;

  public MovableObjectHandleGenerator(CustomType initiatorType, CustomType domainType) {
    super(initiatorType, domainType);
  }

  @Override
  public boolean isRelevant(AnnotationProcessingContext context) {
    return true;
  }

  @Override
  protected ObjectHandleTypes getObjectHandleType() {
    return ObjectHandleTypes.Movable;
  }

  @Override
  public String artifactName() {
    return NameConventionFunctions.getMovableObjectHandleTypename(annotatedType.className());
  }

  @Override
  protected String templateName() {
    return "/movable_object_handle.template";
  }

  protected Map<String, Object> templateVariables() {
    Map<String, Object> vars = new HashMap<>();
    vars.put("generatedAnnotation", makeGeneratedAnnotation());
    vars.put("packageName", context.packageName());
    vars.put("importedClasses", context.getImports());
    vars.put("sourceClassName", sourceClassCanonicalName());
    vars.put("sourceClassSimpleName", sourceClassSimpleName());
    vars.put("classSimpleName", context.generatedClassSimpleName());
    vars.put("domainTypeParamsFull", domainTypeParamsFull);
    vars.put("domainTypeParamsBrief", domainTypeParamsBrief);
    vars.put("baseObjectHandle", baseObjectHandle);
    vars.put("conversionMethods", conversionMethods);
    vars.put("domainMethods", methods);
    vars.put("movableObjectHandleName", context.addToImportAndGetSimpleName(MovableObjectHandle.class));
    vars.put("isAlias", isAlias);
    vars.put("primaryObjectHandle", primaryObjectHandle);
    vars.put("primaryDomainSimpleName", primaryDomainSimpleName);
    vars.put("primaryDomainTypeArguments", primaryDomainTypeArguments);
    return vars;
  }

  @Override
  protected boolean analyzeAnnotatedType(RoundEnvironment roundEnv) {
    context.generatedClassCanonicalName(artifactName());
    if (annotatedType.isNested()) {
      context.addImport(sourceClassCanonicalName());
    }
    context.addImport(MovableObjectHandle.class);
    context.addImport(ObjectHandle.class);
    context.addImport(Channel0.class);
    context.addImport(Channel1.class);
    context.addImport(ChannelMethod0.class);
    context.addImport(ChannelMethod1.class);
    context.addImport(TraverseException.class);
    context.addImport(Movable.class);
    context.addImport(MappingOfMovingChannel.class);

    domainTypeParamsFull = annotatedType.typeParametersFullDeclaration();
    domainTypeParamsBrief = annotatedType.typeParametersBriefDeclaration();
    baseObjectHandle = context.addToImportAndGetSimpleName(
        NameConventionFunctions.getCommonObjectHandleTypename(annotatedType.className())
    );
    analyzeObjectHandleMethods(annotatedType, roundEnv);
    analyzeConversionMethods(annotatedType, roundEnv);

    List<CustomTypeReference> equivalentDomains = DomainFunctions.getEquivalentDomains(annotatedType);
    isAlias = !equivalentDomains.isEmpty();
    if (isAlias) {
      CustomTypeReference nearEquivalentDomain = equivalentDomains.get(0);
      CustomTypeReference mainEquivalentDomain = equivalentDomains.get(equivalentDomains.size() - 1);

      primaryObjectHandle = getObjectHandleDeclaration(nearEquivalentDomain, ObjectHandleTypes.Movable);
      primaryDomainSimpleName = context.addToImportAndGetSimpleName(mainEquivalentDomain.targetType().canonicalName());
      primaryDomainTypeArguments = nearEquivalentDomain.typeArgumentsDeclaration(context::addToImportAndGetSimpleName);
    }
    return true;
  }

  @Override
  protected Stream<MethodStatement> getObjectHandleMethods(CustomType customType, RoundEnvironment roundEnv) {
    return super.getObjectHandleMethods(customType, roundEnv)
        .filter(m -> ChannelFunctions.getTraverseTypes(m).stream().anyMatch(TraverseType::isMovingBased));
  }

  @Override
  protected void appendMethodReturnHandleType(StringBuilder sb, MethodStatement method) {
    TypeReference domainReturnType = method.returnType().orElseThrow();
    if (
        ChannelFunctions.getTraverseTypes(method).stream().anyMatch(TraverseType::isMoving)
            || method.hasAnnotation(Movable.class)
    ) {
      sb.append(getObjectHandleDeclaration(domainReturnType, ObjectHandleTypes.Movable));
    } else if (method.hasAnnotation(Unmovable.class)) {
      sb.append(getObjectHandleDeclaration(domainReturnType, ObjectHandleTypes.Unmovable));
    } else {
      sb.append(getObjectHandleDeclaration(domainReturnType, ObjectHandleTypes.Common));
    }
  }
}
