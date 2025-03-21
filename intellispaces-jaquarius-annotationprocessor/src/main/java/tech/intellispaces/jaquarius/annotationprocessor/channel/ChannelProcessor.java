package tech.intellispaces.jaquarius.annotationprocessor.channel;

import com.google.auto.service.AutoService;
import tech.intellispaces.commons.annotation.processor.ArtifactGenerator;
import tech.intellispaces.commons.annotation.processor.ArtifactGeneratorContext;
import tech.intellispaces.commons.annotation.processor.ArtifactProcessor;
import tech.intellispaces.commons.annotation.processor.ArtifactValidator;
import tech.intellispaces.commons.reflection.customtype.CustomType;
import tech.intellispaces.jaquarius.annotation.Channel;
import tech.intellispaces.jaquarius.annotationprocessor.AnnotationFunctions;
import tech.intellispaces.jaquarius.annotationprocessor.JaquariusArtifactProcessor;

import javax.annotation.processing.Processor;
import javax.lang.model.element.ElementKind;
import java.util.List;

@AutoService(Processor.class)
public class ChannelProcessor extends ArtifactProcessor {

  public ChannelProcessor() {
    super(ElementKind.INTERFACE, Channel.class, JaquariusArtifactProcessor.SOURCE_VERSION);
  }

  @Override
  public boolean isApplicable(CustomType channelType) {
    return AnnotationFunctions.isAutoGenerationEnabled(channelType);
  }

  @Override
  public ArtifactValidator validator() {
    return new ChannelValidator();
  }

  @Override
  public List<ArtifactGenerator> makeGenerators(CustomType channelType, ArtifactGeneratorContext context) {
    return ChannelProcessorFunctions.makeGenerators(channelType);
  }
}
