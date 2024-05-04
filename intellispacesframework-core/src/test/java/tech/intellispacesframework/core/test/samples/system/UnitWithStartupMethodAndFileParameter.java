package tech.intellispacesframework.core.test.samples.system;

import tech.intellispacesframework.core.annotation.Startup;

import java.io.File;

public class UnitWithStartupMethodAndFileParameter {

  @Startup
  public void startup(File value) {
  }
}
