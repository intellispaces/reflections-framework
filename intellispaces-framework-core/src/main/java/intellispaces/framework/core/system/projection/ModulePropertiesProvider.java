package intellispaces.framework.core.system.projection;

import intellispaces.common.base.exception.UnexpectedViolationException;
import intellispaces.framework.core.annotation.Properties;
import intellispaces.framework.core.object.DataFunctions;
import intellispaces.framework.core.object.ObjectFunctions;
import intellispaces.framework.core.object.ObjectHandleConstants;
import intellispaces.framework.core.space.SpaceConstants;
import intellispaces.framework.core.system.Module;
import intellispaces.framework.core.system.ModulePropertiesFunctions;
import intellispaces.framework.core.system.Modules;

import java.lang.reflect.Method;

public class ModulePropertiesProvider extends AbstractProjectionProvider {

  public ModulePropertiesProvider(Method projectionMethod) {
    super(projectionMethod);
  }

  @Override
  public Object get() {
    Module module = Modules.current();
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
        throw UnexpectedViolationException.withMessage("Invalid return type of method ''{0}'' in class {1}",
            projectionMethod.getName(), projectionMethod.getDeclaringClass().getCanonicalName());
      }
      return target;
    }
    if (ObjectFunctions.isCustomObjectHandleClass(expectedReturnType)) {
      if (DataFunctions.isDataObjectHandle(expectedReturnType)) {
        return module.mapThruTransition1(target, SpaceConstants.PROPERTIES_TO_DATA_TID, expectedReturnType);
      }
    }
    throw UnexpectedViolationException.withMessage("Invalid return type of method ''{0}'' in class {1}",
        projectionMethod.getName(), projectionMethod.getDeclaringClass().getCanonicalName());
  }
}
