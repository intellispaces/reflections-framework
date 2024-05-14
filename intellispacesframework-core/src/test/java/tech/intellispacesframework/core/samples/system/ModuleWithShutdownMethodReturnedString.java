package tech.intellispacesframework.core.samples.system;

import tech.intellispacesframework.core.annotation.Module;
import tech.intellispacesframework.core.annotation.Shutdown;

@Module
public class ModuleWithShutdownMethodReturnedString {

  @Shutdown
  public String shutdown() {
    return "";
  }
}
