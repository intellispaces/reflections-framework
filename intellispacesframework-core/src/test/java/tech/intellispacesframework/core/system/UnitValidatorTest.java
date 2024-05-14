package tech.intellispacesframework.core.system;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import tech.intellispacesframework.core.exception.ConfigurationException;
import tech.intellispacesframework.core.samples.system.EmptyModule;
import tech.intellispacesframework.core.samples.system.EmptyUnit;
import tech.intellispacesframework.core.samples.system.ModuleWithOneShutdownMethod;
import tech.intellispacesframework.core.samples.system.ModuleWithOneStartupMethod;
import tech.intellispacesframework.core.samples.system.UnitWithProjectionAndFileParameter;
import tech.intellispacesframework.core.samples.system.UnitWithProjectionAndStringParameter;
import tech.intellispacesframework.core.samples.system.UnitWithProjectionWithoutReturnedType;
import tech.intellispacesframework.core.samples.system.ModuleWithShutdownMethodAndFileParameter;
import tech.intellispacesframework.core.samples.system.ModuleWithShutdownMethodAndStringParameter;
import tech.intellispacesframework.core.samples.system.ModuleWithShutdownMethodReturnedString;
import tech.intellispacesframework.core.samples.system.ModuleWithStartupMethodAndFileParameter;
import tech.intellispacesframework.core.samples.system.ModuleWithStartupMethodAndStringParameter;
import tech.intellispacesframework.core.samples.system.ModuleWithStartupMethodReturnedString;
import tech.intellispacesframework.core.samples.system.ModuleWithTwoShutdownMethods;
import tech.intellispacesframework.core.samples.system.ModuleWithTwoStartupMethods;
import tech.intellispacesframework.core.samples.system.ModuleWithSimpleProjections;
import tech.intellispacesframework.core.samples.system.UnitWithOneShutdownMethod;
import tech.intellispacesframework.core.samples.system.UnitWithOneStartupMethod;
import tech.intellispacesframework.core.samples.system.UnitWithTwoShutdownMethods;
import tech.intellispacesframework.core.samples.system.UnitWithTwoStartupMethods;

/**
 * Tests for {@link UnitValidator} class.
 */
public class UnitValidatorTest {
  private final UnitValidator unitValidator = new UnitValidator();

  @Test
  public void testValidateUnit_whenEmptyModule() {
    Assertions.assertThatCode(() -> unitValidator.validateUnitDeclaration(EmptyModule.class, true))
        .doesNotThrowAnyException();
  }

  @Test
  public void testValidateUnit_whenEmptyUnit() {
    Assertions.assertThatCode(() -> unitValidator.validateUnitDeclaration(EmptyUnit.class, false))
        .doesNotThrowAnyException();
  }

  @Test
  public void testValidateModule_whenModuleWithOneStartupMethod() {
    Assertions.assertThatCode(() -> unitValidator.validateUnitDeclaration(ModuleWithOneStartupMethod.class, true))
        .doesNotThrowAnyException();
  }

  @Test
  public void testValidateModule_whenUnitWithOneStartupMethod() {
    Assertions.assertThatThrownBy(() -> unitValidator.validateUnitDeclaration(UnitWithOneStartupMethod.class, false))
        .isExactlyInstanceOf(ConfigurationException.class)
        .hasMessage("Additional unit cannot contain startup method, but unit %s contains it", UnitWithOneStartupMethod.class.getCanonicalName());
  }

  @Test
  public void testValidateModule_whenModuleWithTwoStartupMethods() {
    Assertions.assertThatThrownBy(() -> unitValidator.validateUnitDeclaration(ModuleWithTwoStartupMethods.class, true))
        .isExactlyInstanceOf(ConfigurationException.class)
        .hasMessage("Main unit %s contains more that one startup methods", ModuleWithTwoStartupMethods.class.getCanonicalName());
  }

  @Test
  public void testValidateModule_whenUnitWithTwoStartupMethods() {
    Assertions.assertThatThrownBy(() -> unitValidator.validateUnitDeclaration(UnitWithTwoStartupMethods.class, true))
        .isExactlyInstanceOf(ConfigurationException.class)
        .hasMessage("Main unit %s contains more that one startup methods", UnitWithTwoStartupMethods.class.getCanonicalName());
  }

  @Test
  public void testValidateModule_whenStartupMethodReturnedString() {
    Assertions.assertThatCode(() -> unitValidator.validateUnitDeclaration(ModuleWithStartupMethodReturnedString.class, true))
        .doesNotThrowAnyException();
  }

  @Test
  public void testValidateModule_whenStartupMethodAndStringParameter() {
    Assertions.assertThatCode(() -> unitValidator.validateUnitDeclaration(ModuleWithStartupMethodAndStringParameter.class, true))
        .doesNotThrowAnyException();
  }

