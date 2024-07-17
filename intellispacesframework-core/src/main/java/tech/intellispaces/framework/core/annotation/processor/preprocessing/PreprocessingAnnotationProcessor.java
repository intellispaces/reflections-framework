package tech.intellispaces.framework.core.annotation.processor.preprocessing;

import com.google.auto.service.AutoService;
import tech.intellispaces.framework.annotationprocessor.AnnotatedTypeValidator;
import tech.intellispaces.framework.annotationprocessor.generator.ArtifactGenerator;
import tech.intellispaces.framework.core.annotation.Configuration;
import tech.intellispaces.framework.core.annotation.Guide;
import tech.intellispaces.framework.core.annotation.Module;
import tech.intellispaces.framework.core.annotation.Preprocessing;
import tech.intellispaces.framework.core.annotation.processor.AbstractAnnotationProcessor;
import tech.intellispaces.framework.core.annotation.processor.module.UnitWrapperGenerator;
import tech.intellispaces.framework.core.system.ModuleFunctions;
import tech.intellispaces.framework.javastatements.JavaStatements;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;

import javax.annotation.processing.Processor;
import javax.lang.model.element.ElementKind;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@AutoService(Processor.class)
public class PreprocessingAnnotationProcessor extends AbstractAnnotationProcessor {

  public PreprocessingAnnotationProcessor() {
    super(Preprocessing.class, Set.of(ElementKind.CLASS, ElementKind.INTERFACE));
  }

  @Override
  protected boolean isApplicable(CustomType customType) {
    return isAutoGenerationEnabled(customType);
  }

  @Override
  protected AnnotatedTypeValidator getValidator() {
    return null;
  }

  @Override
  protected List<ArtifactGenerator> makeArtifactGenerators(CustomType customType) {
    Preprocessing annotation = customType.selectAnnotation(Preprocessing.class).orElseThrow();
    if (annotation.value().length == 0) {
      return List.of();
    }

    List<ArtifactGenerator> generators = new ArrayList<>();
    for (Class<?> preprocessingClass : annotation.value()) {
      if (preprocessingClass.isAnnotationPresent(Module.class)) {
        CustomType moduleCustomType = JavaStatements.customTypeStatement(preprocessingClass);
        generators.add(new UnitWrapperGenerator(moduleCustomType));
        Iterable<CustomType> includedUnits = ModuleFunctions.getIncludedUnits(moduleCustomType);
        includedUnits.forEach(u -> generators.add(new UnitWrapperGenerator(u)));
      } else if (
          preprocessingClass.isAnnotationPresent(Configuration.class) || preprocessingClass.isAnnotationPresent(Guide.class)
      ) {
        CustomType unitCustomType = JavaStatements.customTypeStatement(preprocessingClass);
        generators.add(new UnitWrapperGenerator(unitCustomType));
      }
    }
    return generators;
  }
}
