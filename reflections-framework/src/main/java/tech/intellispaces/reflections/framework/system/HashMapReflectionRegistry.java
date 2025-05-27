package tech.intellispaces.reflections.framework.system;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import tech.intellispaces.core.Reflection;
import tech.intellispaces.core.Reflections;
import tech.intellispaces.core.Rid;
import tech.intellispaces.core.Rids;

public class HashMapReflectionRegistry implements ReflectionRegistry {
  private final Map<Rid, Reflection> map = new HashMap<>();

  @Override
  public Reflection register(Reflection reflection) {
    Rid rid = Rids.of(UUID.randomUUID());
    Reflection registredReflection = Reflections.registered(reflection, rid);
    map.put(rid, registredReflection);
    return registredReflection;
  }
}
