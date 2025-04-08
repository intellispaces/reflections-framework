package tech.intellispaces.jaquarius.system.settings;

import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.commons.type.Types;
import tech.intellispaces.jaquarius.Jaquarius;
import tech.intellispaces.jaquarius.annotation.Properties;
import tech.intellispaces.jaquarius.dataset.DatasetFunctions;
import tech.intellispaces.jaquarius.exception.ConfigurationExceptions;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceFunctions;
import tech.intellispaces.jaquarius.settings.KeyChannel;
import tech.intellispaces.jaquarius.settings.KeyChannelPurposes;
import tech.intellispaces.jaquarius.system.Module;
import tech.intellispaces.jaquarius.system.Modules;
import tech.intellispaces.jaquarius.system.projection.InjectedMethodProjectionSupplier;
import tech.intellispaces.reflection.method.MethodStatement;

public class ModulePropertiesProjectionSupplier extends InjectedMethodProjectionSupplier {

  public ModulePropertiesProjectionSupplier(MethodStatement injectedMethod) {
    super(injectedMethod);
  }

  @Override
  public Object get() {
    Module module = Modules.current();
    Properties annotation = injectedMethod.selectAnnotation(Properties.class).orElseThrow();
    Object settingsProperties = parseSettingsFile(annotation.filename(), module);
    Object settingsValue = traverseToValue(settingsProperties, annotation.value(), module);

    Class<?> expectedReturnClass = injectedMethod.returnType().orElseThrow()
        .asCustomTypeReferenceOrElseThrow()
        .targetClass();
    if (settingsValue.getClass() == expectedReturnClass) {
      return settingsValue;
    }
    if (ObjectReferenceFunctions.propertiesHandleClass().getCanonicalName().equals(expectedReturnClass.getCanonicalName())) {
      if (!ObjectReferenceFunctions.propertiesHandleClass().isAssignableFrom(settingsValue.getClass())) {
        throw UnexpectedExceptions.withMessage("Invalid return type of method '{0}' in class {1}",
            injectedMethod.name(), injectedMethod.owner().canonicalName());
      }
      return settingsValue;
    }
    if (ObjectReferenceFunctions.isObjectFormClass(expectedReturnClass)) {
      if (DatasetFunctions.isDatasetObjectHandle(expectedReturnClass)) {
        return traverseToData(settingsValue, expectedReturnClass, module);
      }
    }
    throw UnexpectedExceptions.withMessage("Invalid return type of method '{0}' in class {1}. Expected any object form",
        injectedMethod.name(), injectedMethod.owner().canonicalName());
  }

  private Object parseSettingsFile(String filename, Module module) {
    String settingsText = ModuleSettingsFunctions.getSettingsText(module, filename);
    if (filename.toLowerCase().endsWith(".yaml")) {
      KeyChannel keyChannel = Jaquarius.settings().getKeyChannelByPurpose(KeyChannelPurposes.YamlStringToProperties);
      return module.mapThruChannel0(settingsText, keyChannel.cid());
    }
    throw ConfigurationExceptions.withMessage("Unsupported module settings file format. File {0}", filename);
  }

  private Object traverseToValue(Object settingsProperties, String valuePath, Module module) {
    KeyChannel keyChannel = Jaquarius.settings().getKeyChannelByPurpose(KeyChannelPurposes.PropertiesToValue);
    return module.mapThruChannel1(settingsProperties, keyChannel.cid(), valuePath);
  }

  private Object traverseToData(Object settingsValue, Class<?> expectedReturnClass, Module module) {
    KeyChannel keyChannel = Jaquarius.settings().getKeyChannelByPurpose(KeyChannelPurposes.PropertiesToData);
    return module.mapThruChannel1(settingsValue, keyChannel.cid(), Types.get(expectedReturnClass));
  }
}
