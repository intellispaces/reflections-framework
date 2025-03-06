package tech.intellispaces.jaquarius.annotationprocessor;

import tech.intellispaces.commons.annotation.processor.ArtifactGeneratorContext;
import tech.intellispaces.commons.annotation.processor.TemplatedJavaArtifactGenerator;
import tech.intellispaces.commons.base.type.ClassFunctions;
import tech.intellispaces.commons.java.reflection.customtype.CustomType;
import tech.intellispaces.commons.java.reflection.customtype.CustomTypes;
import tech.intellispaces.commons.java.reflection.method.MethodParam;
import tech.intellispaces.commons.java.reflection.method.MethodSignatureDeclarations;
import tech.intellispaces.commons.java.reflection.method.MethodStatement;
import tech.intellispaces.commons.java.reflection.reference.TypeReference;
import tech.intellispaces.jaquarius.Jaquarius;
import tech.intellispaces.jaquarius.annotation.Domain;
import tech.intellispaces.jaquarius.naming.NameConventionFunctions;
import tech.intellispaces.jaquarius.object.reference.ObjectHandleFunctions;
import tech.intellispaces.jaquarius.object.reference.ObjectHandleType;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForm;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForms;
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
        .map(c -> NameConventionFunctions.getUndefinedPureObjectTypename(c.canonicalName()))
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

  protected String buildObjectHandleDeclaration(
      TypeReference domainType, ObjectHandleType handleType, boolean replaceKeyDomain
  ) {
    return ObjectHandleFunctions.getObjectHandleDeclaration(
        domainType, handleType, ObjectReferenceForms.Default, replaceKeyDomain, this::addImportAndGetSimpleName
    );
  }

  protected String buildHandleDeclarationDefaultForm(
      TypeReference domainType, ObjectHandleType handleType, boolean replaceKeyDomain,  ObjectReferenceForm handleForm
  ) {
    return ObjectHandleFunctions.getObjectHandleDeclaration(
        domainType, handleType, handleForm, replaceKeyDomain, this::addImportAndGetSimpleName
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
