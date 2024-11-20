package intellispaces.jaquarius.system.projection;

import intellispaces.jaquarius.annotation.Properties;
import intellispaces.jaquarius.object.DataFunctions;
import intellispaces.jaquarius.object.ObjectFunctions;
import intellispaces.jaquarius.object.ObjectHandleConstants;
import intellispaces.jaquarius.space.SpaceConstants;
import intellispaces.jaquarius.system.Module;
import intellispaces.jaquarius.system.ModulePropertiesFunctions;
import intellispaces.jaquarius.system.Modules;
import tech.intellispaces.entity.exception.UnexpectedExceptions;

import java.lang.reflect.Method;

public class ModulePropertiesTargetSupplier extends AbstractProjectionTargetSupplier {

  public ModulePropertiesTargetSupplier(Method projectionMethod) {
    super(projectionMethod);
  }

  @Override
  public Object get() {
    Module module = Modules.current();
    Properties annotation = projectionMethod.getAnnotation(Properties.class);
    String rawProperties = ModulePropertiesFunctions.getProperties(module, null);
    Object parsedProperties = module.mapThruChannel0(rawProperties, SpaceConstants.STRING_TO_PROPERTIES_TID);

    Object target = module.mapThruChannel1(
        parsedProperties, SpaceConstants.PROPERTIES_TO_VALUE_TID, annotation.value());

    Class<?> expectedReturnType = projectionMethod.getReturnType();
    if (target.getClass() == expectedReturnType) {
      return target;
    }
    if (ObjectHandleConstants.PROPERTIES_HANDLE_CLASSNAME.equals(expectedReturnType.getCanonicalName())) {
      if (!ObjectFunctions.propertiesHandleClass().isAssignableFrom(target.getClass())) {
        throw UnexpectedExceptions.withMessage("Invalid return type of method '{0}' in class {1}",
            projectionMethod.getName(), projectionMethod.getDeclaringClass().getCanonicalName());
      }
      return target;
    }
    if (ObjectFunctions.isCustomObjectHandleClass(expectedReturnType)) {
      if (DataFunctions.isDataObjectHandle(expectedReturnType)) {
        return module.mapThruChannel1(target, SpaceConstants.PROPERTIES_TO_DATA_TID, expectedReturnType);
      }
    }
    throw UnexpectedExceptions.withMessage("Invalid return type of method '{0}' in class {1}",
        projectionMethod.getName(), projectionMethod.getDeclaringClass().getCanonicalName());
  }
}
