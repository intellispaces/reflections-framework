package tech.intellispaces.framework.core.annotation.processor.domain;

import com.google.auto.service.AutoService;
import tech.intellispaces.framework.annotationprocessor.generator.ArtifactGenerator;
import tech.intellispaces.framework.annotationprocessor.validator.AnnotatedTypeValidator;
import tech.intellispaces.framework.commons.collection.ArraysFunctions;
import tech.intellispaces.framework.core.annotation.Domain;
import tech.intellispaces.framework.core.annotation.Transition;
import tech.intellispaces.framework.core.annotation.processor.AbstractAnnotationProcessor;
import tech.intellispaces.framework.core.traverse.TraverseTypes;
import tech.intellispaces.framework.core.validation.DomainValidator;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;
import tech.intellispaces.framework.javastatements.statement.method.MethodStatement;
import tech.intellispaces.framework.javastatements.statement.reference.CustomTypeReference;
import tech.intellispaces.framework.javastatements.statement.reference.NonPrimitiveTypeReference;
import tech.intellispaces.framework.javastatements.statement.reference.TypeBoundReference;
import tech.intellispaces.framework.javastatements.statement.reference.TypeReference;

import javax.annotation.processing.Processor;
import javax.lang.model.element.ElementKind;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@AutoService(Processor.class)
public class DomainAnnotationProcessor extends AbstractAnnotationProcessor {

  public DomainAnnotationProcessor() {
    super(Domain.class, Set.of(ElementKind.INTERFACE));
  }

  @Override
  protected boolean isApplicable(CustomType domainType) {
    return isAutoGenerationEnabled(domainType);
  }

  @Override
  protected AnnotatedTypeValidator getValidator() {
    return new DomainValidator();
  }

  @Override
  protected List<ArtifactGenerator> makeArtifactGenerators(CustomType domainType) {
    List<ArtifactGenerator> generators = new ArrayList<>();
    for (MethodStatement method : domainType.declaredMethods()) {
      if (method.hasAnnotation(Transition.class)) {
        if (isAutoGenerationEnabled(domainType, "Transition")) {
          generators.add(new DomainTransitionGenerator(domainType, method));
        }
        if (isEnableMapperGuideGeneration(domainType, method)) {
          generators.add(new DomainGuideGenerator(TraverseTypes.Mapping, domainType, method));
        }
        if (isEnableMoverGuideGeneration(domainType, method)) {
          generators.add(new DomainGuideGenerator(TraverseTypes.Moving, domainType, method));
        }
      }
    }
    addBasicObjectHandleGenerators(domainType, generators);
    addUpgradeObjectHandleGenerators(domainType, domainType, generators);
    addDowngradeObjectHandleGenerators(domainType, generators);
    return generators;
  }

  private boolean isEnableMapperGuideGeneration(CustomType domainType, MethodStatement method) {
    return isAutoGenerationEnabled(domainType, "Mapper") &&
        ArraysFunctions.contains(
            method.selectAnnotation(Transition.class).orElseThrow().allowedTraverseTypes(),
            TraverseTypes.Mapping);
  }

  private boolean isEnableMoverGuideGeneration(CustomType domainType, MethodStatement method) {
    return isAutoGenerationEnabled(domainType, "Mover") &&
        ArraysFunctions.contains(
            method.selectAnnotation(Transition.class).orElseThrow().allowedTraverseTypes(),
            TraverseTypes.Moving);
  }

  private void addBasicObjectHandleGenerators(CustomType domainType, List<ArtifactGenerator> generators) {
    if (isAutoGenerationEnabled(domainType, "ObjectHandle")) {
      generators.add(new CommonObjectHandleGenerator(domainType));
    }
    if (isAutoGenerationEnabled(domainType, "MovableObjectHandle")) {
      generators.add(new MovableObjectHandleGenerator(domainType));
    }
    if (isAutoGenerationEnabled(domainType, "UnmovableObjectHandle")) {
      generators.add(new UnmovableObjectHandleGenerator(domainType));
    }
  }

  private void addUpgradeObjectHandleGenerators(
      CustomType domainType, CustomType curDomainType, List<ArtifactGenerator> generators
  ) {
    List<CustomTypeReference> parents = curDomainType.parentTypes();
    if (parents.size() != 1) {
      return;
    }
    CustomTypeReference baseDomainType = parents.get(0);
    List<NonPrimitiveTypeReference> baseTypeArguments = baseDomainType.typeArguments();
    if (baseTypeArguments.isEmpty()) {
      return;
    }
    boolean allTypeArgumentsAreCustomTypes = baseTypeArguments.stream().allMatch(this::isCustomTypeRelated);
    if (!allTypeArgumentsAreCustomTypes) {
      return;
    }
    generators.add(new UnmovableUpgradeObjectHandleGenerator(domainType, baseDomainType));

    addUpgradeObjectHandleGenerators(domainType, baseDomainType.targetType(), generators);
  }

  private void addDowngradeObjectHandleGenerators(CustomType domainType, List<ArtifactGenerator> generators) {
    List<CustomTypeReference> parents = domainType.parentTypes();
    if (parents.size() != 1) {
      return;
    }
    CustomTypeReference baseDomainType = parents.get(0);

    generators.add(new MovableDowngradeObjectHandleGenerator(domainType, baseDomainType));
  }

  private boolean isCustomTypeRelated(TypeReference type) {
    if (type.isCustomTypeReference()) {
      return true;
    }
    if (type.isNamedTypeReference()) {
      List<TypeBoundReference> extendedBounds = type.asNamedTypeReferenceSurely().extendedBounds();
      if (extendedBounds.size() == 1 && extendedBounds.get(0).isCustomTypeReference()) {
        return true;
      }
    }
    return false;
  }
}
