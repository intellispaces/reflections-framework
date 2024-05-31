package tech.intellispacesframework.core.system.projection;

import tech.intellispacesframework.core.annotation.Properties;
import tech.intellispacesframework.core.space.SpaceConstants;
import tech.intellispacesframework.core.system.Module;
import tech.intellispacesframework.core.system.PropertiesFunctions;
import tech.intellispacesframework.core.system.Modules;

import java.lang.reflect.Method;

import static tech.intellispacesframework.core.space.SpaceConstants.PROPERTIES_DOMAIN_NAME;
import static tech.intellispacesframework.core.space.SpaceConstants.PROPERTIES_HANDLE_CLASSNAME;

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
    if (PROPERTIES_DOMAIN_NAME.equals(returnClassname) || PROPERTIES_HANDLE_CLASSNAME.equals(returnClassname)) {
      return targetProperties;
    }

    return module.mapThruTransition1(
        targetProperties, SpaceConstants.PROPERTIES_TO_DATA_TID, projectionMethod.getReturnType());
  }
}
