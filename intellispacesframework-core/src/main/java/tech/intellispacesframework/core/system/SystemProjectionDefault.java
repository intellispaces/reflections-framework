package tech.intellispacesframework.core.system;

class SystemProjectionDefault implements SystemProjection {
  private final String name;
  private final Class<?> targetClass;
  private final Object target;
  private final ProjectionProvider provider;

  public SystemProjectionDefault(String name, Class<?> targetClass, ProjectionProvider provider, Object target) {
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
  public ProjectionProvider provider() {
    return provider;
  }

  @Override
  public Object target() {
    return target;
  }
}
