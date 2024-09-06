package intellispaces.framework.core.system.projection;

import intellispaces.framework.core.system.ProjectionReference;

class ProjectionReferenceImpl implements ProjectionReference {
  private final String name;
  private final Class<?> targetClass;

  ProjectionReferenceImpl(String name, Class<?> targetClass) {
    this.name = name;
    this.targetClass = targetClass;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public Class<?> targetClass() {
    return targetClass;
  }
}
