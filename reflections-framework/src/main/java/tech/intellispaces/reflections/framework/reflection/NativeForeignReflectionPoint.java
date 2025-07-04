package tech.intellispaces.reflections.framework.reflection;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.core.Projection;
import tech.intellispaces.core.Reflection;
import tech.intellispaces.core.ReflectionChannel;
import tech.intellispaces.core.ReflectionDomain;
import tech.intellispaces.core.ReflectionPoint;
import tech.intellispaces.core.ReflectionSpace;
import tech.intellispaces.core.Rid;
import tech.intellispaces.core.TraversableReflectionPoint;
import tech.intellispaces.reflections.framework.channel.Channel1;
import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.traverse.MappingTraverse;

public class NativeForeignReflectionPoint implements NativeReflectionPoint, TraversableReflectionPoint {
  private final NativeReflectionPoint foreignPoint;
  private final Rid rid;
  private final ReflectionDomain domain;

  public NativeForeignReflectionPoint(NativeReflectionPoint foreignPoint) {
    this(foreignPoint, foreignPoint.rid(), foreignPoint.domain());
  }

  public NativeForeignReflectionPoint(NativeReflectionPoint foreignPoint, Rid rid) {
    this(foreignPoint, rid, foreignPoint.domain());
  }

  public NativeForeignReflectionPoint(NativeReflectionPoint foreignPoint, ReflectionDomain domain) {
    this(foreignPoint, foreignPoint.rid(), domain);
  }

  public NativeForeignReflectionPoint(NativeReflectionPoint foreignPoint, Rid rid, ReflectionDomain domain) {
    this.foreignPoint = foreignPoint;
    this.rid = rid;
    this.domain = domain;
  }

  @Override
  public Object boundObject() {
    return foreignPoint.boundObject();
  }

  @Override
  public boolean isMovable() {
    return foreignPoint.isMovable();
  }

  @Override
  public MovableReflection asMovableOrElseThrow() {
    throw NotImplementedExceptions.withCode("6IRFdw");
  }

  @Override
  public <TD, T> void addProjection(Class<TD> targetDomain, T target) {
    foreignPoint.addProjection(targetDomain, target);
  }

  @Override
  public List<? extends SystemReflection> underlyingReflections() {
    return foreignPoint.underlyingReflections();
  }

  @Override
  public SystemReflection overlyingReflection() {
    return foreignPoint.overlyingReflection();
  }

  @Override
  public <TD, T> T mapTo(Class<TD> targetDomain) {
    return foreignPoint.mapTo(targetDomain);
  }

  @Override
  public <T, Q, C extends Channel1 & MappingTraverse> T mapThru(Class<C> channelClass, Q qualifier) throws TraverseException {
    return foreignPoint.mapThru(channelClass, qualifier);
  }

  @Override
  public void unbind() {
    foreignPoint.unbind();
  }

  @Override
  public @Nullable Rid rid() {
    return rid;
  }

  @Override
  public @Nullable String reflectionName() {
    return foreignPoint.reflectionName();
  }

  @Override
  public ReflectionDomain domain() {
    return domain;
  }

  @Override
  public Projection projectionThru(Rid cid) {
    return foreignPoint.projectionThru(cid);
  }

  @Override
  public Projection projectionThru(String channelName) {
    return foreignPoint.projectionThru(channelName);
  }

  @Override
  public Projection projectionTo(String domainName) {
    return foreignPoint.projectionTo(domainName);
  }

  @Override
  public Projection projectionTo(ReflectionDomain domain) {
    return foreignPoint.projectionTo(domain);
  }

  @Override
  public List<Reflection> relatedReflections() {
    throw NotImplementedExceptions.withCode("Ltk71t3E");
  }

  @Override
  public List<ReflectionPoint> underlyingPoints() {
    return foreignPoint.underlyingPoints();
  }

  @Override
  public boolean canBeRepresentedAsPoint() {
    return true;
  }

  @Override
  public boolean canBeRepresentedAsDomain() {
    throw NotImplementedExceptions.withCode("rnVuR6Qi");
  }

  @Override
  public boolean canBeRepresentedAsChannel() {
    throw NotImplementedExceptions.withCode("0yncwMOM");
  }

  @Override
  public boolean canBeRepresentedAsSpace() {
    throw NotImplementedExceptions.withCode("5wTJ2NCS");
  }

  @Override
  public ReflectionPoint asPoint() {
    return this;
  }

  @Override
  public ReflectionDomain asDomain() {
    throw NotImplementedExceptions.withCode("AjIDY5Bz");
  }

  @Override
  public ReflectionChannel asChannel() {
    throw NotImplementedExceptions.withCode("32Pyrfch");
  }

  @Override
  public ReflectionSpace asSpace() {
    throw NotImplementedExceptions.withCode("olslVCkn");
  }
}
