package tech.intellispaces.reflections.framework.annotationprocessor.guide;

import java.util.List;
import java.util.Set;
import javax.annotation.processing.Processor;
import javax.lang.model.element.ElementKind;

import com.google.auto.service.AutoService;

import tech.intellispaces.annotationprocessor.ArtifactGenerator;
import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.annotationprocessor.ArtifactProcessor;
import tech.intellispaces.annotationprocessor.ArtifactValidator;
import tech.intellispaces.javareflection.customtype.CustomType;
import tech.intellispaces.reflections.framework.annotation.Guide;
import tech.intellispaces.reflections.framework.annotationprocessor.AnnotationFunctions;
import tech.intellispaces.reflections.framework.annotationprocessor.ReflectionsArtifactProcessor;
import tech.intellispaces.reflections.framework.annotationprocessor.module.UnitWrapperGenerator;

@AutoService(Processor.class)
public class GuideProcessor extends ArtifactProcessor {

  public GuideProcessor() {
    super(Set.of(ElementKind.INTERFACE, ElementKind.CLASS), Guide.class, ReflectionsArtifactProcessor.SOURCE_VERSION);
  }

  @Override
  public boolean isApplicable(CustomType moduleType) {
    return AnnotationFunctions.isAutoGenerationEnabled(moduleType);
  }

  @Override
  public ArtifactValidator validator() {
    return null;
  }

  @Override
  public List<ArtifactGenerator> makeGenerators(CustomType guideType, ArtifactGeneratorContext context) {
    if (guideType.asInterface().isPresent()) {
      return List.of(new AutoGuideGenerator(guideType));
    } else {
      return List.of(new UnitWrapperGenerator(guideType));
    }
  }
}
