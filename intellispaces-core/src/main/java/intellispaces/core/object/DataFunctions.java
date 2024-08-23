package intellispaces.core.object;

import intellispaces.core.annotation.Data;

public interface DataFunctions {

  static boolean isDataObjectHandle(Class<?> objectHandleClass) {
    return isDataDomain(ObjectFunctions.getDomainClassOfObjectHandle(objectHandleClass));
  }

  static boolean isDataDomain(Class<?> domainClass) {
    return domainClass.isAnnotationPresent(Data.class);
  }
}
