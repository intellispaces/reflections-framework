package tech.intellispaces.framework.core.annotation.processor.module;

import com.google.auto.service.AutoService;
import tech.intellispaces.framework.annotationprocessor.generator.ArtifactGenerator;
import tech.intellispaces.framework.annotationprocessor.validator.AnnotatedTypeValidator;
import tech.intellispaces.framework.core.annotation.Module;
import tech.intellispaces.framework.core.annotation.processor.AbstractAnnotationProcessor;
import tech.intellispaces.framework.core.system.ModuleFunctions;
import tech.intellispaces.framework.core.validation.ModuleValidator;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;

import javax.annotation.processing.Processor;
import javax.lang.model.element.ElementKind;
import java.util.ArrayList;
import java.util.List;
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
  protected AnnotatedTypeValidator getValidator() {
    return new ModuleValidator();
  }

  @Override
  protected List<ArtifactGenerator> makeArtifactGenerators(CustomType moduleType) {
    List<ArtifactGenerator> generators = new ArrayList<>();
    generators.add(new UnitWrapperGenerator(moduleType));
    Iterable<CustomType> includedUnits = ModuleFunctions.getIncludedUnits(moduleType);
    includedUnits.forEach(u -> generators.add(new UnitWrapperGenerator(u)));
    return generators;
  }
}
