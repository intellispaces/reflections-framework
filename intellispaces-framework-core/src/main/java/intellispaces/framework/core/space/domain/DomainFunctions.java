package intellispaces.framework.core.space.domain;

import intellispaces.common.base.exception.UnexpectedViolationException;
import intellispaces.common.base.type.TypeFunctions;
import intellispaces.common.javastatement.JavaStatements;
import intellispaces.common.javastatement.customtype.CustomType;
import intellispaces.common.javastatement.reference.CustomTypeReference;
import intellispaces.common.javastatement.reference.CustomTypeReferences;
import intellispaces.common.javastatement.reference.NamedReference;
import intellispaces.common.javastatement.reference.NotPrimitiveReference;
import intellispaces.common.javastatement.reference.TypeReference;
import intellispaces.common.javastatement.reference.TypeReferenceFunctions;
import intellispaces.framework.core.annotation.Domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

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
      Class<?> wrapperClass = TypeFunctions.getPrimitiveWrapperClass(type.asPrimitiveReferenceOrElseThrow().typename());
      return JavaStatements.customTypeStatement(wrapperClass);
    } else if (type.isCustomTypeReference()) {
      CustomType domainType = type.asCustomTypeReferenceOrElseThrow().targetType();
      if (!isDefaultDomainType(domainType) && !domainType.hasAnnotation(Domain.class)) {
        throw UnexpectedViolationException.withMessage("Unexpected type reference. Class {0} is not domain class",
            domainType.canonicalName());
      }
      return domainType;
    } else {
      throw UnexpectedViolationException.withMessage("Unexpected type reference: {0}", type);
    }
  }

  public static List<CustomType> getDomainTypeAndParents(CustomType domainType) {
    List<CustomType> result = new ArrayList<>();
    result.add(domainType);
    getParentDomains(domainType, result::add);
    return result;
  }

  public static List<CustomType> getParentDomains(CustomType domainType) {
    List<CustomType> parents = new ArrayList<>();
    getParentDomains(domainType, parents::add);
    return parents;
  }

  private static void getParentDomains(CustomType domainType, Consumer<CustomType> consumer) {
    for (CustomTypeReference parent : domainType.parentTypes()) {
      if (DomainFunctions.isDomainType(domainType)) {
        consumer.accept(parent.targetType());
        getParentDomains(parent.targetType(), consumer);
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
          if (!domainTypeParam.extendedBounds().isEmpty()) {
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
  public static Optional<CustomTypeReference> getNearEquivalentDomain(CustomType domain) {
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
  public static Optional<CustomTypeReference> getMainEquivalentDomain(CustomType domain) {
    List<CustomTypeReference> equivalentDomains = getEquivalentDomains(domain);
    if (equivalentDomains.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(equivalentDomains.get(equivalentDomains.size() - 1));
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
}
