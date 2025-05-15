package tech.intellispaces.reflections.framework.annotationprocessor.reflection;

import java.util.List;
import javax.annotation.processing.Processor;
import javax.lang.model.element.ElementKind;

import com.google.auto.service.AutoService;

import tech.intellispaces.annotationprocessor.ArtifactGenerator;
import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.annotationprocessor.ArtifactProcessor;
import tech.intellispaces.annotationprocessor.ArtifactValidator;
import tech.intellispaces.reflections.framework.annotation.Reflection;
import tech.intellispaces.reflections.framework.annotationprocessor.AnnotationFunctions;
import tech.intellispaces.reflections.framework.annotationprocessor.ReflectionsArtifactProcessor;
import tech.intellispaces.jstatements.customtype.CustomType;

@AutoService(Processor.class)
public class ReflectionProcessor extends ArtifactProcessor {

  public ReflectionProcessor() {
    super(ElementKind.CLASS, Reflection.class, ReflectionsArtifactProcessor.SOURCE_VERSION);
  }

  @Override
  public boolean isApplicable(CustomType objectHandleType) {
    return objectHandleType.isAbstract() && AnnotationFunctions.isAutoGenerationEnabled(objectHandleType);
  }

  @Override
  public ArtifactValidator validator() {
    return null;
  }

  @Override
  public List<ArtifactGenerator> makeGenerators(CustomType objectHandleType, ArtifactGeneratorContext context) {
    return ReflectionProcessorFunctions.makeReflectionArtifactGenerators(objectHandleType);
  }
}
