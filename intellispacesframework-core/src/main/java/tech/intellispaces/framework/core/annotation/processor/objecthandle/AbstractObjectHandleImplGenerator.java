package tech.intellispaces.framework.core.annotation.processor.objecthandle;

import tech.intellispaces.framework.commons.action.Action;
import tech.intellispaces.framework.commons.type.TypeFunctions;
import tech.intellispaces.framework.core.annotation.ObjectHandle;
import tech.intellispaces.framework.core.annotation.processor.AbstractGenerator;
import tech.intellispaces.framework.core.common.ActionFunctions;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;
import tech.intellispaces.framework.javastatements.statement.method.MethodParam;
import tech.intellispaces.framework.javastatements.statement.method.MethodStatement;
import tech.intellispaces.framework.javastatements.statement.reference.NamedTypeReference;
import tech.intellispaces.framework.javastatements.statement.reference.TypeReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

abstract class AbstractObjectHandleImplGenerator extends AbstractGenerator {
  protected String domainSimpleClassName;
  protected String typeParamsFull;
  protected String typeParamsBrief;
  protected List<Object> constructors;

  AbstractObjectHandleImplGenerator(CustomType objectHandleType) {
    super(objectHandleType);
  }

  protected String getGeneratedClassCanonicalName() {
    Optional<ObjectHandle> annotation = annotatedType.selectAnnotation(ObjectHandle.class);
    if (annotation.isPresent()) {
      return TypeFunctions.replaceSimpleName(annotatedType.canonicalName(), annotation.get().value());
    } else {
      return annotatedType.canonicalName() + "Impl";
    }
  }

  protected void analyzeTypeParams(CustomType objectHandleType) {
    if (objectHandleType.typeParameters().isEmpty()) {
      typeParamsFull = typeParamsBrief = "";
      return;
    }

    var typeParamsFullBuilder = new StringBuilder();
    var typeParamsBriefBuilder = new StringBuilder();
    Action addCommaAction = ActionFunctions.buildAppendSeparatorAction(typeParamsFullBuilder, ", ")
        .join(ActionFunctions.buildAppendSeparatorAction(typeParamsBriefBuilder, ", "));

    typeParamsFullBuilder.append("<");
    typeParamsBriefBuilder.append("<");
    for (NamedTypeReference typeParam : objectHandleType.typeParameters()) {
      addCommaAction.execute();
      typeParamsFullBuilder.append(typeParam.formalFullDeclaration());
      typeParamsBriefBuilder.append(typeParam.formalBriefDeclaration());
    }
    typeParamsFullBuilder.append(">");
    typeParamsBriefBuilder.append(">");
    typeParamsFull = typeParamsFullBuilder.toString();
    typeParamsBrief = typeParamsBriefBuilder.toString();
  }

  @SuppressWarnings("unchecked, rawtypes")
  protected void analyzeConstructors(CustomType objectHandleType) {
    List<MethodStatement> constructors;
    if (objectHandleType.asClass().isPresent()) {
      constructors = objectHandleType.asClass().get().constructors();
      List<Map<String, Object>> constructorDescriptors = new ArrayList<>();
      for (MethodStatement constructor : constructors) {
        constructorDescriptors.add(analyzeConstructor(constructor, context.getImportConsumer()));
      }
      this.constructors = (List) constructorDescriptors;
    } else {
      this.constructors = List.of();
    }
  }

  private Map<String, Object> analyzeConstructor(MethodStatement constructor, Consumer<String> imports) {
    Map<String, Object> constructorDescriptor = new HashMap<>();
    List<Map<String, String>> paramDescriptors = new ArrayList<>();
    for (MethodParam param : constructor.params()) {
      TypeReference type = param.type();
      type.dependencyTypenames().forEach(imports);
      paramDescriptors.add(Map.of(
              "name", param.name(),
              "type", type.actualDeclaration(context::simpleNameOf)
          )
      );
      type.asCustomTypeReference().ifPresent(t -> imports.accept(t.targetType().canonicalName()));
    }
    constructorDescriptor.put("params", paramDescriptors);
    return constructorDescriptor;
  }
}
