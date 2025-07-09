package tech.intellispaces.reflections.framework.space.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jetbrains.annotations.Nullable;

import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.commons.type.ClassFunctions;
import tech.intellispaces.commons.type.Type;
import tech.intellispaces.commons.type.Types;
import tech.intellispaces.core.Domains;
import tech.intellispaces.core.ReflectionDomain;
import tech.intellispaces.core.Rid;
import tech.intellispaces.core.Rids;
import tech.intellispaces.javareflection.JavaStatements;
import tech.intellispaces.javareflection.customtype.CustomType;
import tech.intellispaces.javareflection.customtype.CustomTypes;
import tech.intellispaces.javareflection.method.MethodStatement;
import tech.intellispaces.javareflection.reference.CustomTypeReference;
import tech.intellispaces.javareflection.reference.CustomTypeReferences;
import tech.intellispaces.javareflection.reference.NamedReference;
import tech.intellispaces.javareflection.reference.NotPrimitiveReference;
import tech.intellispaces.javareflection.reference.TypeReference;
import tech.intellispaces.javareflection.reference.TypeReferenceFunctions;
import tech.intellispaces.reflections.framework.annotation.Domain;
import tech.intellispaces.reflections.framework.naming.NameConventionFunctions;
import tech.intellispaces.reflections.framework.node.ReflectionsNodeFunctions;
import tech.intellispaces.reflections.framework.settings.ChannelAssignments;
import tech.intellispaces.reflections.framework.settings.DomainReference;
import tech.intellispaces.reflections.framework.space.channel.ChannelFunctions;

/**
 * ReflectionDomain functions.
 */
public final class DomainFunctions {

  public static ReflectionDomain getDomain(Class<?> domainClass) {
    return Domains.create(
        getDomainId(domainClass),
        getDomainName(domainClass),
        domainClass,
        Types.get(domainClass)
    );
  }

  public static ReflectionDomain getDomain(CustomType domainType) {
    return Domains.create(
        getDomainId(domainType),
        getDomainName(domainType),
        null,
        null
    );
  }

  public static Rid getDomainId(CustomType domainType) {
    DomainReference domainReference = ReflectionsNodeFunctions
        .ontologyReference()
        .getDomainByDelegateClass(domainType.canonicalName());
    if (domainReference != null) {
      return null;
    }
    return Rids.create(domainType.selectAnnotation(Domain.class).orElseThrow().value());
  }

  public static Rid getDomainId(Class<?> domainClass) {
    return Rids.create(domainClass.getAnnotation(Domain.class).value());
  }

  public static String getDomainName(CustomType domainType) {
    return domainType.selectAnnotation(Domain.class).orElseThrow().name();
  }

  public static String getDomainName(Class<?> domainClass) {
    return domainClass.getAnnotation(Domain.class).name();
  }

  public static boolean isDomainClass(Class<?> aClass) {
    return aClass.isAnnotationPresent(Domain.class);
  }

  public static boolean isDomainType(TypeReference type) {
    return type.isCustomTypeReference() && type.asCustomTypeReferenceOrElseThrow().targetType().hasAnnotation(Domain.class);
  }

  public static boolean isDomainType(CustomType type) {
    return type.hasAnnotation(Domain.class) || isDefaultDomainType(type);
  }

  public static CustomType getDomainType(TypeReference type) {
    if (type.isPrimitiveReference()) {
      Class<?> wrapperClass = ClassFunctions.wrapperClassOfPrimitive(type.asPrimitiveReferenceOrElseThrow().typename());
      return JavaStatements.customTypeStatement(wrapperClass);
    } else if (type.isCustomTypeReference()) {
      CustomType domainType = type.asCustomTypeReferenceOrElseThrow().targetType();
      if (!isDefaultDomainType(domainType) && !domainType.hasAnnotation(Domain.class)) {
        throw UnexpectedExceptions.withMessage("Unexpected type reference. Class {0} is not domain class",
            domainType.canonicalName());
      }
      return domainType;
    } else {
      throw UnexpectedExceptions.withMessage("Unexpected type reference: {0}", type);
    }
  }

