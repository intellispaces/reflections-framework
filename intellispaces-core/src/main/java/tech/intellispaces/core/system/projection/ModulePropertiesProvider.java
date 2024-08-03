package tech.intellispaces.core.system.projection;

import tech.intellispaces.commons.exception.UnexpectedViolationException;
import tech.intellispaces.core.annotation.Properties;
import tech.intellispaces.core.object.ObjectFunctions;
import tech.intellispaces.core.object.ObjectHandleConstants;
import tech.intellispaces.core.space.SpaceConstants;
import tech.intellispaces.core.system.Module;
import tech.intellispaces.core.system.ModulePropertiesFunctions;
import tech.intellispaces.core.system.Modules;

import java.lang.reflect.Method;

public class ModulePropertiesProvider extends AbstractProjectionProvider {

  public ModulePropertiesProvider(Method projectionMethod) {
    super(projectionMethod);
  }

  @Override
  public Object get() {
    Module module = Modules.activeModule();
    Properties annotation = projectionMethod.getAnnotation(Properties.class);
    String rawProperties = ModulePropertiesFunctions.getProperties(module, null);
    Object parsedProperties = module.mapThruTransition0(rawProperties, SpaceConstants.STRING_TO_PROPERTIES_TID);

    Object target = module.mapThruTransition1(
        parsedProperties, SpaceConstants.PROPERTIES_TO_VALUE_TID, annotation.value());

    Class<?> expectedReturnType = projectionMethod.getReturnType();
    if (target.getClass() == expectedReturnType) {
      return target;
    }
    if (ObjectHandleConstants.PROPERTIES_HANDLE_CLASSNAME.equals(expectedReturnType.getCanonicalName())) {
      if (!ObjectFunctions.propertiesHandleClass().isAssignableFrom(target.getClass())) {
        throw UnexpectedViolationException.withMessage("Invalid return type of method '{}' in class {}",
            projectionMethod.getName(), projectionMethod.getDeclaringClass().getCanonicalName());
      }
      return target;
    }
    if (ObjectFunctions.isCustomObjectHandleClass(expectedReturnType)) {
      if (ObjectFunctions.isDataObjectHandle(expectedReturnType)) {
        return module.mapThruTransition1(target, SpaceConstants.PROPERTIES_TO_DATA_TID, expectedReturnType);
      }
    }
    throw UnexpectedViolationException.withMessage("Invalid return type of method '{}' in class {}",
        projectionMethod.getName(), projectionMethod.getDeclaringClass().getCanonicalName());
  }
}