  @Test
  public void testValidateModule_whenStartupMethodAndFileParameter() {
    Assertions.assertThatThrownBy(() -> unitValidator.validateUnitDeclaration(ModuleWithStartupMethodAndFileParameter.class, true))
        .isExactlyInstanceOf(ConfigurationException.class)
        .hasMessage("Parameter 'value' of method 'startup' in unit " + ModuleWithStartupMethodAndFileParameter.class.getCanonicalName() + " should be object handle or domain class");
  }

  @Test
  public void testValidateModule_whenModuleWithOneShutdownMethod() {
    Assertions.assertThatCode(() -> unitValidator.validateUnitDeclaration(ModuleWithOneShutdownMethod.class, true))
        .doesNotThrowAnyException();
  }

  @Test
  public void testValidateModule_whenUnitWithOneShutdownMethod() {
    Assertions.assertThatThrownBy(() -> unitValidator.validateUnitDeclaration(UnitWithOneShutdownMethod.class, false))
        .isExactlyInstanceOf(ConfigurationException.class)
        .hasMessage("Additional unit cannot contain shutdown method, but unit %s contains it", UnitWithOneShutdownMethod.class.getCanonicalName());
  }

  @Test
  public void testValidateModule_whenModuleWithTwoShutdownMethods() {
    Assertions.assertThatThrownBy(() -> unitValidator.validateUnitDeclaration(ModuleWithTwoShutdownMethods.class, true))
        .isExactlyInstanceOf(ConfigurationException.class)
        .hasMessage("Main unit %s contains more that one shutdown methods", ModuleWithTwoShutdownMethods.class.getCanonicalName());
  }

  @Test
  public void testValidateModule_whenUnitWithTwoShutdownMethods() {
    Assertions.assertThatThrownBy(() -> unitValidator.validateUnitDeclaration(UnitWithTwoShutdownMethods.class, false))
        .isExactlyInstanceOf(ConfigurationException.class)
        .hasMessage("Additional unit cannot contain shutdown method, but unit %s contains it", UnitWithTwoShutdownMethods.class.getCanonicalName());
  }

  @Test
  public void testValidateModule_whenShutdownMethodReturnedString() {
    Assertions.assertThatCode(() -> unitValidator.validateUnitDeclaration(ModuleWithShutdownMethodReturnedString.class, true))
        .doesNotThrowAnyException();
  }

  @Test
  public void testValidateModule_whenShutdownMethodAndStringParameter() {
    Assertions.assertThatCode(() -> unitValidator.validateUnitDeclaration(ModuleWithShutdownMethodAndStringParameter.class, true))
        .doesNotThrowAnyException();
  }

  @Test
  public void testValidateModule_whenShutdownMethodAndFileParameter() {
    Assertions.assertThatThrownBy(() -> unitValidator.validateUnitDeclaration(ModuleWithShutdownMethodAndFileParameter.class, true))
        .isExactlyInstanceOf(ConfigurationException.class)
        .hasMessage("Parameter 'value' of method 'shutdown' in unit " + ModuleWithShutdownMethodAndFileParameter.class.getCanonicalName() + " should be object handle or domain class");
  }

  @Test
  public void testValidateModule_whenValidProjections() {
    Assertions.assertThatCode(() -> unitValidator.validateUnitDeclaration(ModuleWithSimpleProjections.class, true))
        .doesNotThrowAnyException();
  }

  @Test
  public void testValidateModule_whenProjectionWithoutReturnedType() {
    Assertions.assertThatThrownBy(() -> unitValidator.validateUnitDeclaration(UnitWithProjectionWithoutReturnedType.class, false))
        .isExactlyInstanceOf(ConfigurationException.class)
        .hasMessage("Method of the projection 'projection' in unit %s should return value", UnitWithProjectionWithoutReturnedType.class.getCanonicalName());
  }

  @Test
  public void testValidateModule_whenProjectionAndStringParameter() {
    Assertions.assertThatCode(() -> unitValidator.validateUnitDeclaration(UnitWithProjectionAndStringParameter.class, false))
        .doesNotThrowAnyException();
  }

  @Test
  public void testValidateModule_whenProjectionAndFileParameter() {
    Assertions.assertThatThrownBy(() -> unitValidator.validateUnitDeclaration(UnitWithProjectionAndFileParameter.class, false))
        .isExactlyInstanceOf(ConfigurationException.class)
        .hasMessage("Parameter 'value' of method 'projection' in unit " + UnitWithProjectionAndFileParameter.class.getCanonicalName() + " should be object handle or domain class");
  }
}
