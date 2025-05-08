package samples;

import java.io.File;

import tech.intellispaces.reflections.annotation.Module;
import tech.intellispaces.reflections.annotation.Shutdown;
import tech.intellispaces.reflections.annotation.validator.Sample;

@Sample
@Module
public class ModuleWithShutdownMethodAndFileParameter {

  @Shutdown
  public void shutdown(File value) {
  }
}
