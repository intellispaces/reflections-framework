package tech.intellispaces.jaquarius.annotation.processor.ontology;

import tech.intellispaces.jaquarius.annotation.processor.AbstractChannelGenerator;
import tech.intellispaces.jaquarius.common.NameConventionFunctions;
import tech.intellispaces.java.annotation.context.AnnotationProcessingContext;
import tech.intellispaces.java.reflection.customtype.CustomType;
import tech.intellispaces.java.reflection.method.MethodStatement;
import tech.intellispaces.java.reflection.reference.TypeReference;
import tech.intellispaces.java.reflection.method.MethodParam;

import java.util.List;

public class OntologyChannelGenerator extends AbstractChannelGenerator {

  public OntologyChannelGenerator(CustomType initiatorType, CustomType ontologyType, MethodStatement channelMethod) {
    super(initiatorType, ontologyType, channelMethod);
  }

  @Override
  public boolean isRelevant(AnnotationProcessingContext context) {
    return true;
  }

  @Override
  protected String getChannelMethodSignature() {
    return buildMethodSignature(
        channelMethod,
        channelMethod.name(),
        true,
        true,
        List.of()
    );
  }

  @Override
  protected String getChannelClassCanonicalName() {
    return NameConventionFunctions.getChannelClassCanonicalName(channelMethod);
  }

  @Override
  protected List<TypeReference> getQualifierTypes() {
    return channelMethod.params().subList(1, channelMethod.params().size()).stream()
        .map(MethodParam::type)
        .toList();
  }
}
