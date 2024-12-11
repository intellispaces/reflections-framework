package tech.intellispaces.jaquarius.annotationprocessor.domain;

import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.jaquarius.annotationprocessor.AbstractObjectHandleGenerator;
import tech.intellispaces.jaquarius.annotationprocessor.AnnotationProcessorFunctions;
import tech.intellispaces.jaquarius.annotationprocessor.ArtifactTypes;
import tech.intellispaces.jaquarius.object.ObjectHandleFunctions;
import tech.intellispaces.jaquarius.space.domain.DomainFunctions;
import tech.intellispaces.java.reflection.customtype.CustomType;
import tech.intellispaces.java.reflection.customtype.InterfaceType;
import tech.intellispaces.java.reflection.customtype.Interfaces;
import tech.intellispaces.java.reflection.method.MethodStatement;
import tech.intellispaces.java.reflection.reference.CustomTypeReference;
import tech.intellispaces.java.reflection.reference.CustomTypeReferences;
import tech.intellispaces.java.reflection.reference.NamedReference;
import tech.intellispaces.java.reflection.reference.NotPrimitiveReference;
import tech.intellispaces.java.reflection.reference.ReferenceBound;

import javax.annotation.processing.RoundEnvironment;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

abstract class AbstractDomainObjectHandleGenerator extends AbstractObjectHandleGenerator {

  public AbstractDomainObjectHandleGenerator(CustomType domainType) {
    super(domainType);
  }

  protected Stream<MethodStatement> getObjectHandleMethods(
      CustomType customType, RoundEnvironment roundEnv
  ) {
    return customType.actualMethods().stream()
        .filter(m -> DomainFunctions.isDomainType(m.owner()));
  }

  protected String typeConverter(String className) {
    String handleCanonicalName = ObjectHandleFunctions.getObjectHandleTypename(className, getObjectHandleType());
    return addToImportAndGetSimpleName(handleCanonicalName);
  }

  protected List<MethodStatement> getAdditionalOMethods(CustomType customType, RoundEnvironment roundEnv) {
    List<MethodStatement> methods = new ArrayList<>();
    List<CustomType> artifactAddOns = AnnotationProcessorFunctions.findArtifactAddOns(
        customType, ArtifactTypes.ObjectHandle, roundEnv
    );
    for (CustomType artifactAddOn : artifactAddOns) {
      methods.addAll(artifactAddOn.declaredMethods());
    }
    return methods;
  }

  protected CustomType buildActualType(CustomType domain, ArtifactGeneratorContext context) {
    InterfaceType domainInterface = domain.asInterfaceOrElseThrow();

    var builder = Interfaces.build(domainInterface);
    getAdditionalOMethods(domainInterface, context.roundEnvironment()).forEach(builder::addDeclaredMethod);

    var parentInterfaces = new ArrayList<CustomTypeReference>();
    for (CustomTypeReference parent : domainInterface.extendedInterfaces()) {
      parentInterfaces.add(
          CustomTypeReferences.get(buildActualType(parent.targetType(), context), parent.typeArguments())
      );
    }
    builder.extendedInterfaces(parentInterfaces);
    return builder.get();
  }

  protected String buildDomainType(CustomType domainType, List<NotPrimitiveReference> typeQualifiers) {
    StringBuilder sb = new StringBuilder();
    sb.append("Types.get(");
    sb.append(addToImportAndGetSimpleName(domainType.canonicalName())).append(".class");
    for (NotPrimitiveReference typeQualifier : typeQualifiers) {
      sb.append(", ");
      analyzeDomainType(typeQualifier, sb);
    }
    sb.append(");");
    return sb.toString();
  }

  private void analyzeDomainType(NotPrimitiveReference typeReference, StringBuilder sb) {
    if (typeReference.isCustomTypeReference()) {
      CustomTypeReference customTypeReference = typeReference.asCustomTypeReferenceOrElseThrow();
      sb.append("Types.get(");
      sb.append(addToImportAndGetSimpleName(customTypeReference.targetType().canonicalName())).append(".class");
      for (NotPrimitiveReference typeArg : customTypeReference.typeArguments()) {
        sb.append(", ");
        analyzeDomainType(typeArg, sb);
      }
      sb.append(")");
    } else if (typeReference.isNamedReference()) {
      NamedReference namedReference = typeReference.asNamedReferenceOrElseThrow();
      if (namedReference.extendedBounds().isEmpty()) {
        sb.append("Types.get(");
        sb.append(addToImportAndGetSimpleName(Object.class)).append(".class");
        sb.append(")");
      } else {
        ReferenceBound extendedBound = namedReference.extendedBounds().get(0);
        analyzeDomainType(extendedBound, sb);
      }
    }
  }
}
