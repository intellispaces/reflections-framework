package tech.intellispaces.reflections.framework.space.channel;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.core.Projection;
import tech.intellispaces.core.Reflection;
import tech.intellispaces.core.ReflectionChannel;
import tech.intellispaces.core.ReflectionDomain;
import tech.intellispaces.core.ReflectionPoint;
import tech.intellispaces.core.ReflectionSpace;
import tech.intellispaces.core.Rid;
import tech.intellispaces.javareflection.method.MethodStatement;

public class MethodReflectionChannel implements ReflectionChannel, ReflectionPoint {
  private final Class<?> domainClass;
  private final MethodStatement channelMethod;

  public MethodReflectionChannel(Class<?> domainClass, MethodStatement channelMethod) {
    this.domainClass = domainClass;
    this.channelMethod = channelMethod;
  }

  @Override
  public String alias() {
    return channelMethod.name();
  }

  @Override
  public ReflectionDomain sourceDomain() {
    throw NotImplementedExceptions.withCode("iXuL0goc");
  }

  @Override
  public ReflectionDomain targetDomain() {
    throw NotImplementedExceptions.withCode("8wugYrjU");
  }

  @Override
  public ReflectionDomain domain() {
    throw NotImplementedExceptions.withCode("eQoyLpBu");
  }

  @Override
  public @Nullable String domainAlias() {
    return domain().alias();
  }

  @Override
  public List<ReflectionPoint> parentPoints() {
    return List.of();
  }

  @Override
  public Projection projectionThru(Rid cid) {
    throw NotImplementedExceptions.withCode("r5IcakYg");
  }

  @Override
  public Projection projectionThru(String channelName) {
    throw NotImplementedExceptions.withCode("Jnhj7d2L");
  }

  @Override
  public Projection projectionTo(String domainName) {
    throw NotImplementedExceptions.withCode("92WN572d");
  }

  @Override
  public Projection projectionTo(ReflectionDomain domain) {
    throw NotImplementedExceptions.withCode("NP0POsKp");
  }

  @Override
  public List<Reflection> relatedReflections() {
    return List.of();
  }

  @Override
  public boolean canBeRepresentedAsPoint() {
    return true;
  }

  @Override
  public boolean canBeRepresentedAsDomain() {
    return false;
  }

  @Override
  public boolean canBeRepresentedAsChannel() {
    return true;
  }

  @Override
  public boolean canBeRepresentedAsSpace() {
    return false;
  }

  @Override
  public ReflectionPoint asPoint() {
    return this;
  }

  @Override
  public ReflectionDomain asDomain() {
    throw UnexpectedExceptions.withMessage("This reflection cannot be considered as a reflection domain");
  }

  @Override
  public ReflectionChannel asChannel() {
    return this;
  }

  @Override
  public ReflectionSpace asSpace() {
    throw UnexpectedExceptions.withMessage("This reflection cannot be considered as a reflection space");
  }

  @Override
  public @Nullable Rid rid() {
    return ChannelFunctions.getChannelId(channelMethod);
  }

  @Override
  public @Nullable String contextAlias() {
    throw NotImplementedExceptions.withCode("iZ8WTTH1");
  }
}
