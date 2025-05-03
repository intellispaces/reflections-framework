package tech.intellispaces.reflectionsj.annotationprocessor.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.reflectionsj.ArtifactType;
import tech.intellispaces.reflectionsj.annotation.Channel;
import tech.intellispaces.reflectionsj.annotation.Customizer;
import tech.intellispaces.reflectionsj.annotation.ObjectHandle;
import tech.intellispaces.reflectionsj.annotation.Unmovable;
import tech.intellispaces.reflectionsj.artifact.ArtifactTypes;
import tech.intellispaces.reflectionsj.naming.NameConventionFunctions;
import tech.intellispaces.reflectionsj.object.reference.MovabilityType;
import tech.intellispaces.reflectionsj.object.reference.MovabilityTypes;
import tech.intellispaces.reflectionsj.object.reference.ObjectReferenceForm;
import tech.intellispaces.reflectionsj.object.reference.ObjectReferenceForms;
import tech.intellispaces.reflectionsj.object.reference.ObjectReferenceFunctions;
import tech.intellispaces.reflectionsj.object.reference.UnmovableObjectHandle;
import tech.intellispaces.reflectionsj.space.channel.ChannelFunctions;
import tech.intellispaces.reflectionsj.space.domain.DomainFunctions;
import tech.intellispaces.reflectionsj.traverse.TraverseType;
import tech.intellispaces.statementsj.customtype.CustomType;
import tech.intellispaces.statementsj.instance.AnnotationInstance;
import tech.intellispaces.statementsj.method.MethodStatement;
import tech.intellispaces.statementsj.reference.CustomTypeReference;
import tech.intellispaces.statementsj.reference.NotPrimitiveReference;
import tech.intellispaces.statementsj.reference.TypeReferenceFunctions;

public class GeneralRegularObjectGenerator extends AbstractRegularObjectGenerator {

  public GeneralRegularObjectGenerator(CustomType domainType) {
    super(domainType);
  }

  @Override
  public boolean isRelevant(ArtifactGeneratorContext context) {
    return true;
  }

  @Override
  protected ObjectReferenceForm getForm() {
    return ObjectReferenceForms.Regular;
  }

  @Override
  protected MovabilityType getMovabilityType() {
    return MovabilityTypes.General;
  }

  @Override
  protected List<ArtifactType> relatedArtifactTypes() {
    return List.of(ArtifactTypes.RegularObject);
  }

  @Override
  public String generatedArtifactName() {
    return NameConventionFunctions.getGeneralRegularObjectTypename(sourceArtifact().className(), false);
  }

  @Override
  protected String templateName() {
    return "/general_regular_object.template";
  }

  @Override
  protected boolean analyzeSourceArtifact(ArtifactGeneratorContext context) {
    addImports(
        ObjectHandle.class,
        Unmovable.class,
        UnmovableObjectHandle.class,
        UnexpectedExceptions.class
    );

    analyzeDomain();
    analyzeObjectFormMethods(sourceArtifact(), context);
    analyzeConversionMethods(sourceArtifact());

    addVariable("handleTypeParamsFull", typeParamsFull);
    addVariable("domainMethods", methods);
    addVariable("parents", getParents(context));
    return true;
  }

  private List<String> getParents(ArtifactGeneratorContext context) {
    var parents = new ArrayList<String>();
    for (CustomTypeReference parent : sourceArtifact().parentTypes()) {
      parents.add(ObjectReferenceFunctions.geGeneralRegularObjectDeclaration(parent, false, this::addImportAndGetSimpleName)
      );
    }
    addExtraInterfaces(parents, context);
    return parents;
  }

  private void addExtraInterfaces(ArrayList<String> parents, ArtifactGeneratorContext context) {
    List<CustomType> customizers = findCustomizers(sourceArtifact(), context.initialRoundEnvironment());
    for (CustomType customizer : customizers) {
      AnnotationInstance annotation = customizer.selectAnnotation(Customizer.class.getCanonicalName()).orElseThrow();
      CustomType domain = annotation.valueOf("origin").orElseThrow().asClass().orElseThrow().type();
      Map<String, NotPrimitiveReference> parentTypeArgumentMapping = TypeReferenceFunctions.getTypeArgumentMapping(
          sourceArtifact(), domain
      );
      for (CustomTypeReference customizerParent : customizer.parentTypes()) {
        parents.add(
          customizerParent.effective(parentTypeArgumentMapping).actualDeclaration(this::addImportAndGetSimpleName)
        );
      }
    }
  }

  @Override
  protected Stream<MethodStatement> getObjectFormMethods(CustomType customType, ArtifactGeneratorContext context) {
    return buildActualType(customType, context)
        .actualMethods().stream()
        .filter(DomainFunctions::isNotDomainClassGetter)
        .filter(m -> excludeDeepConversionMethods(m, customType))
        .filter(m -> !ChannelFunctions.isChannelMethod(m)
            || ChannelFunctions.getTraverseTypes(m).stream().noneMatch(TraverseType::isMovingBased));
  }

  @Override
  protected Map<String, String> generateMethod(
      MethodStatement method, ObjectReferenceForm targetForm, int methodOrdinal
  ) {
    if (method.hasAnnotation(Channel.class)) {
      return super.generateMethod(method, targetForm, methodOrdinal);
    } else {
      return buildCustomizerMethod(method);
    }
  }

  private Map<String, String> buildCustomizerMethod(MethodStatement method) {
    var sb = new StringBuilder();
    appendMethodTypeParameters(sb, method);
    appendMethodReturnType(sb, method);
    sb.append(" ");
    sb.append(method.name());
    sb.append("(");
    appendMethodParams(sb, method);
    sb.append(")");
    appendMethodExceptions(sb, method);
    return Map.of(
        "javadoc", buildGeneratedMethodJavadoc(method.owner().canonicalName(), method),
        "declaration", sb.toString()
    );
  }
}
