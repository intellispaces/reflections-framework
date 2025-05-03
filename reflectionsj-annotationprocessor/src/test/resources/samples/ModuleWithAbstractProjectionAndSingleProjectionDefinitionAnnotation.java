package samples;

import tech.intellispaces.reflectionsj.annotation.Module;
import tech.intellispaces.reflectionsj.annotation.Projection;
import tech.intellispaces.reflectionsj.annotation.Properties;
import tech.intellispaces.reflectionsj.annotationprocessor.Sample;

@Sample
@Module
public abstract class ModuleWithAbstractProjectionAndSingleProjectionDefinitionAnnotation {

  @Projection
  @Properties
  public abstract String projection();
}
