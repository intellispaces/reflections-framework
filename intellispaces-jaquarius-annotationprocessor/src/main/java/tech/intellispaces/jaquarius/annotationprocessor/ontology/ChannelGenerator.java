package tech.intellispaces.jaquarius.annotationprocessor.ontology;

import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.jaquarius.annotationprocessor.AbstractChannelArtifactGenerator;
import tech.intellispaces.jaquarius.naming.NameConventionFunctions;
import tech.intellispaces.java.reflection.customtype.CustomType;
import tech.intellispaces.java.reflection.method.MethodParam;
import tech.intellispaces.java.reflection.method.MethodStatement;
import tech.intellispaces.java.reflection.reference.TypeReference;

import java.util.List;

public class ChannelGenerator extends AbstractChannelArtifactGenerator {

  public ChannelGenerator(CustomType ontologyType, MethodStatement channelMethod) {
    super(ontologyType, channelMethod);
  }

  @Override
  public boolean isRelevant(ArtifactGeneratorContext context) {
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
