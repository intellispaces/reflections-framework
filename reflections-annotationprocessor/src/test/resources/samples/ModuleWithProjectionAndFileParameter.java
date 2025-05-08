package samples;

import java.io.File;

import tech.intellispaces.reflections.annotation.Module;
import tech.intellispaces.reflections.annotation.Projection;
import tech.intellispaces.reflections.annotationprocessor.Sample;

@Sample
@Module
public class ModuleWithProjectionAndFileParameter {

  @Projection
  public String projection(File value) {
    return "";
  }
}
