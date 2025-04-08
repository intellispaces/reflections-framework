package tech.intellispaces.jaquarius.annotationprocessor.ontology;

import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.jaquarius.annotationprocessor.channel.AbstractChannelGenerator;
import tech.intellispaces.jaquarius.naming.NameConventionFunctions;
import tech.intellispaces.reflection.customtype.CustomType;
import tech.intellispaces.reflection.method.MethodParam;
import tech.intellispaces.reflection.method.MethodStatement;
import tech.intellispaces.reflection.reference.TypeReference;

import java.util.List;

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
