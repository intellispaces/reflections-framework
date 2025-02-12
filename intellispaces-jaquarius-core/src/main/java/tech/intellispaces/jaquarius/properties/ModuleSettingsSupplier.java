package tech.intellispaces.jaquarius.properties;

import tech.intellispaces.commons.base.exception.UnexpectedExceptions;
import tech.intellispaces.commons.base.type.Types;
import tech.intellispaces.commons.java.reflection.method.MethodStatement;
import tech.intellispaces.jaquarius.annotation.Settings;
import tech.intellispaces.jaquarius.data.DataFunctions;
import tech.intellispaces.jaquarius.object.reference.ObjectHandleConstants;
import tech.intellispaces.jaquarius.object.reference.ObjectHandleFunctions;
import tech.intellispaces.jaquarius.space.SpaceConstants;
import tech.intellispaces.jaquarius.system.Module;
import tech.intellispaces.jaquarius.system.Modules;
import tech.intellispaces.jaquarius.system.projection.AbstractProjectionSupplier;

public class ModuleSettingsSupplier extends AbstractProjectionSupplier {

  public ModuleSettingsSupplier(MethodStatement projectionMethod) {
    super(projectionMethod);
  }

  @Override
  public Object get() {
    Module module = Modules.current();
    Settings annotation = projectionMethod.selectAnnotation(Settings.class).orElseThrow();
    String rawProperties = ModulePropertiesFunctions.getProperties(module, null);
    Object parsedProperties = module.mapThruChannel0(rawProperties, SpaceConstants.STRING_TO_DICTIONARY_CID);

    Object target = module.mapThruChannel1(
        parsedProperties, SpaceConstants.DICTIONARY_TO_VALUE_CID, annotation.value());

    Class<?> expectedReturnType = projectionMethod.returnType().orElseThrow()
        .asCustomTypeReferenceOrElseThrow()
        .targetClass();
    if (target.getClass() == expectedReturnType) {
      return target;
    }
    if (ObjectHandleConstants.DICTIONARY_HANDLE_CLASSNAME.equals(expectedReturnType.getCanonicalName())) {
      if (!ObjectHandleFunctions.propertiesHandleClass().isAssignableFrom(target.getClass())) {
        throw UnexpectedExceptions.withMessage("Invalid return type of method '{0}' in class {1}",
            projectionMethod.name(), projectionMethod.owner().canonicalName());
      }
      return target;
    }
    if (ObjectHandleFunctions.isCustomObjectHandleClass(expectedReturnType)) {
      if (DataFunctions.isDataObjectHandle(expectedReturnType)) {
        return module.mapThruChannel1(target, SpaceConstants.DICTIONARY_TO_DATA_CID, Types.get(expectedReturnType));
      }
    }
    throw UnexpectedExceptions.withMessage("Invalid return type of method '{0}' in class {1}",
        projectionMethod.name(), projectionMethod.owner().canonicalName());
  }
}
