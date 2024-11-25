package samples;

import tech.intellispaces.jaquarius.annotation.Module;
import tech.intellispaces.jaquarius.annotation.Shutdown;
import tech.intellispaces.jaquarius.annotation.validator.Sample;

import java.io.File;

@Sample
@Module
public class ModuleWithShutdownMethodAndFileParameter {

  @Shutdown
  public void shutdown(File value) {
  }
}
