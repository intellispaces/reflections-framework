package intellispaces.framework.core.annotation.processor.channel;

import com.google.auto.service.AutoService;
import intellispaces.common.annotationprocessor.AnnotatedTypeProcessor;
import intellispaces.common.annotationprocessor.generator.Generator;
import intellispaces.common.annotationprocessor.validator.AnnotatedTypeValidator;
import intellispaces.common.javastatement.customtype.CustomType;
import intellispaces.framework.core.annotation.Channel;
import intellispaces.framework.core.annotation.processor.AnnotationProcessorFunctions;
import intellispaces.framework.core.validation.ChannelValidator;

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
