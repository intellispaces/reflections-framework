package tech.intellispaces.core.system;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import tech.intellispaces.core.exception.ConfigurationException;
import tech.intellispaces.core.samples.system.EmptyModule;
import tech.intellispaces.core.samples.system.EmptyUnit;
import tech.intellispaces.core.system.shadow.ModuleValidator;
import tech.intellispaces.core.system.shadow.ShadowModule;
import tech.intellispaces.core.system.shadow.ShadowUnit;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link ModuleValidator} class.
 */
public class ModuleValidatorTest {
  private final ModuleValidator moduleValidator = new ModuleValidator();

  @Test
  public void testValidate_whenNoMainUnit() {
    // Given
    ShadowUnit unit1 = mock(ShadowUnit.class);
    when(unit1.isMain()).thenReturn(false);

    ShadowUnit unit2 = mock(ShadowUnit.class);
    when(unit2.isMain()).thenReturn(false);

    ShadowModule module = mock(ShadowModule.class);
    when(module.units()).thenReturn(List.of(unit1, unit2));

    // Then
    Assertions.assertThatThrownBy(() -> moduleValidator.validate(module))
        .isExactlyInstanceOf(ConfigurationException.class)
        .hasMessage("Main unit is not found");
  }

  @Test
  @SuppressWarnings("unchecked,rawtypes")
  public void testValidate_whenTwoMainUnits() {
    // Given
    Class unitClass1 = EmptyModule.class;
    Class unitClass2 = EmptyUnit.class;

    ShadowUnit unit1 = mock(ShadowUnit.class);
    when(unit1.unitClass()).thenReturn(unitClass1);
    when(unit1.isMain()).thenReturn(true);

    ShadowUnit unit2 = mock(ShadowUnit.class);
    when(unit2.unitClass()).thenReturn(unitClass2);
    when(unit2.isMain()).thenReturn(true);

    ShadowModule module = mock(ShadowModule.class);
    when(module.units()).thenReturn(List.of(unit1, unit2));

    // Then
    Assertions.assertThatThrownBy(() -> moduleValidator.validate(module))
        .isExactlyInstanceOf(ConfigurationException.class)
        .hasMessage("Multiple main units found: " + unitClass1.getSimpleName() + ", " + unitClass2.getSimpleName());
  }

  @Test
  @SuppressWarnings("unchecked,rawtypes")
  public void testValidate_whenTwoProjectionsWithSameName() throws Exception {
    // Given
    Class unitClass1 = EmptyModule.class;
    Class unitClass2 = EmptyUnit.class;
    String projectionName = "projection";

    ShadowUnit unit1 = mock(ShadowUnit.class);
    when(unit1.isMain()).thenReturn(true);
    when(unit1.unitClass()).thenReturn(unitClass1);
    when(unit1.shutdownAction()).thenReturn(Optional.empty());
    UnitProjectionDefinition projectionProvider1 = mock(UnitProjectionDefinition.class);
    when(projectionProvider1.name()).thenReturn(projectionName);
    when(projectionProvider1.unitClass()).thenReturn(unitClass1);
    when(projectionProvider1.projectionMethod()).thenReturn(String.class.getDeclaredMethod("trim"));
    when(unit1.projectionDefinitions()).thenReturn(List.of(projectionProvider1));

    ShadowUnit unit2 = mock(ShadowUnit.class);
    when(unit2.isMain()).thenReturn(false);
    when(unit2.unitClass()).thenReturn(unitClass2);
    when(unit2.shutdownAction()).thenReturn(Optional.empty());
    UnitProjectionDefinition projectionProvider2 = mock(UnitProjectionDefinition.class);
    when(projectionProvider2.unitClass()).thenReturn(unitClass2);
    when(projectionProvider2.projectionMethod()).thenReturn(Integer.class.getDeclaredMethod("intValue"));
    when(projectionProvider2.name()).thenReturn(projectionName);
    when(unit2.projectionDefinitions()).thenReturn(List.of(projectionProvider2));

    ShadowModule module = mock(ShadowModule.class);
    when(module.units()).thenReturn(List.of(unit1, unit2));

    // Then
    Assertions.assertThatThrownBy(() -> moduleValidator.validate(module))
        .isExactlyInstanceOf(ConfigurationException.class)
        .hasMessage("Found multiple projections with same name: Projection name '" + projectionName + "', projection providers: " +
            unitClass1.getCanonicalName() + "#trim, " +
            unitClass2.getCanonicalName() + "#intValue");
  }

