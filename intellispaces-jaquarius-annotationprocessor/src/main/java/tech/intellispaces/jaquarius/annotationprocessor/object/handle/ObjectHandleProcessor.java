package tech.intellispaces.jaquarius.annotationprocessor.object.handle;

import com.google.auto.service.AutoService;
import tech.intellispaces.annotationprocessor.ArtifactGenerator;
import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.annotationprocessor.ArtifactProcessor;
import tech.intellispaces.annotationprocessor.ArtifactValidator;
import tech.intellispaces.jaquarius.annotation.ObjectHandle;
import tech.intellispaces.jaquarius.annotationprocessor.AnnotationFunctions;
import tech.intellispaces.jaquarius.annotationprocessor.JaquariusArtifactProcessor;
import tech.intellispaces.reflection.customtype.CustomType;

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
    return objectHandleType.isAbstract() && AnnotationFunctions.isAutoGenerationEnabled(objectHandleType);
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
