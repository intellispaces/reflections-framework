package intellispaces.framework.core.annotation.processor.ontology;

import intellispaces.common.annotationprocessor.context.AnnotationProcessingContext;
import intellispaces.common.javastatement.customtype.CustomType;
import intellispaces.common.javastatement.method.MethodParam;
import intellispaces.common.javastatement.method.MethodStatement;
import intellispaces.common.javastatement.reference.TypeReference;
import intellispaces.framework.core.annotation.processor.AbstractChannelGenerator;
import intellispaces.framework.core.common.NameConventionFunctions;

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
