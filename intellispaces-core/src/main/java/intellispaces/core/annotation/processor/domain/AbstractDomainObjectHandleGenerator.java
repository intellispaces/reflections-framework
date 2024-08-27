package intellispaces.core.annotation.processor.domain;

import intellispaces.core.annotation.processor.AbstractObjectHandleGenerator;
import intellispaces.core.annotation.processor.AnnotationProcessorFunctions;
import intellispaces.core.annotation.processor.ArtifactTypes;
import intellispaces.core.object.ObjectFunctions;
import intellispaces.core.space.domain.DomainFunctions;
import intellispaces.javastatements.customtype.CustomType;
import intellispaces.javastatements.customtype.InterfaceType;
import intellispaces.javastatements.customtype.Interfaces;
import intellispaces.javastatements.method.MethodStatement;
import intellispaces.javastatements.reference.CustomTypeReference;
import intellispaces.javastatements.reference.CustomTypeReferences;

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
    List<CustomType> artifactAnnexes = AnnotationProcessorFunctions.findArtifactAnnexes(
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
