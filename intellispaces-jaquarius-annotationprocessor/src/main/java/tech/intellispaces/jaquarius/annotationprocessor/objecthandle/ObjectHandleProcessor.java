package tech.intellispaces.jaquarius.annotationprocessor.objecthandle;

import com.google.auto.service.AutoService;
import tech.intellispaces.annotationprocessor.ArtifactGenerator;
import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.annotationprocessor.ArtifactProcessor;
import tech.intellispaces.annotationprocessor.ArtifactValidator;
import tech.intellispaces.jaquarius.annotation.ObjectHandle;
import tech.intellispaces.jaquarius.annotationprocessor.AnnotationProcessorConstants;
import tech.intellispaces.jaquarius.annotationprocessor.AnnotationProcessorFunctions;
import tech.intellispaces.java.reflection.customtype.CustomType;

import javax.annotation.processing.Processor;
import javax.lang.model.element.ElementKind;
import java.util.List;

@AutoService(Processor.class)
public class ObjectHandleProcessor extends ArtifactProcessor {

  public ObjectHandleProcessor() {
    super(ElementKind.CLASS, ObjectHandle.class, AnnotationProcessorConstants.SOURCE_VERSION);
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
    return AnnotationProcessorFunctions.makeObjectHandleArtifactGenerators(objectHandleType);
  }
}
