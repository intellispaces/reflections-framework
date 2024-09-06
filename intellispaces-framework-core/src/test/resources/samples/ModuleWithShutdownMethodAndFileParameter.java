package samples;

import intellispaces.framework.core.annotation.Module;
import intellispaces.framework.core.annotation.Shutdown;
import intellispaces.framework.core.annotation.validator.Sample;

import java.io.File;

@Sample
@Module
public class ModuleWithShutdownMethodAndFileParameter {

  @Shutdown
  public void shutdown(File value) {
  }
}
