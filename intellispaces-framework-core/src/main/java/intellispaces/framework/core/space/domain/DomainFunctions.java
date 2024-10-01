package intellispaces.framework.core.space.domain;

import intellispaces.common.base.exception.UnexpectedViolationException;
import intellispaces.common.base.type.TypeFunctions;
import intellispaces.common.javastatement.JavaStatements;
import intellispaces.common.javastatement.customtype.CustomType;
import intellispaces.common.javastatement.reference.CustomTypeReference;
import intellispaces.common.javastatement.reference.NotPrimitiveReference;
import intellispaces.common.javastatement.reference.ReferenceBound;
import intellispaces.common.javastatement.reference.TypeReference;
import intellispaces.framework.core.annotation.Domain;

import java.util.ArrayList;
import java.util.List;
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

  public static boolean isAliasDomain(CustomType domain) {
    return getPrimaryDomainOfAlias(domain).isPresent();
  }

  public static Optional<CustomTypeReference> getPrimaryDomainOfAlias(CustomType domain) {
    List<CustomTypeReference> parents = domain.parentTypes();
    if (parents.size() != 1) {
      return Optional.empty();
    }
    CustomTypeReference parentDomainType = parents.get(0);
    List<NotPrimitiveReference> parentTypeArguments = parentDomainType.typeArguments();
    if (parentTypeArguments.isEmpty()) {
      return Optional.empty();
    }
    boolean allTypeArgumentsAreCustomTypeRelated = parentTypeArguments.stream()
        .allMatch(DomainFunctions::isCustomTypeRelated);
    if (!allTypeArgumentsAreCustomTypeRelated) {
      return Optional.empty();
    }
    return Optional.of(parentDomainType);
  }

  public static Optional<CustomTypeReference> getMainPrimaryDomainOfAlias(CustomType domain) {
    List<CustomTypeReference> primaryDomains = getAllPrimaryDomainOfAlias(domain);
    if (primaryDomains.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(primaryDomains.get(primaryDomains.size() - 1));
  }

  public static List<CustomTypeReference> getAllPrimaryDomainOfAlias(CustomType domain) {
    List<CustomTypeReference> result = new ArrayList<>();
    Optional<CustomTypeReference> primaryDomain = getPrimaryDomainOfAlias(domain);
    while (primaryDomain.isPresent()) {
      result.add(primaryDomain.get());
      Optional<CustomTypeReference> nextPrimaryDomain = getPrimaryDomainOfAlias(primaryDomain.get().targetType());
      if (nextPrimaryDomain.isEmpty()) {
        break;
      }
      primaryDomain = nextPrimaryDomain;
    }
    return result;
  }

  private static boolean isCustomTypeRelated(TypeReference type) {
    if (type.isCustomTypeReference()) {
      return true;
    }
    if (type.isNamedReference()) {
      List<ReferenceBound> extendedBounds = type.asNamedReferenceOrElseThrow().extendedBounds();
      if (extendedBounds.size() == 1 && extendedBounds.get(0).isCustomTypeReference()) {
        return true;
      }
    }
    return false;
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
