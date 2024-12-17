package tech.intellispaces.jaquarius.traverse;

import tech.intellispaces.general.entity.Enumeration;

public enum TraverseTypes implements TraverseType, Enumeration<TraverseType> {

  Mapping,

  Moving,

  MappingOfMoving;


  @Override
  public boolean isMapping() {
    return (this == TraverseTypes.Mapping);
  }

  @Override
  public boolean isMoving() {
    return (this == TraverseTypes.Moving);
  }

  @Override
  public boolean isMappingOfMoving() {
    return (this == TraverseTypes.MappingOfMoving);
  }

  @Override
  public boolean isMovingBased() {
    return (this == TraverseTypes.Moving || this == TraverseTypes.MappingOfMoving);
  }
}
