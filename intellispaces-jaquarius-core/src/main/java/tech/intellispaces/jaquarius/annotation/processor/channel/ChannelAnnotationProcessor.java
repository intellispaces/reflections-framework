package tech.intellispaces.jaquarius.annotation.processor.channel;

import com.google.auto.service.AutoService;
import tech.intellispaces.jaquarius.annotation.Channel;
import tech.intellispaces.jaquarius.annotation.processor.AnnotationProcessorFunctions;
import tech.intellispaces.jaquarius.validation.ChannelValidator;
import tech.intellispaces.java.annotation.AnnotatedTypeProcessor;
import tech.intellispaces.java.annotation.generator.Generator;
import tech.intellispaces.java.annotation.validator.AnnotatedTypeValidator;
import tech.intellispaces.java.reflection.customtype.CustomType;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import java.util.List;
import java.util.Set;

@AutoService(Processor.class)
public class ChannelAnnotationProcessor extends AnnotatedTypeProcessor {

  public ChannelAnnotationProcessor() {
    super(Channel.class, Set.of(ElementKind.INTERFACE));
  }

  @Override
  public boolean isApplicable(CustomType domainType) {
    return AnnotationProcessorFunctions.isAutoGenerationEnabled(domainType);
  }

  @Override
  public AnnotatedTypeValidator getValidator() {
    return new ChannelValidator();
  }

  @Override
  public List<Generator> makeGenerators(CustomType initiatorType, CustomType domainType, RoundEnvironment roundEnv) {
    return AnnotationProcessorFunctions.makeChannelArtifactGenerators(initiatorType, domainType, roundEnv);
  }
}
