package tech.intellispacesframework.core.test.samples.system;

import tech.intellispacesframework.core.annotation.Projection;
import tech.intellispacesframework.core.annotation.Unit;

import java.io.File;

@Unit
public class UnitWithProjectionAndFileParameter {

  @Projection
  String projection(File value) {
    return "";
  }
}
