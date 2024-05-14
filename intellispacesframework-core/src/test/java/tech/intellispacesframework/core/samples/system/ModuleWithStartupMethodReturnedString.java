package tech.intellispacesframework.core.samples.system;

import tech.intellispacesframework.core.annotation.Module;
import tech.intellispacesframework.core.annotation.Startup;

@Module
public class ModuleWithStartupMethodReturnedString {

  @Startup
  public String startup() {
    return "";
  }
}
