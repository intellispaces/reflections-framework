package tech.intellispacesframework.core.test.sample.system;

import tech.intellispacesframework.core.annotation.Projection;

import java.io.File;

public class UnitWithProjectionAndFileParameter {

  @Projection
  String projection(File value) {
    return "";
  }
}