  public static List<CustomType> getDomainTypeAndParents(CustomType domainType) {
    List<CustomType> result = new ArrayList<>();
    result.add(domainType);
    allParentDomains(domainType, result::add);
    return result;
  }

  public static List<CustomType> allParentDomains(CustomType domainType) {
    List<CustomType> parents = new ArrayList<>();
    allParentDomains(domainType, parents::add);
    return parents;
  }

  private static void allParentDomains(CustomType domainType, Consumer<CustomType> consumer) {
    for (CustomTypeReference parent : domainType.parentTypes()) {
      if (DomainFunctions.isDomainType(domainType)) {
        consumer.accept(parent.targetType());
        allParentDomains(parent.targetType(), consumer);
      }
    }
  }

  public static boolean isAliasOf(CustomTypeReference testAliasDomain, CustomType domain) {
    List<CustomTypeReference> equivalentDomains = getEquivalentDomains(domain);
    for (CustomTypeReference equivalentDomain : equivalentDomains) {
      if (TypeReferenceFunctions.isEqualTypes(testAliasDomain, equivalentDomain)) {
        return true;
      }
    }
    return false;
  }

  public static boolean hasEquivalentDomains(CustomType domain) {
    return !getEquivalentDomains(domain).isEmpty();
  }

  public static List<CustomTypeReference> getEquivalentDomains(CustomType domain) {
    if (domain.parentTypes().isEmpty()) {
      return List.of();
    }
    List<CustomTypeReference> aliases = new ArrayList<>();
    addEquivalentDomains(domain, aliases, List.of());
    return aliases;
  }

  private static void addEquivalentDomains(
      CustomType domain, List<CustomTypeReference> aliases, List<NotPrimitiveReference> arguments
  ) {
    if (domain.parentTypes().isEmpty()) {
      return;
    }

    Map<String, NamedReference> aliasDomainTypeParameters = domain.typeParameterMap();
    for (CustomTypeReference parent : domain.parentTypes()) {
      List<NotPrimitiveReference> aliasTypeArguments = new ArrayList<>();
      boolean isAlias = false;
      for (NotPrimitiveReference typeArgument : parent.typeArguments()) {
        if (typeArgument.isCustomTypeReference()) {
          isAlias = true;
          aliasTypeArguments.add(typeArgument);
        } else if (typeArgument.isNamedReference()) {
          String typeArgumentName = typeArgument.asNamedReferenceOrElseThrow().name();
          NamedReference domainTypeParam = aliasDomainTypeParameters.get(typeArgumentName);
          if (domainTypeParam != null && !domainTypeParam.extendedBounds().isEmpty()) {
            isAlias = true;

            if (arguments.isEmpty()) {
              aliasTypeArguments.add(domainTypeParam);
            } else {
              int index = 0;
              for (NamedReference domainTypeParam2 : domain.typeParameters()) {
                if (domainTypeParam2.name().equals(typeArgumentName)) {
                  aliasTypeArguments.add(arguments.get(index));
                  break;
                }
                index++;
              }
            }
          }
        }
      }
      if (isAlias) {
        aliases.add(CustomTypeReferences.get(parent.targetType(), aliasTypeArguments));
        addEquivalentDomains(parent.targetType(), aliases, aliasTypeArguments);
      }
    }
  }

