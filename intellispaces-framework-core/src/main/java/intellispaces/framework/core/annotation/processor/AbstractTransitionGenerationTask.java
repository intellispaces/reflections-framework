package intellispaces.framework.core.annotation.processor;

import intellispaces.common.base.exception.UnexpectedViolationException;
import intellispaces.common.base.type.TypeFunctions;
import intellispaces.common.javastatement.Statement;
import intellispaces.common.javastatement.customtype.CustomType;
import intellispaces.common.javastatement.method.MethodStatement;
import intellispaces.common.javastatement.reference.CustomTypeReferences;
import intellispaces.common.javastatement.reference.TypeReference;
import intellispaces.framework.core.annotation.Transition;
import intellispaces.framework.core.space.transition.TransitionFunctions;
import intellispaces.framework.core.traverse.TraverseTypes;

import javax.annotation.processing.RoundEnvironment;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractTransitionGenerationTask extends AbstractGenerationTask {
  protected final MethodStatement transitionMethod;
  private String transitionMethodSignature;
  private String transitionClass;
  private String transitionTypeParamsFull;
  private String transitionTypeParamsBrief;

  public AbstractTransitionGenerationTask(
      CustomType initiatorType, CustomType annotatedType, MethodStatement transitionMethod
  ) {
    super(initiatorType, annotatedType);
    this.transitionMethod = transitionMethod;
  }

  protected abstract String getTransitionClassCanonicalName();

  protected abstract String getTransitionClassTypeParams();

  protected abstract String getTransitionMethodSignature();

  protected abstract Statement getSourceType();

  protected abstract TypeReference getTargetType();

  protected abstract List<TypeReference> getQualifierTypes();

  @Override
  public String artifactName() {
    return getTransitionClassCanonicalName();
  }

  @Override
  protected String templateName() {
    return "/transition.template";
  }

  @Override
  protected Map<String, Object> templateVariables() {
    Map<String, Object> vars = new HashMap<>();
    vars.put("generatedAnnotation", makeGeneratedAnnotation());
    vars.put("packageName", context.packageName());
    vars.put("sourceClassName", sourceClassCanonicalName());
    vars.put("sourceClassSimpleName", sourceClassSimpleName());

    boolean isMovingTransition = TransitionFunctions.getTraverseTypes(transitionMethod).contains(TraverseTypes.Moving);
    if (isMovingTransition) {
      vars.put("targetClassLink", AnnotationProcessorFunctions.getDomainClassLink(CustomTypeReferences.get(annotatedType)));
    } else {
      vars.put("targetClassLink", AnnotationProcessorFunctions.getDomainClassLink(transitionMethod.returnType().orElseThrow()));
    }

    vars.put("classSimpleName", context.generatedClassSimpleName());
    vars.put("importedClasses", context.getImports());
    vars.put("transitionMethod", transitionMethodSignature);
    vars.put("transitionClass", transitionClass);
    vars.put("transitionTypeParamsFull", transitionTypeParamsFull);
    vars.put("transitionTypeParamsBrief", transitionTypeParamsBrief);
    vars.put("transitionMethodName", transitionMethod.name());
    vars.put("tid", getTid());
    return vars;
  }

  @Override
  protected boolean analyzeAnnotatedType(RoundEnvironment roundEnv) {
    context.generatedClassCanonicalName(artifactName());
    if (annotatedType.isNested()) {
      context.addImport(sourceClassCanonicalName());
    }
    context.addImport(Transition.class);

    transitionClass = getTransitionClass();
    transitionTypeParamsFull = getTransitionClassTypeParams();
    transitionTypeParamsBrief = getTransitionTypeParamsBrief();
    transitionMethodSignature = getTransitionMethodSignature();
    return true;
  }

  private String getTransitionClass() {
    return context.addToImportAndGetSimpleName(
        TransitionFunctions.getTransitionClass(getQualifierTypes().size())
    );
  }

  private String getTransitionTypeParamsBrief() {
    var sb = new StringBuilder();
    sb.append("<");
    appendTypeDeclaration(sb, getSourceType());
    sb.append(", ");
    if (TransitionFunctions.getTraverseTypes(transitionMethod).contains(TraverseTypes.Moving)) {
      appendTypeDeclaration(sb, getSourceType());
    } else {
      appendTypeDeclaration(sb, getTargetType());
    }
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
    return transitionMethod.selectAnnotation(Transition.class).orElseThrow().value();
  }

  private TraverseTypes getTransitionType() {
    return TransitionFunctions.getTraverseType(transitionMethod);
  }
}
