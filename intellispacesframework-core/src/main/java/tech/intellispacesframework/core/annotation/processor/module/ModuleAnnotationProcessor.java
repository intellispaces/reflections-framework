package tech.intellispacesframework.core.annotation.processor.module;

import com.google.auto.service.AutoService;
import tech.intellispacesframework.annotationprocessor.generator.ArtifactGenerator;
import tech.intellispacesframework.core.annotation.Module;
import tech.intellispacesframework.core.annotation.processor.AbstractAnnotationProcessor;
import tech.intellispacesframework.javastatements.statement.custom.CustomType;
import tech.intellispacesframework.javastatements.statement.instance.AnnotationInstance;
import tech.intellispacesframework.javastatements.statement.instance.Instance;

import javax.annotation.processing.Processor;
import javax.lang.model.element.ElementKind;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@AutoService(Processor.class)
public class ModuleAnnotationProcessor extends AbstractAnnotationProcessor {

  public ModuleAnnotationProcessor() {
    super(Module.class, Set.of(ElementKind.CLASS));
  }

  @Override
  protected boolean isApplicable(CustomType moduleType) {
    return isAutoGenerationEnabled(moduleType);
  }

  @Override
  protected List<ArtifactGenerator> makeArtifactGenerators(CustomType moduleType) {
    Iterable<CustomType> includedUnitTypes = getIncludedUnitTypes(moduleType);
    ModuleValidator.validateModule(moduleType, includedUnitTypes);

    List<ArtifactGenerator> makers = new ArrayList<>();
    makers.add(new UnitWrapperGenerator(moduleType));
    includedUnitTypes.forEach(u -> makers.add(new UnitWrapperGenerator(u)));
    return makers;
  }

  private Iterable<CustomType> getIncludedUnitTypes(CustomType moduleType) {
    Map<String, CustomType> unitTypes = new HashMap<>();
    AnnotationInstance moduleAnnotation = moduleType.selectAnnotation(Module.class.getCanonicalName()).orElseThrow();
    Optional<Instance> unitsAttr = moduleAnnotation.elementValue("units");
    if (unitsAttr.isPresent()) {
      List<Instance> units = unitsAttr.get().asArray().orElseThrow().elements();
      for (Instance unit : units) {
        CustomType unitType = unit.asClass().orElseThrow().type();
        if (!unitTypes.containsKey(unitType.className())) {
          unitTypes.put(unitType.className(), unitType);
        }
      }
    }
    return unitTypes.values();
  }
}
