package tech.intellispaces.reflections.framework.annotationprocessor.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.javareflection.customtype.CustomType;
import tech.intellispaces.javareflection.method.MethodParam;
import tech.intellispaces.javareflection.method.MethodStatement;
import tech.intellispaces.reflections.framework.annotationprocessor.ReflectionsArtifactGenerator;
import tech.intellispaces.reflections.framework.factory.FactoryFunctions;
import tech.intellispaces.reflections.framework.factory.FactoryMethod;
import tech.intellispaces.reflections.framework.factory.FactoryMethods;
import tech.intellispaces.reflections.framework.factory.FactoryWrapper;
import tech.intellispaces.reflections.framework.naming.NameConventionFunctions;
import tech.intellispaces.reflections.framework.reflection.ReflectionFunctions;

public class FactoryWrapperGenerator extends ReflectionsArtifactGenerator {

  public FactoryWrapperGenerator(CustomType factoryType) {
    super(factoryType);
  }

  @Override
  public boolean isRelevant(ArtifactGeneratorContext context) {
    return true;
  }

  @Override
  public String generatedArtifactName() {
    return NameConventionFunctions.getObjectFactoryWrapperClassName(sourceArtifact().className());
  }

  @Override
  protected String templateName() {
    return "/factory_wrapper.template";
  }

  @Override
  protected boolean analyzeSourceArtifact(ArtifactGeneratorContext context) {
    if (sourceArtifact().isNested()) {
      addImport(sourceArtifactName());
    }
    addImport(List.class);
    addImport(ArrayList.class);
    addImport(FactoryWrapper.class);
    addImport(FactoryMethods.class);
    addImport(FactoryMethod.class);

    addVariable("factoryMethods", getFactoryMethods());
    return true;
  }

  List<Map<String, Object>> getFactoryMethods() {
    var methods = new ArrayList<Map<String, Object>>();
      for (MethodStatement method : sourceArtifact().declaredMethods()) {
        if (method.isStatic() || !method.isPublic()) {
          continue;
        }
        methods.add(Map.of(
            "name", method.name(),
            "contractType", FactoryFunctions.getContractType(method),
            "returnedType", getMethodReturnedType(method),
            "returnedDomainClass", getMethodReturnedDomainClass(method),
            "qualifiers", getContractQualifiers(method)
        ));
      }
    return methods;
  }

  private String getMethodReturnedType(MethodStatement method) {
    return method.returnType().orElseThrow().typeExpression(this::addImportAndGetSimpleName);
  }

  private String getMethodReturnedDomainClass(MethodStatement method) {
    CustomType reflectionType = method.returnType().orElseThrow().asCustomTypeReferenceOrElseThrow().targetType();
    CustomType domainType = ReflectionFunctions.getDomainOfObjectForm(reflectionType).orElseThrow();
    return addImportAndGetSimpleName(domainType.canonicalName());
  }

  private List<Map<String, Object>> getContractQualifiers(MethodStatement method) {
    List<Map<String, Object>> qualifiers = new ArrayList<>(method.params().size());
    for (MethodParam param : method.params()) {
      qualifiers.add(Map.of(
          "name", param.name(),
          "type", param.type().typeExpression(this::addImportAndGetSimpleName)
      ));
    }
    return qualifiers;
  }
}
