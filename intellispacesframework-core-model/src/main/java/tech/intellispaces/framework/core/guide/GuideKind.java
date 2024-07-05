package tech.intellispaces.framework.core.guide;

public interface GuideKind {

  String name();

  boolean isMapper();

  boolean isMover();

  int qualifierCount();
}
