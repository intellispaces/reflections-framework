package intellispaces.framework.core.annotation.processor;

import intellispaces.common.base.exception.UnexpectedViolationException;
import intellispaces.common.base.type.TypeFunctions;
import intellispaces.common.javastatement.Statement;
import intellispaces.common.javastatement.customtype.CustomType;
import intellispaces.common.javastatement.method.MethodStatement;
import intellispaces.common.javastatement.reference.TypeReference;
import intellispaces.framework.core.annotation.Channel;
import intellispaces.framework.core.space.channel.ChannelFunctions;
import intellispaces.framework.core.traverse.TraverseTypes;

import javax.annotation.processing.RoundEnvironment;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractChannelGenerator extends AbstractGenerator {
  protected final MethodStatement channelMethod;
  private String channelMethodSignature;
  private String channelClass;
  private String channelTypeParamsFull;
  private String channelTypeParamsBrief;

  public AbstractChannelGenerator(
      CustomType initiatorType, CustomType annotatedType, MethodStatement channelMethod
  ) {
    super(initiatorType, annotatedType);
    this.channelMethod = channelMethod;
  }

  protected abstract String getChannelClassCanonicalName();

  protected abstract String getChannelClassTypeParams();

  protected abstract String getChannelMethodSignature();

  protected abstract Statement getSourceType();

  protected abstract TypeReference getResultType();

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
    vars.put("channelClass", channelClass);
    vars.put("channelTypeParamsFull", channelTypeParamsFull);
    vars.put("channelTypeParamsBrief", channelTypeParamsBrief);
    vars.put("channelMethodName", channelMethod.name());
    vars.put("cid", getTid());
    return vars;
  }

  @Override
  protected boolean analyzeAnnotatedType(RoundEnvironment roundEnv) {
    context.generatedClassCanonicalName(artifactName());
    if (annotatedType.isNested()) {
      context.addImport(sourceClassCanonicalName());
    }
    context.addImport(Channel.class);

    channelClass = getChannelClass();
    channelTypeParamsFull = getChannelClassTypeParams();
    channelTypeParamsBrief = getChannelTypeParamsBrief();
    channelMethodSignature = getChannelMethodSignature();
    return true;
  }

  private String getChannelClass() {
    return context.addToImportAndGetSimpleName(
        ChannelFunctions.getChannelClass(getQualifierTypes().size())
    );
  }

  private String getChannelTypeParamsBrief() {
    var sb = new StringBuilder();
    sb.append("<");
    appendTypeDeclaration(sb, getSourceType());
    sb.append(", ");
    appendTypeDeclaration(sb, getResultType());
    for (TypeReference qualifierType : getQualifierTypes()) {
      sb.append(", ");
      appendTypeDeclaration(sb, qualifierType);
    }
    sb.append(">");
    return sb.toString();
  }

  private void appendTypeDeclaration(StringBuilder sb, Statement type) {
    if (type instanceof CustomType customType) {
      sb.append(customType.simpleName());
      sb.append(customType.typeParametersBriefDeclaration());
    } else if (type instanceof TypeReference typeReference) {
      if (typeReference.isPrimitiveReference()) {
        Class<?> wrapperClass = TypeFunctions.getPrimitiveWrapperClass(typeReference.asPrimitiveReferenceOrElseThrow().typename());
        sb.append(wrapperClass.getSimpleName());
      } else {
        sb.append(typeReference.actualDeclaration(context::addToImportAndGetSimpleName));
      }
    } else {
      throw UnexpectedViolationException.withMessage("Unexpected statement type {0}", type.statementType());
    }
  }

  private String getTid() {
    return channelMethod.selectAnnotation(Channel.class).orElseThrow().value();
  }

  private TraverseTypes getChannelType() {
    return ChannelFunctions.getTraverseType(channelMethod);
  }
}
