package tech.intellispaces.core.system;

class SystemProjectionImpl implements SystemProjection {
  private final String name;
  private final Class<?> targetClass;
  private final Object target;
  private final ProjectionDefinition provider;

  public SystemProjectionImpl(String name, Class<?> targetClass, ProjectionDefinition provider, Object target) {
    this.name = name;
    this.targetClass = targetClass;
    this.provider = provider;
    this.target = target;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public Class<?> targetClass() {
    return targetClass;
  }

  @Override
  public ProjectionDefinition provider() {
    return provider;
  }

  @Override
  public Object target() {
    return target;
  }
}
