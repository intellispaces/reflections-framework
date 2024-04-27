package tech.intellispacesframework.core.system;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import tech.intellispacesframework.core.exception.ConfigurationException;
import tech.intellispacesframework.core.test.sample.system.EmptyUnit;
import tech.intellispacesframework.core.test.sample.system.UnitWithOneShutdownMethod;
import tech.intellispacesframework.core.test.sample.system.UnitWithOneStartupMethod;
import tech.intellispacesframework.core.test.sample.system.UnitWithProjectionWithoutReturnedType;
import tech.intellispacesframework.core.test.sample.system.UnitWithShutdownMethodReturnedString;
import tech.intellispacesframework.core.test.sample.system.UnitWithStartupMethodReturnedString;
import tech.intellispacesframework.core.test.sample.system.UnitWithTwoShutdownMethods;
import tech.intellispacesframework.core.test.sample.system.UnitWithTwoStartupMethods;
import tech.intellispacesframework.core.test.sample.system.UnitWithValidProjections;

/**
 * Tests for {@link UnitDeclarationValidator} class.
 */
public class UnitDeclarationValidatorTest {
  private final UnitDeclarationValidator validator = new UnitDeclarationValidator();

  @Test
  public void testValidateModule_whenEmptyMainUnit() {
    Assertions.assertThatCode(() -> validator.validateUnitDeclaration(EmptyUnit.class, true))
        .doesNotThrowAnyException();
  }

  @Test
  public void testValidateModule_whenEmptyAdditionalUnit() {
    Assertions.assertThatCode(() -> validator.validateUnitDeclaration(EmptyUnit.class, false))
        .doesNotThrowAnyException();
  }

  @Test
  public void testValidateModule_whenMainUnitWithOneStartupMethod() {
    Assertions.assertThatCode(() -> validator.validateUnitDeclaration(UnitWithOneStartupMethod.class, true))
        .doesNotThrowAnyException();
  }

  @Test
  public void testValidateModule_whenAdditionalUnitWithOneStartupMethod() {
    Assertions.assertThatThrownBy(() -> validator.validateUnitDeclaration(UnitWithOneStartupMethod.class, false))
        .isExactlyInstanceOf(ConfigurationException.class)
        .hasMessage("Additional unit cannot contain startup method, but unit %s contains it", UnitWithOneStartupMethod.class.getCanonicalName());
  }

  @Test
  public void testValidateModule_whenMainUnitWithTwoStartupMethods() {
    Assertions.assertThatThrownBy(() -> validator.validateUnitDeclaration(UnitWithTwoStartupMethods.class, true))
        .isExactlyInstanceOf(ConfigurationException.class)
        .hasMessage("Main unit %s contains more that one startup methods", UnitWithTwoStartupMethods.class.getCanonicalName());
  }

  @Test
  public void testValidateModule_whenAdditionalUnitWithTwoStartupMethods() {
    Assertions.assertThatThrownBy(() -> validator.validateUnitDeclaration(UnitWithTwoStartupMethods.class, false))
        .isExactlyInstanceOf(ConfigurationException.class)
        .hasMessage("Additional unit cannot contain startup method, but unit %s contains it", UnitWithTwoStartupMethods.class.getCanonicalName());
  }

  @Test
  public void testValidateModule_whenStartupMethodReturnedString() {
    Assertions.assertThatCode(() -> validator.validateUnitDeclaration(UnitWithStartupMethodReturnedString.class, true))
        .doesNotThrowAnyException();
  }

  @Test
  public void testValidateModule_whenMainUnitWithOneShutdownMethod() {
    Assertions.assertThatCode(() -> validator.validateUnitDeclaration(UnitWithOneShutdownMethod.class, true))
        .doesNotThrowAnyException();
  }

  @Test
  public void testValidateModule_whenAdditionalUnitWithOneShutdownMethod() {
    Assertions.assertThatThrownBy(() -> validator.validateUnitDeclaration(UnitWithOneShutdownMethod.class, false))
        .isExactlyInstanceOf(ConfigurationException.class)
        .hasMessage("Additional unit cannot contain shutdown method, but unit %s contains it", UnitWithOneShutdownMethod.class.getCanonicalName());
  }

  @Test
  public void testValidateModule_whenMainUnitWithTwoShutdownMethods() {
    Assertions.assertThatThrownBy(() -> validator.validateUnitDeclaration(UnitWithTwoShutdownMethods.class, true))
        .isExactlyInstanceOf(ConfigurationException.class)
        .hasMessage("Main unit %s contains more that one shutdown methods", UnitWithTwoShutdownMethods.class.getCanonicalName());
  }

  @Test
  public void testValidateModule_whenAdditionalUnitWithTwoShutdownMethods() {
    Assertions.assertThatThrownBy(() -> validator.validateUnitDeclaration(UnitWithTwoShutdownMethods.class, false))
        .isExactlyInstanceOf(ConfigurationException.class)
        .hasMessage("Additional unit cannot contain shutdown method, but unit %s contains it", UnitWithTwoShutdownMethods.class.getCanonicalName());
  }

  @Test
  public void testValidateModule_whenShutdownMethodReturnedString() {
    Assertions.assertThatCode(() -> validator.validateUnitDeclaration(UnitWithShutdownMethodReturnedString.class, true))
        .doesNotThrowAnyException();
  }

  @Test
  public void testValidateModule_whenUnitWithValidProjections() {
    Assertions.assertThatCode(() -> validator.validateUnitDeclaration(UnitWithValidProjections.class, true))
        .doesNotThrowAnyException();
  }

  @Test
  public void testValidateModule_whenProjectionWithoutReturnedType() {
    Assertions.assertThatThrownBy(() -> validator.validateUnitDeclaration(UnitWithProjectionWithoutReturnedType.class, false))
        .isExactlyInstanceOf(ConfigurationException.class)
        .hasMessage("Method of the projection 'projection' in unit %s should return value", UnitWithProjectionWithoutReturnedType.class.getCanonicalName());
  }
}
