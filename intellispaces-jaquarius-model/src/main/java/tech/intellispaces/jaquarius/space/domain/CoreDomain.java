package tech.intellispaces.jaquarius.space.domain;

import tech.intellispaces.general.entity.Enumerable;

public interface CoreDomain extends Enumerable<CoreDomain> {

  String className();
}
