package samples;

import tech.intellispaces.framework.core.annotation.Module;
import tech.intellispaces.framework.core.annotation.Shutdown;
import tech.intellispaces.framework.core.annotation.validator.Sample;

import java.io.File;

@Sample
@Module
public class ModuleWithShutdownMethodAndFileParameter {

  @Shutdown
  public void shutdown(File value) {
  }
}
