package tech.intellispaces.core.space;

import tech.intellispaces.commons.exception.UnexpectedViolationException;
import tech.intellispaces.commons.type.TypeFunctions;

public abstract class SpaceConstants {

  private SpaceConstants() {}

  public static final String POINT_DOMAIN_NAME = "intellispaces.ixora.core.Point";

  public static final String POINT_TO_BASE_DOMAIN_TRANSITION_SIMPLE_NAME = "baseDomain";

  public static final String STRING_TO_PROPERTIES_TID = "1830e924-2c78-4f18-b9eb-39f002cbeea6";

  public static final String PROPERTIES_TO_DATA_TID = "cafc0a2e-a1da-44e1-997f-2db62f7e385c";

  public static final String PROPERTIES_TO_VALUE_TID = "ea111b97-c025-4d74-8466-d462c4c87efd";

  public static final String PROPERTIES_DOMAIN_NAME = "intellispaces.ixora.structures.properties.Properties";

  public static final String PROPERTIES_HANDLE_CLASSNAME = "intellispaces.ixora.structures.properties.PropertiesHandle";

  public static Class<?> propertiesHandleClass() {
    if (propertiesHandleClass == null) {
      propertiesHandleClass = TypeFunctions.getClass(PROPERTIES_HANDLE_CLASSNAME).orElseThrow(
          () -> UnexpectedViolationException.withMessage("Could not get class {}", PROPERTIES_HANDLE_CLASSNAME)
      );
    }
    return propertiesHandleClass;
  }

  private static Class<?> propertiesHandleClass;
}
