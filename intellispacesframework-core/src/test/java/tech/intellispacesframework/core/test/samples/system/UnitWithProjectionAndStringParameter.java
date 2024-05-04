package tech.intellispacesframework.core.test.samples.system;

import tech.intellispacesframework.core.annotation.Projection;

public class UnitWithProjectionAndStringParameter {

  @Projection
  String projection(String value) {
    return "";
  }
}
