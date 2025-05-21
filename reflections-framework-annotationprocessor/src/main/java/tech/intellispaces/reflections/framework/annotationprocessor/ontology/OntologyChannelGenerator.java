package tech.intellispaces.reflections.framework.annotationprocessor.ontology;

import java.util.List;

import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.javareflection.customtype.CustomType;
import tech.intellispaces.javareflection.method.MethodParam;
import tech.intellispaces.javareflection.method.MethodStatement;
import tech.intellispaces.javareflection.reference.TypeReference;
import tech.intellispaces.reflections.framework.annotationprocessor.channel.AbstractChannelGenerator;
import tech.intellispaces.reflections.framework.naming.NameConventionFunctions;

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
