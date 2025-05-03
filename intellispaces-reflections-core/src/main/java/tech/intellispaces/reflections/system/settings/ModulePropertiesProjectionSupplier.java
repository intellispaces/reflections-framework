package tech.intellispaces.reflections.system.settings;

import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.commons.type.Types;
import tech.intellispaces.reflections.Jaquarius;
import tech.intellispaces.reflections.annotation.Properties;
import tech.intellispaces.reflections.dataset.DatasetFunctions;
import tech.intellispaces.reflections.exception.ConfigurationExceptions;
import tech.intellispaces.reflections.object.reference.ObjectReferenceFunctions;
import tech.intellispaces.reflections.settings.ChannelReference;
import tech.intellispaces.reflections.settings.ChannelTypes;
import tech.intellispaces.reflections.system.Module;
import tech.intellispaces.reflections.system.Modules;
import tech.intellispaces.reflections.system.projection.InjectedMethodProjectionSupplier;
import tech.intellispaces.jstatements.method.MethodStatement;

public class ModulePropertiesProjectionSupplier extends InjectedMethodProjectionSupplier {

  public ModulePropertiesProjectionSupplier(MethodStatement injectedMethod) {
    super(injectedMethod);
  }

  @Override
  public Object get() {
    Module module = Modules.current();
    Properties annotation = injectedMethod.selectAnnotation(Properties.class).orElseThrow();
    Object props = readPropertiesFile(annotation.path(), module);
    Object propertyValue = traverseToPropertyValue(props, annotation.value(), module);

    Class<?> expectedReturnClass = injectedMethod.returnType().orElseThrow()
        .asCustomTypeReferenceOrElseThrow()
        .targetClass();
    if (propertyValue.getClass() == expectedReturnClass) {
      return propertyValue;
    }
    if (ObjectReferenceFunctions.propertiesHandleClass().getCanonicalName().equals(expectedReturnClass.getCanonicalName())) {
      if (!ObjectReferenceFunctions.propertiesHandleClass().isAssignableFrom(propertyValue.getClass())) {
        throw UnexpectedExceptions.withMessage("Invalid return type of method '{0}' in class {1}",
            injectedMethod.name(), injectedMethod.owner().canonicalName());
      }
      return propertyValue;
    }
    if (ObjectReferenceFunctions.isObjectFormClass(expectedReturnClass)) {
      if (DatasetFunctions.isDatasetObjectHandle(expectedReturnClass)) {
        return traverseToData(propertyValue, expectedReturnClass, module);
      }
    }
    throw UnexpectedExceptions.withMessage("Invalid return type of method '{0}' in class {1}. Expected any object form",
        injectedMethod.name(), injectedMethod.owner().canonicalName());
  }

  private Object readPropertiesFile(String filename, Module module) {
    String settingsText = ModuleSettingsFunctions.getSettingsText(module, filename);
    if (filename.toLowerCase().endsWith(".yaml")) {
      ChannelReference channelReference = Jaquarius.ontologyReference().getChannelByType(ChannelTypes.YamlStringToPropertiesSet);
      return module.mapThruChannel0(settingsText, channelReference.channelId());
    }
    throw ConfigurationExceptions.withMessage("Unsupported module settings file format. File {0}", filename);
  }

  private Object traverseToPropertyValue(Object props, String traversePath, Module module) {
    ChannelReference channelReference = Jaquarius.ontologyReference().getChannelByType(ChannelTypes.PropertiesSetToValue);
    return module.mapThruChannel1(props, channelReference.channelId(), traversePath);
  }

  private Object traverseToData(Object propsValue, Class<?> expectedReturnClass, Module module) {
    ChannelReference channelReference = Jaquarius.ontologyReference().getChannelByType(ChannelTypes.PropertiesSetToData);
    return module.mapThruChannel1(propsValue, channelReference.channelId(), Types.get(expectedReturnClass));
  }
}
