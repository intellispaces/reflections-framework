package tech.intellispaces.reflections.framework.reflection;

import java.util.List;

import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.core.Domain;
import tech.intellispaces.core.Projection;
import tech.intellispaces.core.Rid;
import tech.intellispaces.reflections.framework.channel.Channel1;
import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.traverse.MappingTraverse;

public class NativeForeignReflection implements NativeReflection {
  private final NativeReflection foreignReflection;
  private final Domain domain;

  public NativeForeignReflection(NativeReflection foreignReflection, Domain domain) {
    this.foreignReflection = foreignReflection;
    this.domain = domain;
  }

  @Override
  public Object boundObject() {
    return foreignReflection.boundObject();
  }

  @Override
  public boolean isMovable() {
    return foreignReflection.isMovable();
  }

  @Override
  public MovableReflection asMovableOrElseThrow() {
    throw NotImplementedExceptions.withCode("6IRFdw");
  }

  @Override
  public <TD, T> void addProjection(Class<TD> targetDomain, T target) {
    foreignReflection.addProjection(targetDomain, target);
  }

  @Override
  public List<? extends SystemReflection> underlyingReflections() {
    return foreignReflection.underlyingReflections();
  }

  @Override
  public SystemReflection overlyingReflection() {
    return foreignReflection.overlyingReflection();
  }

  @Override
  public boolean isFocused() {
    return foreignReflection.isFocused();
  }

  @Override
  public <TD, T> T mapTo(Class<TD> targetDomain) {
    return foreignReflection.mapTo(targetDomain);
  }

  @Override
  public <T, Q, C extends Channel1 & MappingTraverse> T mapThru(Class<C> channelClass, Q qualifier) throws TraverseException {
    return foreignReflection.mapThru(channelClass, qualifier);
  }

  @Override
  public void unbind() {
    foreignReflection.unbind();
  }

  @Override
  public Rid rid() {
    return foreignReflection.rid();
  }

  @Override
  public Domain domain() {
    return domain;
  }

  @Override
  public Projection projectThru(Rid cid) {
    throw NotImplementedExceptions.withCode("fQkkyA");
  }
}
