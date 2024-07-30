package samples;

import tech.intellispaces.core.annotation.Module;
import tech.intellispaces.core.annotation.Shutdown;
import tech.intellispaces.core.annotation.validator.Sample;

import java.io.File;

@Sample
@Module
public class ModuleWithShutdownMethodAndFileParameter {

  @Shutdown
  public void shutdown(File value) {
  }
}
