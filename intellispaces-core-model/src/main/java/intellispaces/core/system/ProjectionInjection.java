package intellispaces.core.system;

public interface ProjectionInjection extends Injection {

  String name();

  Class<?> targetClass();
}
