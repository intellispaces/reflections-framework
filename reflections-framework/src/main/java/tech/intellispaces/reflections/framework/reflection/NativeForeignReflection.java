package tech.intellispaces.reflections.framework.reflection;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.core.Domain;
import tech.intellispaces.core.Projection;
import tech.intellispaces.core.ReflectionType;
import tech.intellispaces.core.Rid;
import tech.intellispaces.reflections.framework.channel.Channel1;
import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.traverse.MappingTraverse;

public class NativeForeignReflection implements NativeReflection {
  private final NativeReflection foreignReflection;
  private final Rid rid;
  private final Domain domain;

  public NativeForeignReflection(NativeReflection foreignReflection, Domain domain) {
    this(foreignReflection, foreignReflection.rid(), domain);
  }

  public NativeForeignReflection(NativeReflection foreignReflection, Rid rid, Domain domain) {
    this.foreignReflection = foreignReflection;
    this.rid = rid;
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
  public ReflectionType rtype() {
    return foreignReflection.rtype();
  }

  @Override
  public @Nullable Rid rid() {
    return rid;
  }

  @Override
  public @Nullable String rname() {
    return foreignReflection.rname();
  }

  @Override
  public Domain domain() {
    return domain;
  }

  @Override
  public Projection projectThru(Rid cid) {
    throw NotImplementedExceptions.withCode("fQkkyA");
  }

  @Override
  public Projection projectThru(String channelName) {
    throw NotImplementedExceptions.withCode("d9LaxMYX");
  }
}
