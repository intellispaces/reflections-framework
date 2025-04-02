package tech.intellispaces.jaquarius.annotationprocessor.domain;

import tech.intellispaces.commons.action.Action;
import tech.intellispaces.commons.annotation.processor.ArtifactGeneratorContext;
import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.commons.reflection.customtype.CustomType;
import tech.intellispaces.commons.reflection.method.MethodParam;
import tech.intellispaces.commons.reflection.method.MethodSignatureDeclarations;
import tech.intellispaces.commons.reflection.method.MethodStatement;
import tech.intellispaces.commons.reflection.reference.ArrayReference;
import tech.intellispaces.commons.reflection.reference.TypeReference;
import tech.intellispaces.commons.text.StringFunctions;
import tech.intellispaces.jaquarius.annotationprocessor.AnnotationFunctions;
import tech.intellispaces.jaquarius.annotationprocessor.ArtifactTypes;
import tech.intellispaces.jaquarius.annotationprocessor.JaquariusArtifactGenerator;
import tech.intellispaces.jaquarius.engine.JaquariusEngines;
import tech.intellispaces.jaquarius.naming.NameConventionFunctions;
import tech.intellispaces.jaquarius.object.provider.ObjectProviderFunctions;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceFunctions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ObjectProviderBrokerGenerator extends JaquariusArtifactGenerator {

  public ObjectProviderBrokerGenerator(CustomType domainType) {
    super(domainType);
  }

  @Override
  protected String templateName() {
    return "/object_provider_broker.template";
  }

  @Override
  public String generatedArtifactName() {
    return NameConventionFunctions.getObjectProviderBrokerCanonicalName(sourceArtifact());
  }

  @Override
  public boolean isRelevant(ArtifactGeneratorContext context) {
    // Starts the Object Provider generator after all other types of generators
    return context.isPenaltyRound();
  }

  @Override
  protected boolean analyzeSourceArtifact(ArtifactGeneratorContext context) {
    Collection<CustomType> customizers = findCustomizers(context);
    if (customizers.isEmpty()) {
      return false;
    }

    addImport(JaquariusEngines.class);
    addImport(UnexpectedExceptions.class);

    addVariable("customizers", getCustomizers(customizers));
    addVariable("customizeMethods", getCustomizeMethods(customizers));
    return true;
  }

  List<String> getCustomizers(Collection<CustomType> customizers) {
    return customizers.stream()
        .map(CustomType::canonicalName)
        .map(this::addImportAndGetSimpleName)
        .toList();
  }

  List<Map<String, Object>> getCustomizeMethods(Collection<CustomType> customizers) {
    var methods = new ArrayList<Map<String, Object>>();
    for (CustomType customizer : customizers) {
      for (MethodStatement method : customizer.declaredMethods()) {
        if (method.isDefault()) {
          continue;
        }
        methods.add(Map.of(
            "signature", MethodSignatureDeclarations.build(method).get(this::addImport, this::simpleNameOf),
            "returnTypeDeclaration", method.returnType().orElseThrow().actualDeclaration(this::addImportAndGetSimpleName),
            "paramNames", method.params().stream().map(MethodParam::name).collect(Collectors.joining(", ")),
            "returnDomainClass", getReturnDomainClassCanonicalName(method),
            "actionName", makeActionName(method),
            "actionType", makeActionType(method),
            "returnType", makeReturnType(method),
            "paramTypes", makeParamTypes(method),
            "contractType", ObjectProviderFunctions.getContractType(method)
        ));
      }
    }
    return methods;
  }

  Collection<CustomType> findCustomizers(ArtifactGeneratorContext context) {
    return AnnotationFunctions.findArtifactCustomizers(
        sourceArtifact(), ArtifactTypes.ObjectProvider, context.roundEnvironments()
    );
  }

  String makeActionName(MethodStatement method) {
    var sb = new StringBuilder();
    sb.append(method.name());
    if (!method.params().isEmpty()) {
      sb.append("By");
      for (MethodParam param : method.params()) {
        appendActionNameParam(sb, param.type());
      }
    }
    sb.append("ObjectProviderAction");
    return sb.toString();
  }

  private void appendActionNameParam(StringBuilder sb, TypeReference paramType) {
    if (paramType.isCustomTypeReference()) {
      sb.append(StringFunctions.capitalizeFirstLetter(paramType.asCustomTypeReferenceOrElseThrow().targetType().simpleName()));
    } else if (paramType.isNamedReference()) {
      sb.append("Object");
    } else if (paramType.isPrimitiveReference()) {
      sb.append(StringFunctions.capitalizeFirstLetter(paramType.asPrimitiveReferenceOrElseThrow().typename()));
    } else if (paramType.isArrayReference()) {
      ArrayReference arrayReference = paramType.asArrayReferenceOrElseThrow();
      appendActionNameParam(sb, arrayReference.elementType());
      sb.append("s");
    } else {
      throw NotImplementedExceptions.withCode("KTwt1fni");
    }
  }

  String makeActionType(MethodStatement method) {
    var sb = new StringBuilder();
    sb.append(addImportAndGetSimpleName(Action.class.getCanonicalName() + method.params().size()));
    sb.append("<");
    sb.append(method.returnType().orElseThrow().actualRawDeclaration(this::addImportAndGetSimpleName));
    for (MethodParam param : method.params()) {
      sb.append(", ");
      if (param.type().isPrimitiveReference()) {
        sb.append(addImportAndGetSimpleName(param.type().asPrimitiveReferenceOrElseThrow().primitiveType().wrapperClass()));
      } else {
        sb.append(param.type().actualRawDeclaration(this::addImportAndGetSimpleName));
      }
    }
    sb.append(">");
    return sb.toString();
  }

  String makeReturnType(MethodStatement method) {
    return method.returnType().orElseThrow().typeExpression(this::addImportAndGetSimpleName);
  }

  List<String> makeParamTypes(MethodStatement method) {
    return method.params().stream()
        .map(MethodParam::type)
        .map(type -> type.typeExpression(this::addImportAndGetSimpleName))
        .toList();
  }

  String getReturnDomainClassCanonicalName(MethodStatement method) {
    CustomType objectHandleType = method.returnType().orElseThrow().asCustomTypeReferenceOrElseThrow().targetType();
    CustomType domainType = ObjectReferenceFunctions.getDomainOfObjectForm(objectHandleType).orElseThrow();
    return addImportAndGetSimpleName(domainType.canonicalName());
  }
}
