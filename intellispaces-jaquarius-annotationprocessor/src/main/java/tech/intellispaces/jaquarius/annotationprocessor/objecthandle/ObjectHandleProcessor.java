package tech.intellispaces.jaquarius.annotationprocessor.objecthandle;

import com.google.auto.service.AutoService;
import tech.intellispaces.commons.annotation.processor.ArtifactGenerator;
import tech.intellispaces.commons.annotation.processor.ArtifactGeneratorContext;
import tech.intellispaces.commons.annotation.processor.ArtifactProcessor;
import tech.intellispaces.commons.annotation.processor.ArtifactValidator;
import tech.intellispaces.commons.java.reflection.customtype.CustomType;
import tech.intellispaces.jaquarius.annotation.ObjectHandle;
import tech.intellispaces.jaquarius.annotationprocessor.AnnotationProcessorFunctions;
import tech.intellispaces.jaquarius.annotationprocessor.JaquariusArtifactProcessor;

import javax.annotation.processing.Processor;
import javax.lang.model.element.ElementKind;
import java.util.List;

@AutoService(Processor.class)
public class ObjectHandleProcessor extends ArtifactProcessor {

  public ObjectHandleProcessor() {
    super(ElementKind.CLASS, ObjectHandle.class, JaquariusArtifactProcessor.SOURCE_VERSION);
  }

  @Override
  public boolean isApplicable(CustomType objectHandleType) {
    return objectHandleType.isAbstract() && AnnotationProcessorFunctions.isAutoGenerationEnabled(objectHandleType);
  }

  @Override
  public ArtifactValidator validator() {
    return null;
  }

  @Override
  public List<ArtifactGenerator> makeGenerators(CustomType objectHandleType, ArtifactGeneratorContext context) {
    return ObjectHandleProcessorFunctions.makeObjectHandleArtifactGenerators(objectHandleType);
  }
}
