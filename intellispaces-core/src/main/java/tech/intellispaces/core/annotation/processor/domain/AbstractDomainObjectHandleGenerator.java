package tech.intellispaces.core.annotation.processor.domain;

import tech.intellispaces.core.annotation.processor.AbstractObjectHandleGenerator;
import tech.intellispaces.core.annotation.processor.AnnotationFunctions;
import tech.intellispaces.core.annotation.processor.ArtifactTypes;
import tech.intellispaces.core.object.ObjectFunctions;
import tech.intellispaces.core.space.domain.DomainFunctions;
import tech.intellispaces.javastatements.customtype.CustomType;
import tech.intellispaces.javastatements.customtype.InterfaceType;
import tech.intellispaces.javastatements.customtype.Interfaces;
import tech.intellispaces.javastatements.method.MethodStatement;
import tech.intellispaces.javastatements.reference.CustomTypeReference;
import tech.intellispaces.javastatements.reference.CustomTypeReferences;

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
    String handleCanonicalName = ObjectFunctions.getObjectHandleTypename(className, getObjectHandleType());
    return context.addToImportAndGetSimpleName(handleCanonicalName);
  }

  protected List<MethodStatement> getAdditionalOMethods(CustomType customType, RoundEnvironment roundEnv) {
    List<MethodStatement> methods = new ArrayList<>();
    List<CustomType> artifactAnnexes = AnnotationFunctions.findArtifactAnnexes(
        customType, ArtifactTypes.ObjectHandle, roundEnv
    );
    for (CustomType artifactAnnex : artifactAnnexes) {
      methods.addAll(artifactAnnex.declaredMethods());
    }
    return methods;
  }

  protected CustomType buildActualType(CustomType domain, RoundEnvironment roundEnv) {
    InterfaceType domainInterface = domain.asInterfaceOrElseThrow();

    var builder = Interfaces.build(domainInterface);
    getAdditionalOMethods(domainInterface, roundEnv).forEach(builder::addDeclaredMethod);

    var parentInterfaces = new ArrayList<CustomTypeReference>();
    for (CustomTypeReference parent : domainInterface.extendedInterfaces()) {
      parentInterfaces.add(
          CustomTypeReferences.get(buildActualType(parent.targetType(), roundEnv), parent.typeArguments())
      );
    }
    builder.extendedInterfaces(parentInterfaces);
    return builder.get();
  }
}
