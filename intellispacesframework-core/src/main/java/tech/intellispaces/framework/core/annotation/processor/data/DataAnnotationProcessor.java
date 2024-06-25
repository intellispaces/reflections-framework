package tech.intellispaces.framework.core.annotation.processor.data;

import com.google.auto.service.AutoService;
import tech.intellispaces.framework.annotationprocessor.AnnotatedTypeValidator;
import tech.intellispaces.framework.annotationprocessor.generator.ArtifactGenerator;
import tech.intellispaces.framework.core.annotation.Data;
import tech.intellispaces.framework.core.annotation.processor.AbstractAnnotationProcessor;
import tech.intellispaces.framework.core.annotation.validator.DataDomainValidator;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;

import javax.annotation.processing.Processor;
import javax.lang.model.element.ElementKind;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@AutoService(Processor.class)
public class DataAnnotationProcessor extends AbstractAnnotationProcessor {

  public DataAnnotationProcessor() {
    super(Data.class, Set.of(ElementKind.INTERFACE));
  }

  @Override
  protected boolean isApplicable(CustomType dataType) {
    return isAutoGenerationEnabled(dataType);
  }

  @Override
  protected AnnotatedTypeValidator getValidator() {
    return new DataDomainValidator();
  }

  @Override
  protected List<ArtifactGenerator> makeArtifactGenerators(CustomType dataType) {
    List<ArtifactGenerator> generators = new ArrayList<>();
    generators.add(new UnmovableDataHandleGenerator(dataType));
    return generators;
  }
}
