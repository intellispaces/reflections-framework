package tech.intellispaces.reflections.annotationprocessor.ontology;

import java.util.List;

import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.reflections.annotationprocessor.channel.AbstractChannelGenerator;
import tech.intellispaces.reflections.naming.NameConventionFunctions;
import tech.intellispaces.jstatements.customtype.CustomType;
import tech.intellispaces.jstatements.method.MethodParam;
import tech.intellispaces.jstatements.method.MethodStatement;
import tech.intellispaces.jstatements.reference.TypeReference;

public class OntologyChannelGenerator extends AbstractChannelGenerator {

  public OntologyChannelGenerator(
      CustomType ontologyType, MethodStatement channelMethod
  ) {
    super(ontologyType, channelMethod);
  }

  @Override
  public boolean isRelevant(ArtifactGeneratorContext context) {
    return true;
  }

  @Override
  protected String buildChannelMethodSignature() {
    return buildMethodSignatureIncludedOwnerTypeParams(channelMethod);
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
