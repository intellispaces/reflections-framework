package tech.intellispacesframework.core.test.sample.system;

import tech.intellispacesframework.core.annotation.Projection;

public class UnitWithProjectionAndStringParameter {

  @Projection
  String projection(String value) {
    return "";
  }
}
