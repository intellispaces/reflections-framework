package tech.intellispaces.jaquarius.space.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.commons.type.ClassFunctions;
import tech.intellispaces.jaquarius.Jaquarius;
import tech.intellispaces.jaquarius.annotation.Domain;
import tech.intellispaces.jaquarius.naming.NameConventionFunctions;
import tech.intellispaces.jaquarius.settings.ChannelTypes;
import tech.intellispaces.statementsj.JavaStatements;
import tech.intellispaces.statementsj.customtype.CustomType;
import tech.intellispaces.statementsj.method.MethodStatement;
import tech.intellispaces.statementsj.reference.CustomTypeReference;
import tech.intellispaces.statementsj.reference.CustomTypeReferences;
import tech.intellispaces.statementsj.reference.NamedReference;
import tech.intellispaces.statementsj.reference.NotPrimitiveReference;
import tech.intellispaces.statementsj.reference.TypeReference;
import tech.intellispaces.statementsj.reference.TypeReferenceFunctions;

/**
 * Domain functions.
 */
public final class DomainFunctions {

  private DomainFunctions() {}

  public static String getDomainId(CustomType domain) {
    return domain.selectAnnotation(Domain.class).orElseThrow().value();
  }

  public static boolean isDomainClass(Class<?> aClass) {
    return aClass.isAnnotationPresent(Domain.class);
  }

  public static boolean isDomainType(TypeReference type) {
    return type.isCustomTypeReference() && type.asCustomTypeReferenceOrElseThrow().targetType().hasAnnotation(Domain.class);
  }

  public static boolean isDomainType(CustomType type) {
    return type.hasAnnotation(Domain.class);
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
        !method.name().equals(Jaquarius.ontologyReference().getChannelByType(ChannelTypes.PointToDomain).alias());
  }

  public static boolean isDefaultDomainType(CustomType type) {
    return DEFAULT_DOMAIN_CLASSES.contains(type.canonicalName());
  }

  private final static Set<String> DEFAULT_DOMAIN_CLASSES = Set.of(
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
}
