package tech.intellispaces.jaquarius.annotationprocessor;

import tech.intellispaces.action.runnable.RunnableAction;
import tech.intellispaces.action.text.StringActions;
import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.jaquarius.annotation.Channel;
import tech.intellispaces.jaquarius.channel.MappingChannel;
import tech.intellispaces.jaquarius.channel.MappingOfMovingChannel;
import tech.intellispaces.jaquarius.channel.MovingChannel;
import tech.intellispaces.jaquarius.naming.NameConventionFunctions;
import tech.intellispaces.jaquarius.space.channel.ChannelFunctions;
import tech.intellispaces.jaquarius.traverse.TraverseType;
import tech.intellispaces.java.reflection.customtype.CustomType;
import tech.intellispaces.java.reflection.method.MethodStatement;
import tech.intellispaces.java.reflection.reference.TypeReference;

import java.util.List;

public abstract class ChannelGenerator extends JaquariusArtifactGenerator {
  protected final MethodStatement channelMethod;

  public ChannelGenerator(CustomType annotatedType, MethodStatement channelMethod) {
    super(annotatedType);
    this.channelMethod = channelMethod;
  }

  protected abstract String getChannelClassCanonicalName();

  protected abstract List<TypeReference> getQualifierTypes();

  protected abstract String buildChannelMethodSignature();

  @Override
  public String generatedArtifactName() {
    return getChannelClassCanonicalName();
  }

  @Override
  protected String templateName() {
    return "/channel.template";
  }

  @Override
  protected boolean analyzeSourceArtifact(ArtifactGeneratorContext context) {
    if (sourceArtifact().isNested()) {
      addImport(sourceArtifactName());
    }
    addImport(Channel.class);

    addVariable("targetClassLink", buildDomainLink(channelMethod.returnType().orElseThrow()));
    addVariable("channelMethod", buildChannelMethodSignature());
    addVariable("channelClasses", buildChannelClassesDeclaration());
    addVariable("channelMethodName", channelMethod.name());
    addVariable("cid", getCid());
    return true;
  }

  private String buildChannelClassesDeclaration() {
    var sb = new StringBuilder();
    List<MethodStatement> superChannels = channelMethod.overrideMethods();
    if (!superChannels.isEmpty()) {
      RunnableAction commaAppender = StringActions.skipFirstTimeCommaAppender(sb);
      for (MethodStatement superChannel : superChannels) {
        commaAppender.run();
        sb.append(addImportAndGetSimpleName(NameConventionFunctions.getChannelClassCanonicalName(superChannel)));
      }
    } else {
      sb.append(addImportAndGetSimpleName(ChannelFunctions.getChannelClass(getQualifierTypes().size())));
      for (TraverseType traverseType : getTraverseTypes()) {
        if (traverseType.isMapping()) {
          sb.append(", ");
          sb.append(addImportAndGetSimpleName(MappingChannel.class));
        } else if (traverseType.isMoving()) {
          sb.append(", ");
          sb.append(addImportAndGetSimpleName(MovingChannel.class));
        } else if (traverseType.isMappingOfMoving()) {
          sb.append(", ");
          sb.append(addImportAndGetSimpleName(MappingOfMovingChannel.class));
        }
      }
    }
    return sb.toString();
  }

  private String getCid() {
    return ChannelFunctions.getCid(channelMethod);
  }

  private List<TraverseType> getTraverseTypes() {
    return ChannelFunctions.getTraverseTypes(channelMethod);
  }
}
