package tech.intellispaces.jaquarius.annotationprocessor.domain;

import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.jaquarius.ArtifactType;
import tech.intellispaces.jaquarius.annotation.Channel;
import tech.intellispaces.jaquarius.annotation.Extension;
import tech.intellispaces.jaquarius.annotation.Movable;
import tech.intellispaces.jaquarius.annotation.ObjectHandle;
import tech.intellispaces.jaquarius.annotation.Unmovable;
import tech.intellispaces.jaquarius.artifact.ArtifactTypes;
import tech.intellispaces.jaquarius.naming.NameConventionFunctions;
import tech.intellispaces.jaquarius.object.reference.MovabilityType;
import tech.intellispaces.jaquarius.object.reference.MovabilityTypes;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForm;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForms;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceFunctions;
import tech.intellispaces.jaquarius.object.reference.UnmovableObjectHandle;
import tech.intellispaces.jaquarius.space.channel.ChannelFunctions;
import tech.intellispaces.jaquarius.space.domain.DomainFunctions;
import tech.intellispaces.jaquarius.traverse.TraverseType;
import tech.intellispaces.reflection.customtype.CustomType;
import tech.intellispaces.reflection.instance.AnnotationInstance;
import tech.intellispaces.reflection.method.MethodStatement;
import tech.intellispaces.reflection.reference.CustomTypeReference;
import tech.intellispaces.reflection.reference.NotPrimitiveReference;
import tech.intellispaces.reflection.reference.TypeReference;
import tech.intellispaces.reflection.reference.TypeReferenceFunctions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class GeneralPlainObjectGenerator extends AbstractPlainObjectGenerator {

  public GeneralPlainObjectGenerator(CustomType domainType) {
    super(domainType);
  }

  @Override
  public boolean isRelevant(ArtifactGeneratorContext context) {
    return true;
  }

  @Override
  protected ObjectReferenceForm getForm() {
    return ObjectReferenceForms.Plain;
  }

  @Override
  protected MovabilityType getMovabilityType() {
    return MovabilityTypes.General;
  }

  @Override
  protected List<ArtifactType> relatedArtifactTypes() {
    return List.of(ArtifactTypes.PlainObject);
  }

  @Override
  public String generatedArtifactName() {
    return NameConventionFunctions.getGeneralPlainObjectTypename(sourceArtifact().className());
  }

  @Override
  protected String templateName() {
    return "/general_plain_object.template";
  }

  @Override
  protected boolean analyzeSourceArtifact(ArtifactGeneratorContext context) {
    addImports(
        ObjectHandle.class,
        Unmovable.class,
        UnmovableObjectHandle.class,
        UnexpectedExceptions.class
    );
    addHiddenImports(context);

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
      parents.add(ObjectReferenceFunctions.geGeneralPlainObjectDeclaration(parent, false, this::addImportAndGetSimpleName)
      );
    }
    addExtraInterfaces(parents, context);
    return parents;
  }

  private void addExtraInterfaces(ArrayList<String> parents, ArtifactGeneratorContext context) {
    List<CustomType> extensions = findExtensions(sourceArtifact(), context.initialRoundEnvironment());
    for (CustomType extension : extensions) {
      AnnotationInstance annotation = extension.selectAnnotation(Extension.class.getCanonicalName()).orElseThrow();
      CustomType domain = annotation.valueOf("origin").orElseThrow().asClass().orElseThrow().type();
      Map<String, NotPrimitiveReference> parentTypeArgumentMapping = TypeReferenceFunctions.getTypeArgumentMapping(
          sourceArtifact(), domain
      );
      for (CustomTypeReference extensionParent : extension.parentTypes()) {
        parents.add(
          extensionParent.effective(parentTypeArgumentMapping).actualDeclaration(this::addImportAndGetSimpleName)
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
      return buildExtensionMethod(method);
    }
  }

  private Map<String, String> buildExtensionMethod(MethodStatement method) {
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

  @Override
  protected void appendObjectFormMethodReturnType(StringBuilder sb, MethodStatement method) {
    TypeReference domainReturnType = method.returnType().orElseThrow();
    if (method.hasAnnotation(Movable.class)) {
      sb.append(buildObjectFormDeclaration(domainReturnType, ObjectReferenceForms.Plain, MovabilityTypes.Movable, true));
    } else if (method.hasAnnotation(Unmovable.class)) {
      sb.append(buildObjectFormDeclaration(domainReturnType, ObjectReferenceForms.Plain, MovabilityTypes.Unmovable, true));
    } else {
      sb.append(buildObjectFormDeclaration(domainReturnType, ObjectReferenceForms.Plain, MovabilityTypes.General, true));
    }
  }
}
