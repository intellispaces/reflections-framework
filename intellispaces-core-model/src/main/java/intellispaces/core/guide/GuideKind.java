package intellispaces.core.guide;

public interface GuideKind {

  String name();

  boolean isMapper();

  boolean isMover();

  int guideOrder();
}
