package tech.intellispaces.reflections.framework.annotationprocessor.domain;

import java.util.List;

import tech.intellispaces.actions.runnable.RunnableAction;
import tech.intellispaces.actions.text.StringActions;
import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.javareflection.customtype.CustomType;
import tech.intellispaces.javareflection.method.MethodParam;
import tech.intellispaces.javareflection.method.MethodStatement;
import tech.intellispaces.javareflection.reference.NamedReference;
import tech.intellispaces.javareflection.reference.TypeReference;
import tech.intellispaces.reflections.framework.annotationprocessor.channel.AbstractChannelGenerator;
import tech.intellispaces.reflections.framework.naming.NameConventionFunctions;

public class DomainChannelGenerator extends AbstractChannelGenerator {

  public DomainChannelGenerator(CustomType domainType, MethodStatement channelMethod) {
    super(domainType, channelMethod);
  }

  @Override
  public boolean isRelevant(ArtifactGeneratorContext context) {
    return true;
  }

  @Override
  protected String getChannelClassCanonicalName() {
    return NameConventionFunctions.getChannelClassCanonicalName(
        sourceArtifact().packageName(), sourceArtifact(), channelMethod
    );
  }

  @Override
  protected String buildChannelMethodSignature() {
    return buildMethodSignatureIncludedOwnerTypeParams(channelMethod, List.of(getSourceParamDeclaration()));
  }

  private String getSourceParamDeclaration() {
    var sb = new StringBuilder();
    sb.append(addImportAndGetSimpleName(sourceArtifact().canonicalName()));
    if (!sourceArtifact().typeParameters().isEmpty()) {
      sb.append("<");
      RunnableAction commaAppender = StringActions.skipFirstTimeCommaAppender(sb);
      for (NamedReference typeParam : sourceArtifact().typeParameters()) {
        commaAppender.run();
        sb.append(typeParam.formalBriefDeclaration());
      }
      sb.append(">");
    }
    sb.append(" source");
    return sb.toString();
  }

  @Override
  protected List<TypeReference> getQualifierTypes() {
    return channelMethod.params().stream()
        .map(MethodParam::type)
        .toList();
  }
}