  @Test
  @SuppressWarnings("unchecked,rawtypes")
  public void testValidate_whenUnitProjectionMiss() {
    // Given
    Class unitClass = EmptyModule.class;
    String projectionName = "projection";

    UnitProjectionInjection injection = mock(UnitProjectionInjection.class);
    when(injection.name()).thenReturn(projectionName);
    when(injection.targetClass()).thenReturn((Class) String.class);
    when(injection.unitClass()).thenReturn(unitClass);

    ShadowUnit unit = mock(ShadowUnit.class);
    when(unit.isMain()).thenReturn(true);
    when(unit.unitClass()).thenReturn(unitClass);
    when(unit.startupMethod()).thenReturn(Optional.empty());
    when(unit.shutdownMethod()).thenReturn(Optional.empty());
    when(unit.projectionInjections()).thenReturn(List.of(injection));
    when(unit.projectionDefinitions()).thenReturn(List.of());

    ShadowModule module = mock(ShadowModule.class);
    when(module.units()).thenReturn(List.of(unit));

    // Then
    Assertions.assertThatThrownBy(() -> moduleValidator.validate(module))
        .isExactlyInstanceOf(ConfigurationException.class)
        .hasMessage("Projection injection by name '" + projectionName +
            "' declared in unit " + EmptyModule.class.getCanonicalName() + " is not found");
  }

  @Test
  @SuppressWarnings("unchecked,rawtypes")
  public void testValidate_whenUnitProjectionTypeIncompatible() {
    // Given
    Class unitClass = EmptyModule.class;
    String projectionName = "projection";
    Class projectionType = String.class;
    Class injectionType = Integer.class;

    UnitProjectionInjection injection = mock(UnitProjectionInjection.class);
    when(injection.name()).thenReturn(projectionName);
    when(injection.targetClass()).thenReturn(injectionType);
    when(injection.unitClass()).thenReturn(unitClass);

    Method providerMethod = mock((Method.class));
    when(providerMethod.getName()).thenReturn(projectionName);
    when(providerMethod.getReturnType()).thenReturn(projectionType);
    UnitProjectionDefinition projectionProvider = mock(UnitProjectionDefinition.class);
    when(projectionProvider.name()).thenReturn(projectionName);
    when(projectionProvider.type()).thenReturn(projectionType);

    ShadowUnit unit = mock(ShadowUnit.class);
    when(unit.isMain()).thenReturn(true);
    when(unit.unitClass()).thenReturn(unitClass);
    when(unit.startupMethod()).thenReturn(Optional.empty());
    when(unit.shutdownMethod()).thenReturn(Optional.empty());
    when(unit.projectionInjections()).thenReturn(List.of(injection));
    when(unit.projectionDefinitions()).thenReturn(List.of(projectionProvider));

    ShadowModule module = mock(ShadowModule.class);
    when(module.units()).thenReturn(List.of(unit));

    // Then
    Assertions.assertThatThrownBy(() -> moduleValidator.validate(module))
        .isExactlyInstanceOf(ConfigurationException.class)
        .hasMessage("Projection injection '" + projectionName + "' declared in unit " + unitClass.getCanonicalName() + " has an incompatible target type. " +
            "Expected type " + injectionType.getCanonicalName() + ", actual type " + projectionType.getCanonicalName());
  }

  @Test
  @SuppressWarnings("unchecked,rawtypes")
  public void testValidate_whenValid() throws Exception {
    // Given
    ShadowUnit unit1 = mock(ShadowUnit.class);
    Class unitClass1 = String.class;
    when(unit1.isMain()).thenReturn(true);
    when(unit1.unitClass()).thenReturn(unitClass1);

    when(unit1.startupMethod()).thenReturn(Optional.of(String.class.getDeclaredMethod("isEmpty")));
    when(unit1.shutdownMethod()).thenReturn(Optional.of(String.class.getDeclaredMethod("isBlank")));
    UnitProjectionDefinition projectionProvider1 = mock(UnitProjectionDefinition.class);
    when(projectionProvider1.name()).thenReturn("projection1");
    when(projectionProvider1.unitClass()).thenReturn(unitClass1);
    when(projectionProvider1.projectionMethod()).thenReturn(String.class.getDeclaredMethod("trim"));
    when(unit1.projectionDefinitions()).thenReturn(List.of(projectionProvider1));

    ShadowUnit unit2 = mock(ShadowUnit.class);
    Class unitClass2 = Integer.class;
    when(unit2.isMain()).thenReturn(false);
    when(unit2.unitClass()).thenReturn(unitClass2);
    when(unit2.shutdownAction()).thenReturn(Optional.empty());
    UnitProjectionDefinition projectionProvider2 = mock(UnitProjectionDefinition.class);
    when(projectionProvider2.unitClass()).thenReturn(unitClass2);
    when(projectionProvider2.projectionMethod()).thenReturn(Integer.class.getDeclaredMethod("intValue"));
    when(projectionProvider2.name()).thenReturn("projection2");
    when(unit2.projectionDefinitions()).thenReturn(List.of(projectionProvider2));

    ShadowModule module = mock(ShadowModule.class);
    when(module.units()).thenReturn(List.of(unit1, unit2));

    // Then
    Assertions.assertThatCode(() -> moduleValidator.validate(module))
        .doesNotThrowAnyException();
  }
}
