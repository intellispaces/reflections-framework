package tech.intellispaces.reflections.framework.system.settings;

import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.commons.type.Types;
import tech.intellispaces.javareflection.method.MethodStatement;
import tech.intellispaces.reflections.framework.annotation.Properties;
import tech.intellispaces.reflections.framework.dataset.DatasetFunctions;
import tech.intellispaces.reflections.framework.exception.ConfigurationExceptions;
import tech.intellispaces.reflections.framework.node.ReflectionsNodeFunctions;
import tech.intellispaces.reflections.framework.reflection.ReflectionFunctions;
import tech.intellispaces.reflections.framework.settings.ChannelReference;
import tech.intellispaces.reflections.framework.settings.ChannelAssignments;
import tech.intellispaces.reflections.framework.system.ModuleHandle;
import tech.intellispaces.reflections.framework.system.Modules;
import tech.intellispaces.reflections.framework.system.projection.InjectedMethodProjectionSupplier;

public class ModulePropertiesProjectionSupplier extends InjectedMethodProjectionSupplier {

  public ModulePropertiesProjectionSupplier(MethodStatement injectedMethod) {
    super(injectedMethod);
  }

  @Override
  public Object get() {
    ModuleHandle module = Modules.current();
    Properties annotation = injectedMethod.selectAnnotation(Properties.class).orElseThrow();
    Object props = readPropertiesFile(annotation.path(), module);
    Object propertyValue = traverseToPropertyValue(props, annotation.value(), module);

    Class<?> expectedReturnClass = injectedMethod.returnType().orElseThrow()
        .asCustomTypeReferenceOrElseThrow()
        .targetClass();
    if (propertyValue.getClass() == expectedReturnClass) {
      return propertyValue;
    }
    if (ReflectionFunctions.propertiesReflectionClass().getCanonicalName().equals(expectedReturnClass.getCanonicalName())) {
      if (!ReflectionFunctions.propertiesReflectionClass().isAssignableFrom(propertyValue.getClass())) {
        throw UnexpectedExceptions.withMessage("Invalid return type of method '{0}' in class {1}",
            injectedMethod.name(), injectedMethod.owner().canonicalName());
      }
      return propertyValue;
    }
    if (ReflectionFunctions.isObjectFormClass(expectedReturnClass)) {
      if (DatasetFunctions.isDatasetReflection(expectedReturnClass)) {
        return traverseToData(propertyValue, expectedReturnClass, module);
      }
    }
    throw UnexpectedExceptions.withMessage("Invalid return type of method '{0}' in class {1}. Expected any object form",
        injectedMethod.name(), injectedMethod.owner().canonicalName());
  }

  private Object readPropertiesFile(String filename, ModuleHandle module) {
    String settingsText = ModuleSettingsFunctions.getSettingsText(module, filename);
    if (filename.toLowerCase().endsWith(".yaml")) {
      ChannelReference channelReference = ReflectionsNodeFunctions.ontologyReference().getChannelByType(ChannelAssignments.YamlStringToPropertiesSet);
      return module.mapThruChannel0(settingsText, channelReference.channelId());
    }
    throw ConfigurationExceptions.withMessage("Unsupported module settings file format. File {0}", filename);
  }

  private Object traverseToPropertyValue(Object props, String traversePath, ModuleHandle module) {
    ChannelReference channelReference = ReflectionsNodeFunctions.ontologyReference().getChannelByType(ChannelAssignments.PropertiesSetToValue);
    return module.mapThruChannel1(props, channelReference.channelId(), traversePath);
  }

  private Object traverseToData(Object propsValue, Class<?> expectedReturnClass, ModuleHandle module) {
    ChannelReference channelReference = ReflectionsNodeFunctions.ontologyReference().getChannelByType(ChannelAssignments.PropertiesSetToData);
    return module.mapThruChannel1(propsValue, channelReference.channelId(), Types.get(expectedReturnClass));
  }
}
