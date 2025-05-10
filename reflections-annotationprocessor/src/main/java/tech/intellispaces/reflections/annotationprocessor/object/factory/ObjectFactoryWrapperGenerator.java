package tech.intellispaces.reflections.annotationprocessor.object.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.reflections.annotationprocessor.JaquariusArtifactGenerator;
import tech.intellispaces.reflections.framework.engine.ObjectFactoryWrapper;
import tech.intellispaces.reflections.framework.engine.description.ObjectFactoryMethodDescription;
import tech.intellispaces.reflections.framework.engine.description.ObjectFactoryMethods;
import tech.intellispaces.reflections.framework.naming.NameConventionFunctions;
import tech.intellispaces.reflections.framework.object.reference.ObjectReferenceFunctions;
import tech.intellispaces.jstatements.customtype.CustomType;
import tech.intellispaces.jstatements.method.MethodParam;
import tech.intellispaces.jstatements.method.MethodStatement;

public class ObjectFactoryWrapperGenerator extends JaquariusArtifactGenerator {
  private final ObjectFactoryMetaInfGenerator metaInfGenerator;

  public ObjectFactoryWrapperGenerator(
      CustomType objectFactoryType,
      ObjectFactoryMetaInfGenerator metaInfGenerator
  ) {
    super(objectFactoryType);
    this.metaInfGenerator = metaInfGenerator;
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
    return "/object_factory_wrapper.template";
  }

  @Override
  protected boolean analyzeSourceArtifact(ArtifactGeneratorContext context) {
    if (sourceArtifact().isNested()) {
      addImport(sourceArtifactName());
    }
    addImport(List.class);
    addImport(ArrayList.class);
    addImport(ObjectFactoryWrapper.class);
    addImport(ObjectFactoryMethods.class);
    addImport(ObjectFactoryMethodDescription.class);

    addVariable("factoryMethods", getFactoryMethods());

    metaInfGenerator.addObjectFactory(generatedArtifactName());
    return true;
  }

  List<Map<String, Object>> getFactoryMethods() {
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
