package tech.intellispaces.reflectionsj.annotationprocessor.ontology;

import java.util.List;

import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.reflectionsj.annotationprocessor.channel.AbstractChannelGenerator;
import tech.intellispaces.reflectionsj.naming.NameConventionFunctions;
import tech.intellispaces.statementsj.customtype.CustomType;
import tech.intellispaces.statementsj.method.MethodParam;
import tech.intellispaces.statementsj.method.MethodStatement;
import tech.intellispaces.statementsj.reference.TypeReference;

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
