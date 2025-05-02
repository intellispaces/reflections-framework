package samples;

import java.io.File;

import tech.intellispaces.reflectionsj.annotation.Module;
import tech.intellispaces.reflectionsj.annotation.Projection;
import tech.intellispaces.reflectionsj.annotationprocessor.Sample;

@Sample
@Module
public class ModuleWithProjectionAndFileParameter {

  @Projection
  public String projection(File value) {
    return "";
  }
}
