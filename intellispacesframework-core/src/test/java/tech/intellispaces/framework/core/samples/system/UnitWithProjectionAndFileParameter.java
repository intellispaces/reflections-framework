package tech.intellispaces.framework.core.samples.system;

import tech.intellispaces.framework.core.annotation.Projection;
import tech.intellispaces.framework.core.annotation.Unit;

import java.io.File;

@Unit
public class UnitWithProjectionAndFileParameter {

  @Projection
  String projection(File value) {
    return "";
  }
}
