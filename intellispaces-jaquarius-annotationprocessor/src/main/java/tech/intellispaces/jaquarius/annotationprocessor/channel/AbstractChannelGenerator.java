package tech.intellispaces.jaquarius.annotationprocessor.channel;

import java.util.List;

import tech.intellispaces.actions.runnable.RunnableAction;
import tech.intellispaces.actions.text.StringActions;
import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.jaquarius.annotation.Channel;
import tech.intellispaces.jaquarius.annotationprocessor.JaquariusArtifactGenerator;
import tech.intellispaces.jaquarius.naming.NameConventionFunctions;
import tech.intellispaces.jaquarius.space.channel.ChannelFunctions;
import tech.intellispaces.jaquarius.traverse.MappingOfMovingTraverse;
import tech.intellispaces.jaquarius.traverse.MappingTraverse;
import tech.intellispaces.jaquarius.traverse.MovingTraverse;
import tech.intellispaces.jaquarius.traverse.TraverseType;
import tech.intellispaces.reflection.customtype.CustomType;
import tech.intellispaces.reflection.method.MethodStatement;
import tech.intellispaces.reflection.reference.TypeReference;

public abstract class AbstractChannelGenerator extends JaquariusArtifactGenerator {
  protected final MethodStatement channelMethod;

  public AbstractChannelGenerator(CustomType annotatedType, MethodStatement channelMethod) {
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
    addHiddenImports(context);

    addVariable("targetClassLink", buildDomainLink(channelMethod.returnType().orElseThrow()));
    addVariable("channelMethod", buildChannelMethodSignature());
    addVariable("channelClasses", buildChannelClassesDeclaration());
    addVariable("channelMethodName", channelMethod.name());
    addVariable("id", getCid());
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
          sb.append(addImportAndGetSimpleName(MappingTraverse.class));
        } else if (traverseType.isMoving()) {
          sb.append(", ");
          sb.append(addImportAndGetSimpleName(MovingTraverse.class));
        } else if (traverseType.isMappingOfMoving()) {
          sb.append(", ");
          sb.append(addImportAndGetSimpleName(MappingOfMovingTraverse.class));
        }
      }
    }
    return sb.toString();
  }

  private String getCid() {
    return ChannelFunctions.getChannelId(channelMethod);
  }

  private List<TraverseType> getTraverseTypes() {
    return ChannelFunctions.getTraverseTypes(channelMethod);
  }
}
