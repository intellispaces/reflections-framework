package tech.intellispaces.jaquarius.annotationprocessor.objectprovider;

import tech.intellispaces.commons.annotation.processor.ArtifactGeneratorContext;
import tech.intellispaces.commons.reflection.customtype.CustomType;
import tech.intellispaces.commons.reflection.method.MethodParam;
import tech.intellispaces.commons.reflection.method.MethodStatement;
import tech.intellispaces.jaquarius.annotationprocessor.JaquariusArtifactGenerator;
import tech.intellispaces.jaquarius.engine.ObjectProviderWrapper;
import tech.intellispaces.jaquarius.engine.description.ObjectProviderMethodDescription;
import tech.intellispaces.jaquarius.engine.description.ObjectProviderMethods;
import tech.intellispaces.jaquarius.naming.NameConventionFunctions;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceFunctions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ObjectProviderWrapperGenerator extends JaquariusArtifactGenerator {

  public ObjectProviderWrapperGenerator(CustomType objectProviderType) {
    super(objectProviderType);
  }

  @Override
  public boolean isRelevant(ArtifactGeneratorContext context) {
    return true;
  }

  @Override
  public String generatedArtifactName() {
    return NameConventionFunctions.getObjectProviderWrapperClassName(sourceArtifact().className());
  }

  @Override
  protected String templateName() {
    return "/object_provider_wrapper.template";
  }

  @Override
  protected boolean analyzeSourceArtifact(ArtifactGeneratorContext context) {
    if (sourceArtifact().isNested()) {
      addImport(sourceArtifactName());
    }
    addImport(List.class);
    addImport(ArrayList.class);
    addImport(ObjectProviderWrapper.class);
    addImport(ObjectProviderMethods.class);
    addImport(ObjectProviderMethodDescription.class);

    addVariable("providerMethods", getProviderMethods());
    return true;
  }

  List<Map<String, Object>> getProviderMethods() {
    var methods = new ArrayList<Map<String, Object>>();
      for (MethodStatement method : sourceArtifact().declaredMethods()) {
        if (method.isStatic()) {
          continue;
        }
        methods.add(Map.of(
            "name", method.name(),
            "returnedType", getMethodReturnedType(method),
            "returnedDomainClass", getMethodReturnedDomainClass(method),
            "paramParamTypes", getMethodParamTypes(method)
        ));
      }
    return methods;
  }

  private String getMethodReturnedType(MethodStatement method) {
    return method.returnType().orElseThrow().typeExpression(this::addImportAndGetSimpleName);
  }

  private String getMethodReturnedDomainClass(MethodStatement method) {
    CustomType objectHandleType = method.returnType().orElseThrow().asCustomTypeReferenceOrElseThrow().targetType();
    CustomType domainType = ObjectReferenceFunctions.getDomainOfObjectForm(objectHandleType).orElseThrow();
    return addImportAndGetSimpleName(domainType.canonicalName());
  }

  private List<String> getMethodParamTypes(MethodStatement method) {
    return method.params().stream()
        .map(MethodParam::type)
        .map(t -> t.typeExpression(this::addImportAndGetSimpleName))
        .toList();
  }
}
