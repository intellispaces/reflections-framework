package tech.intellispaces.core.annotation.processor.domain;

import tech.intellispaces.core.annotation.processor.AbstractObjectHandleGenerator;
import tech.intellispaces.core.annotation.processor.AnnotationFunctions;
import tech.intellispaces.core.annotation.processor.ArtifactTypes;
import tech.intellispaces.core.object.ObjectFunctions;
import tech.intellispaces.core.space.domain.DomainFunctions;
import tech.intellispaces.javastatements.customtype.CustomType;
import tech.intellispaces.javastatements.customtype.InterfaceType;
import tech.intellispaces.javastatements.customtype.Interfaces;
import tech.intellispaces.javastatements.instance.AnnotationInstance;
import tech.intellispaces.javastatements.method.MethodStatement;
import tech.intellispaces.javastatements.reference.CustomTypeReference;
import tech.intellispaces.javastatements.reference.CustomTypeReferences;

import javax.annotation.processing.RoundEnvironment;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    List<AnnotationInstance> preprocessingAnnotations = AnnotationFunctions.findPreprocessingAnnotations(
        customType, ArtifactTypes.ObjectHandle, roundEnv
    );
    for (AnnotationInstance preprocessingAnnotation : preprocessingAnnotations) {
      Optional<CustomType> extensionType = AnnotationFunctions.getPreprocessingExtendWith(
          preprocessingAnnotation
      );
      if (extensionType.isPresent()) {
        methods.addAll(extensionType.orElseThrow().declaredMethods());
      }
    }
    return methods;
  }

  protected CustomType buildActualType(CustomType domain, RoundEnvironment roundEnv) {
    InterfaceType anInterface = domain.asInterfaceOrElseThrow();
    var builder = Interfaces.build(anInterface);
    getAdditionalOMethods(anInterface, roundEnv).forEach(builder::addDeclaredMethod);

    var parentInterfaces = new ArrayList<CustomTypeReference>();
    for (CustomTypeReference parent : anInterface.extendedInterfaces()) {
      parentInterfaces.add(
          CustomTypeReferences.get(buildActualType(parent.targetType(), roundEnv), parent.typeArguments())
      );
    }
    builder.setExtendedInterfaces(parentInterfaces);
    return builder.get();
  }
}
