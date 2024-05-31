package tech.intellispaces.framework.core.samples.system;

import tech.intellispaces.framework.core.annotation.Projection;
import tech.intellispaces.framework.core.annotation.Unit;

@Unit
public class UnitWithProjectionAndStringParameter {

  @Projection
  String projection(String value) {
    return "";
  }
}
