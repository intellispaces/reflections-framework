package tech.intellispaces.jaquarius.annotationprocessor;

import tech.intellispaces.commons.annotation.processor.ArtifactGeneratorContext;
import tech.intellispaces.commons.annotation.processor.TemplatedJavaArtifactGenerator;
import tech.intellispaces.commons.reflection.customtype.CustomType;
import tech.intellispaces.commons.reflection.customtype.CustomTypes;
import tech.intellispaces.commons.reflection.method.MethodParam;
import tech.intellispaces.commons.reflection.method.MethodSignatureDeclarations;
import tech.intellispaces.commons.reflection.method.MethodStatement;
import tech.intellispaces.commons.reflection.reference.TypeReference;
import tech.intellispaces.commons.type.ClassFunctions;
import tech.intellispaces.jaquarius.Jaquarius;
import tech.intellispaces.jaquarius.annotation.Domain;
import tech.intellispaces.jaquarius.naming.NameConventionFunctions;
import tech.intellispaces.jaquarius.object.reference.MovabilityType;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForm;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceFunctions;
import tech.intellispaces.jaquarius.settings.KeyDomainPurposes;

import javax.lang.model.element.TypeElement;
import java.util.List;
import java.util.stream.Collectors;

public abstract class JaquariusArtifactGenerator extends TemplatedJavaArtifactGenerator {

  public JaquariusArtifactGenerator(CustomType sourceArtifact) {
    super(sourceArtifact);
  }

  protected void addHiddenImports(ArtifactGeneratorContext context) {
    addHiddenImport(Jaquarius.settings().getKeyDomainByPurpose(KeyDomainPurposes.Point).domainName());
    addHiddenImport(Jaquarius.settings().getKeyDomainByPurpose(KeyDomainPurposes.Boolean).domainName());
    addHiddenImport(Jaquarius.settings().getKeyDomainByPurpose(KeyDomainPurposes.String).domainName());
    addHiddenImport(Jaquarius.settings().getKeyDomainByPurpose(KeyDomainPurposes.Number).domainName());
    addHiddenImport(Jaquarius.settings().getKeyDomainByPurpose(KeyDomainPurposes.Byte).domainName());
    addHiddenImport(Jaquarius.settings().getKeyDomainByPurpose(KeyDomainPurposes.Short).domainName());
    addHiddenImport(Jaquarius.settings().getKeyDomainByPurpose(KeyDomainPurposes.Integer).domainName());
    addHiddenImport(Jaquarius.settings().getKeyDomainByPurpose(KeyDomainPurposes.Long).domainName());
    addHiddenImport(Jaquarius.settings().getKeyDomainByPurpose(KeyDomainPurposes.Float).domainName());
    addHiddenImport(Jaquarius.settings().getKeyDomainByPurpose(KeyDomainPurposes.Double).domainName());

    context.roundEnvironment().getElementsAnnotatedWith(Domain.class).stream()
        .map(e -> CustomTypes.of((TypeElement) e))
        .map(c -> NameConventionFunctions.getUndefinedPlainObjectTypename(c.canonicalName()))
        .forEach(this::addHiddenImport);
  }

  protected String buildMethodSignature(MethodStatement method) {
    return buildMethodSignature(method, method.name(), false, List.of());
  }

  protected String buildMethodSignature(MethodStatement method, String newMethodName) {
    return buildMethodSignature(method, newMethodName, false, List.of());
  }

  protected String buildMethodSignatureIncludedOwnerTypeParams(MethodStatement method) {
    return buildMethodSignature(method, method.name(), true, List.of());
  }

  protected String buildMethodSignatureIncludedOwnerTypeParams(MethodStatement method, List<String> additionalParams) {
    return buildMethodSignature(method, method.name(), true, additionalParams);
  }

  private String buildMethodSignature(
      MethodStatement method,
      String methodName,
      boolean includeOwnerTypeParams,
      List<String> additionalParams
  ) {
    return MethodSignatureDeclarations.build(method)
        .methodName(methodName)
        .includeMethodTypeParams(true)
        .includeOwnerTypeParams(includeOwnerTypeParams)
        .addAdditionalParams(additionalParams)
        .get(this::addImport, this::simpleNameOf);
  }

  protected String buildObjectFormDeclaration(
      TypeReference domainType, ObjectReferenceForm objectForm, MovabilityType movabilityType, boolean replaceKeyDomain
  ) {
    return ObjectReferenceFunctions.getObjectFormDeclaration(
        domainType, objectForm, movabilityType, replaceKeyDomain, this::addImportAndGetSimpleName
    );
  }

  protected String buildGeneratedMethodJavadoc(String sourceClassCanonicalName, MethodStatement method) {
    return """
      /**
       * Based on the method {@link %s#%s(%s)}
       */
      """.formatted(
        addImportAndGetSimpleName(sourceClassCanonicalName),
        method.name(),
        method.params().stream()
            .map(MethodParam::type)
            .map(type -> type.actualDeclaration(this::addImportAndGetSimpleName))
            .collect(Collectors.joining(", "))
    );
  }

  protected String buildDomainLink(TypeReference type) {
    if (type.isPrimitiveReference()) {
      return "{@link " +
          ClassFunctions.wrapperClassOfPrimitive(type.asPrimitiveReferenceOrElseThrow().typename()).getSimpleName() +
          "}";
    } else if (type.isCustomTypeReference()) {
      return "{@link " +
          addImportAndGetSimpleName(type.asCustomTypeReferenceOrElseThrow().targetType().canonicalName()) + "}";
    } else {
      return "Object";
    }
  }
}