  /**
   * Returns near equivalent domain definition of domain alias.
   *
   * @param domain the domain.
   * @return near equivalent domain definition or empty optional when domain is not alias.
   */
  public static Optional<CustomTypeReference> getAliasNearNeighbourDomain(CustomType domain) {
    List<CustomTypeReference> equivalentDomains = getEquivalentDomains(domain);
    if (equivalentDomains.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(equivalentDomains.get(0));
  }

  /**
   * Returns primary equivalent domain definition of domain alias.
   *
   * @param domain the domain.
   * @return primary equivalent domain definition or empty optional when domain is not alias.
   */
  public static Optional<CustomTypeReference> getAliasBaseDomain(CustomType domain) {
    List<CustomTypeReference> equivalentDomains = getEquivalentDomains(domain);
    if (equivalentDomains.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(equivalentDomains.get(equivalentDomains.size() - 1));
  }

  public static boolean isNotDomainClassGetter(MethodStatement method) {
    return !method.name().equals("domainClass") &&
        !method.name().equals(ReflectionsNodeFunctions.ontologyReference().getChannelByType(ChannelAssignments.PointToDomain).alias());
  }

  public static boolean isDefaultDomainType(CustomType type) {
    return DEFAULT_DOMAIN_CLASSES.contains(type.canonicalName());
  }

  public static String buildConversionMethodsChain(CustomType sourceDomain, CustomType targetDomain) {
    return buildConversionMethodsChain("", sourceDomain, targetDomain);
  }

  private static String buildConversionMethodsChain(
      String prevChain, CustomType sourceDomain, CustomType targetDomain
  ) {
    if (sourceDomain.canonicalName().equals(targetDomain.canonicalName())) {
      return prevChain;
    }

    for (CustomTypeReference superDomain : sourceDomain.parentTypes()) {
      String chain = isAliasOf(superDomain, sourceDomain)
          ? prevChain
          : prevChain + "." + NameConventionFunctions.getConversionMethodName(superDomain.targetType()) + "()";
      String next = buildConversionMethodsChain(chain, superDomain.targetType(), targetDomain);
      if (next != null) {
        return next;
      }
    }
    return null;
  }

  public static Collection<CustomTypeReference> getEffectiveSuperDomains(CustomType domain) {
    var superDomains = new ArrayList<CustomTypeReference>();
    getEffectiveSuperDomains(domain, domain, superDomains);
    return filterSuperDomains(superDomains);
  }

  private static void getEffectiveSuperDomains(
      CustomType customType, CustomType effectiveCustomType, List<CustomTypeReference> superDomains
  ) {
    Iterator<CustomTypeReference> parents = customType.parentTypes().iterator();
    Iterator<CustomTypeReference> effectiveParents = effectiveCustomType.parentTypes().iterator();
    while (parents.hasNext() && effectiveParents.hasNext()) {
      CustomTypeReference parent = parents.next();
      CustomTypeReference effectiveParent = effectiveParents.next();
      if (!DomainFunctions.isAliasOf(parent, customType)) {
        superDomains.add(effectiveParent);
      }
      getEffectiveSuperDomains(parent.targetType(), effectiveParent.effectiveTargetType(), superDomains);
    }
  }

  private static Collection<CustomTypeReference> filterSuperDomains(ArrayList<CustomTypeReference> superDomains) {
    Map<String, CustomTypeReference> superDomainNames = superDomains.stream()
        .collect(Collectors.toMap(
            d -> d.targetType().canonicalName(),
            Function.identity(),
            (v1, v2) -> v1
        ));
    return superDomainNames.values();
  }

  public static @Nullable Rid findChannel(CustomType sourceDomain, Rid targetDomainRid) {
    for (MethodStatement method : sourceDomain.actualMethods()) {
      if (ChannelFunctions.isChannelMethod(method)) {
        Rid did = getDomainId(method.returnType().orElseThrow().asCustomTypeReferenceOrElseThrow().targetType());
        if (Objects.equals(did, targetDomainRid)) {
          return ChannelFunctions.getChannelId(method);
        }
      }
    }
    return null;
  }

  public static int getDomainProjectionChannelsExcludeConversionMethodsCount(Class<?> domainClass) {
    return (int) CustomTypes.of(domainClass)
        .actualMethods().stream()
        .filter(m -> !NameConventionFunctions.isConversionMethod(m))
        .count();
  }

  private final static Set<String> DEFAULT_DOMAIN_CLASSES = Set.of(
      Type.class.getCanonicalName(),
      Object.class.getCanonicalName(),
      Boolean.class.getCanonicalName(),
      Number.class.getCanonicalName(),
      Byte.class.getCanonicalName(),
      Short.class.getCanonicalName(),
      Integer.class.getCanonicalName(),
      Long.class.getCanonicalName(),
      Float.class.getCanonicalName(),
      Double.class.getCanonicalName(),
      Character.class.getCanonicalName(),
      String.class.getCanonicalName(),
      Class.class.getCanonicalName(),
      Void.class.getCanonicalName()
  );

  private DomainFunctions() {}
}
