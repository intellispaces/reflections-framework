package tech.intellispaces.jaquarius.guide;

public enum GuideKinds implements GuideKind {

  Mapper0(true, false, false, 0),

  Mapper1(true, false, false, 1),

  Mapper2(true, false, false, 2),

  Mapper3(true, false, false, 3),

  Mapper4(true, false, false, 4),

  Mapper5(true, false, false, 5),

  Mover0(false, true, false, 0),

  Mover1(false, true, false, 1),

  Mover2(false, true, false, 2),

  Mover3(false, true, false, 3),

  Mover4(false, true, false, 4),

  Mover5(false, true, false, 5),

  MapperOfMoving0(true, false, true, 0),

  MapperOfMoving1(true, false, true, 1),

  MapperOfMoving2(true, false, true, 2),

  MapperOfMoving3(true, false, true, 3),

  MapperOfMoving4(true, false, true, 4),

  MapperOfMoving5(true, false, true, 5);


  private final boolean mapper;
  private final boolean mover;
  private final boolean mapperOfMoving;
  private final int order;

  GuideKinds(boolean mapper, boolean mover, boolean mapperOfMoving, int order) {
    this.mapper = mapper;
    this.mover = mover;
    this.mapperOfMoving = mapperOfMoving;
    this.order = order;
  }

  @Override
  public boolean isMapper() {
    return mapper;
  }

  @Override
  public boolean isMover() {
    return mover;
  }

  @Override
  public boolean isMapperOfMoving() {
    return mapperOfMoving;
  }

  @Override
  public boolean isMovingBased() {
    return mover || mapperOfMoving;
  }

  @Override
  public int order() {
    return order;
  }
}
