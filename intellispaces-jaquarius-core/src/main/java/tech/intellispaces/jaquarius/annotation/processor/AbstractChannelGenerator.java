package tech.intellispaces.jaquarius.annotation.processor;

import tech.intellispaces.jaquarius.annotation.Channel;
import tech.intellispaces.jaquarius.channel.MappingChannel;
import tech.intellispaces.jaquarius.channel.MappingOfMovingChannel;
import tech.intellispaces.jaquarius.channel.MovingChannel;
import tech.intellispaces.jaquarius.common.NameConventionFunctions;
import tech.intellispaces.jaquarius.space.channel.ChannelFunctions;
import tech.intellispaces.jaquarius.traverse.TraverseType;
import tech.intellispaces.action.runnable.RunnableAction;
import tech.intellispaces.action.text.StringActions;
import tech.intellispaces.java.reflection.customtype.CustomType;
import tech.intellispaces.java.reflection.method.MethodStatement;
import tech.intellispaces.java.reflection.reference.TypeReference;

import javax.annotation.processing.RoundEnvironment;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractChannelGenerator extends AbstractGenerator {
  protected final MethodStatement channelMethod;
  private String channelMethodSignature;
  private String channelClasses;

  public AbstractChannelGenerator(
      CustomType initiatorType, CustomType annotatedType, MethodStatement channelMethod
  ) {
    super(initiatorType, annotatedType);
    this.channelMethod = channelMethod;
  }

  protected abstract String getChannelClassCanonicalName();

  protected abstract String getChannelMethodSignature();

  protected abstract List<TypeReference> getQualifierTypes();

  @Override
  public String artifactName() {
    return getChannelClassCanonicalName();
  }

  @Override
  protected String templateName() {
    return "/channel.template";
  }

  @Override
  protected Map<String, Object> templateVariables() {
    Map<String, Object> vars = new HashMap<>();
    vars.put("generatedAnnotation", makeGeneratedAnnotation());
    vars.put("packageName", context.packageName());
    vars.put("sourceClassName", sourceClassCanonicalName());
    vars.put("sourceClassSimpleName", sourceClassSimpleName());
    vars.put("targetClassLink", AnnotationProcessorFunctions.getDomainClassLink(channelMethod.returnType().orElseThrow()));
    vars.put("classSimpleName", context.generatedClassSimpleName());
    vars.put("importedClasses", context.getImports());
    vars.put("channelMethod", channelMethodSignature);
    vars.put("channelClasses", channelClasses);
    vars.put("channelMethodName", channelMethod.name());
    vars.put("cid", getCid());
    return vars;
  }

  @Override
  protected boolean analyzeAnnotatedType(RoundEnvironment roundEnv) {
    context.generatedClassCanonicalName(artifactName());
    if (annotatedType.isNested()) {
      context.addImport(sourceClassCanonicalName());
    }
    context.addImport(Channel.class);

    channelClasses = defineChannelClasses();
    channelMethodSignature = getChannelMethodSignature();
    return true;
  }

  private String defineChannelClasses() {
    var sb = new StringBuilder();

    List<MethodStatement> superChannels = channelMethod.overrideMethods();
    if (!superChannels.isEmpty()) {
      RunnableAction commaAppender = StringActions.skipFirstTimeCommaAppender(sb);
      for (MethodStatement superChannel : superChannels) {
        commaAppender.run();
        sb.append(
            context.addToImportAndGetSimpleName(NameConventionFunctions.getChannelClassCanonicalName(superChannel))
        );
      }
    } else {
      sb.append(context.addToImportAndGetSimpleName(ChannelFunctions.getChannelClass(getQualifierTypes().size())));
      for (TraverseType traverseType : getTraverseTypes()) {
        if (traverseType.isMapping()) {
          sb.append(", ");
          sb.append(context.addToImportAndGetSimpleName(MappingChannel.class));
        } else if (traverseType.isMoving()) {
          sb.append(", ");
          sb.append(context.addToImportAndGetSimpleName(MovingChannel.class));
        } else if (traverseType.isMappingOfMoving()) {
          sb.append(", ");
          sb.append(context.addToImportAndGetSimpleName(MappingOfMovingChannel.class));
        }
      }
    }
    return sb.toString();
  }

  private String getCid() {
    return channelMethod.selectAnnotation(Channel.class).orElseThrow().value();
  }

  private List<TraverseType> getTraverseTypes() {
    return ChannelFunctions.getTraverseTypes(channelMethod);
  }
}
