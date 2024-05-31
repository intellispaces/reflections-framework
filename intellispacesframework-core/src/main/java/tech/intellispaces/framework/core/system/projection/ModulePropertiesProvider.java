package tech.intellispaces.framework.core.system.projection;

import tech.intellispaces.framework.core.space.SpaceConstants;
import tech.intellispaces.framework.core.system.PropertiesFunctions;
import tech.intellispaces.framework.core.annotation.Properties;
import tech.intellispaces.framework.core.system.Module;
import tech.intellispaces.framework.core.system.Modules;

import java.lang.reflect.Method;

public class ModulePropertiesProvider extends AbstractProjectionProvider {

  public ModulePropertiesProvider(Method projectionMethod) {
    super(projectionMethod);
  }

  @Override
  public Object get() {
    Module module = Modules.activeModule();
    Properties annotation = projectionMethod.getAnnotation(Properties.class);
    String propertiesContent = PropertiesFunctions.readProperties(module, null);
    Object properties = module.mapThruTransition0(propertiesContent, SpaceConstants.STRING_TO_PROPERTIES_TID);

    Object targetProperties = module.mapThruTransition1(
        properties, SpaceConstants.PROPERTIES_TO_PROPERTIES_VALUE_TID, annotation.value());
    String returnClassname = projectionMethod.getReturnType().getCanonicalName();
    if (SpaceConstants.PROPERTIES_DOMAIN_NAME.equals(returnClassname) || SpaceConstants.PROPERTIES_HANDLE_CLASSNAME.equals(returnClassname)) {
      return targetProperties;
    }

    return module.mapThruTransition1(
        targetProperties, SpaceConstants.PROPERTIES_TO_DATA_TID, projectionMethod.getReturnType());
  }
}
