package tech.intellispaces.jaquarius.annotationprocessor.ontology;

import tech.intellispaces.commons.annotation.processor.ArtifactGeneratorContext;
import tech.intellispaces.commons.java.reflection.customtype.CustomType;
import tech.intellispaces.commons.java.reflection.method.MethodParam;
import tech.intellispaces.commons.java.reflection.method.MethodStatement;
import tech.intellispaces.commons.java.reflection.reference.TypeReference;
import tech.intellispaces.jaquarius.annotationprocessor.ChannelGenerator;
import tech.intellispaces.jaquarius.naming.NameConventionFunctions;

import java.util.List;

public class OntologyChannelGenerator extends ChannelGenerator {

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
