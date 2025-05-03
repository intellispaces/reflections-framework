package samples;

import java.io.File;

import tech.intellispaces.reflectionsj.annotation.Module;
import tech.intellispaces.reflectionsj.annotation.Shutdown;
import tech.intellispaces.reflectionsj.annotationprocessor.Sample;

@Sample
@Module
public class ModuleWithShutdownMethodAndFileParameter {

  @Shutdown
  public void shutdown(File value) {
  }
}
