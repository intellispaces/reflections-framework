package tech.intellispaces.framework.core.guide;

public enum GuideKinds implements GuideKind {

  Mapper0(true, 0),

  Mapper1(true, 1),

  Mapper2(true, 2),

  Mapper3(true, 3),

  Mapper4(true, 4),

  Mapper5(true, 5),

  Mover0(false, 0),

  Mover1(false, 1),

  Mover2(false, 2),

  Mover3(false, 3),

  Mover4(false, 4),

  Mover5(false, 5);

  private final boolean mapper;
  private final int parameterCount;

  GuideKinds(boolean mapper, int parameterCount) {
    this.mapper = mapper;
    this.parameterCount = parameterCount;
  }

  @Override
  public boolean isMapper() {
    return mapper;
  }

  @Override
  public boolean isMover() {
    return !mapper;
  }

  @Override
  public int qualifierCount() {
    return parameterCount;
  }
}
