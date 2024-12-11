package samples;

import tech.intellispaces.jaquarius.annotation.Module;
import tech.intellispaces.jaquarius.annotation.Projection;
import tech.intellispaces.jaquarius.annotationprocessor.Sample;

import java.io.File;

@Sample
@Module
public class ModuleWithProjectionAndFileParameter {

  @Projection
  public String projection(File value) {
    return "";
  }
}
