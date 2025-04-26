package tech.intellispaces.jaquarius.engine.impl;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import tech.intellispaces.jaquarius.exception.ConfigurationException;
import tech.intellispaces.jaquarius.samples.system.EmptyTestModule;
import tech.intellispaces.jaquarius.samples.system.EmptyTestUnit;
import tech.intellispaces.jaquarius.system.ProjectionInjection;
import tech.intellispaces.jaquarius.system.UnitProjectionDefinition;
import tech.intellispaces.jaquarius.system.injection.InjectionKinds;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link ModuleValidator} class.
 */
public class ModuleValidatorTest {

  @Test
  public void testValidate_whenNoMainUnit() {
    // Given
    var unit1 = mock(Unit.class);
    when(unit1.isMain()).thenReturn(false);

    var unit2 = mock(Unit.class);
    when(unit2.isMain()).thenReturn(false);

    var module = mock(Module.class);
    when(module.units()).thenReturn(List.of(unit1, unit2));

    // Then
    Assertions.assertThatThrownBy(() -> ModuleValidator.validate(module))
        .isExactlyInstanceOf(ConfigurationException.class)
        .hasMessage("Main unit is not found");
  }

  @Test
  @SuppressWarnings("unchecked,rawtypes")
  public void testValidate_whenTwoMainUnits() {
    // Given
    Class unitClass1 = EmptyTestModule.class;
    Class unitClass2 = EmptyTestUnit.class;

    var unit1 = mock(Unit.class);
    when(unit1.unitClass()).thenReturn(unitClass1);
    when(unit1.isMain()).thenReturn(true);

    var unit2 = mock(Unit.class);
    when(unit2.unitClass()).thenReturn(unitClass2);
    when(unit2.isMain()).thenReturn(true);

    var module = mock(Module.class);
    when(module.units()).thenReturn(List.of(unit1, unit2));

    // Then
    Assertions.assertThatThrownBy(() -> ModuleValidator.validate(module))
        .isExactlyInstanceOf(ConfigurationException.class)
        .hasMessage("Multiple main units found: " + unitClass1.getSimpleName() + ", " + unitClass2.getSimpleName());
  }

  @Test
  @SuppressWarnings("unchecked,rawtypes")
  public void testValidate_whenTwoProjectionsWithSameName() throws Exception {
    // Given
    Class unitClass1 = EmptyTestModule.class;
    Class unitClass2 = EmptyTestUnit.class;
    String projectionName = "projection";

    var unit1 = mock(Unit.class);
    when(unit1.isMain()).thenReturn(true);
    when(unit1.unitClass()).thenReturn(unitClass1);
    when(unit1.shutdownAction()).thenReturn(Optional.empty());
    var projectionProvider1 = mock(UnitProjectionDefinition.class);
    when(projectionProvider1.name()).thenReturn(projectionName);
    when(projectionProvider1.unitClass()).thenReturn(unitClass1);
    when(projectionProvider1.projectionMethod()).thenReturn(String.class.getDeclaredMethod("trim"));
    when(unit1.projectionDefinitions()).thenReturn(List.of(projectionProvider1));

    var unit2 = mock(Unit.class);
    when(unit2.isMain()).thenReturn(false);
    when(unit2.unitClass()).thenReturn(unitClass2);
    when(unit2.shutdownAction()).thenReturn(Optional.empty());
    var projectionProvider2 = mock(UnitProjectionDefinition.class);
    when(projectionProvider2.unitClass()).thenReturn(unitClass2);
    when(projectionProvider2.projectionMethod()).thenReturn(Integer.class.getDeclaredMethod("intValue"));
    when(projectionProvider2.name()).thenReturn(projectionName);
    when(unit2.projectionDefinitions()).thenReturn(List.of(projectionProvider2));

    var module = mock(Module.class);
    when(module.units()).thenReturn(List.of(unit1, unit2));

    // Then
    Assertions.assertThatThrownBy(() -> ModuleValidator.validate(module))
        .isExactlyInstanceOf(ConfigurationException.class)
        .hasMessage("Found multiple projections with same name: Projection name '" + projectionName + "', projection providers: " +
            unitClass1.getCanonicalName() + "#trim, " +
            unitClass2.getCanonicalName() + "#intValue");
  }

  @Test
  @SuppressWarnings("unchecked,rawtypes")
  public void testValidate_whenUnitProjectionMiss() {
    // Given
    Class unitClass = EmptyTestModule.class;
    String projectionName = "projection";

    var injection = mock(ProjectionInjection.class);
    when(injection.kind()).thenReturn(InjectionKinds.Projection);
    when(injection.name()).thenReturn(projectionName);
    when(injection.targetClass()).thenReturn((Class) String.class);
    when(injection.unitClass()).thenReturn(unitClass);

    var unit = mock(Unit.class);
    when(unit.isMain()).thenReturn(true);
    when(unit.unitClass()).thenReturn(unitClass);
    when(unit.injections()).thenReturn(List.of(injection));
    when(unit.projectionDefinitions()).thenReturn(List.of());

    var module = mock(Module.class);
    when(module.units()).thenReturn(List.of(unit));

    // Then
    Assertions.assertThatThrownBy(() -> ModuleValidator.validate(module))
        .isExactlyInstanceOf(ConfigurationException.class)
        .hasMessage("Projection injection by name '" + projectionName +
            "' declared in unit " + EmptyTestModule.class.getCanonicalName() + " is not found");
  }

  @Test
  @SuppressWarnings("unchecked,rawtypes")
  public void testValidate_whenUnitProjectionTypeIncompatible() {
    // Given
    Class unitClass = EmptyTestModule.class;
    String projectionName = "projection";
    Class projectionType = String.class;
    Class injectionType = Integer.class;

    var injection = mock(ProjectionInjection.class);
    when(injection.kind()).thenReturn(InjectionKinds.Projection);
    when(injection.name()).thenReturn(projectionName);
    when(injection.targetClass()).thenReturn(injectionType);
    when(injection.unitClass()).thenReturn(unitClass);

    var projectionProvider = mock(UnitProjectionDefinition.class);
    when(projectionProvider.name()).thenReturn(projectionName);
    when(projectionProvider.type()).thenReturn(projectionType);

    var unit = mock(Unit.class);
    when(unit.isMain()).thenReturn(true);
    when(unit.unitClass()).thenReturn(unitClass);
    when(unit.injections()).thenReturn(List.of(injection));
    when(unit.projectionDefinitions()).thenReturn(List.of(projectionProvider));

    var module = mock(Module.class);
    when(module.units()).thenReturn(List.of(unit));

    // Then
    Assertions.assertThatThrownBy(() -> ModuleValidator.validate(module))
        .isExactlyInstanceOf(ConfigurationException.class)
        .hasMessage("Projection injection '" + projectionName + "' declared in unit " + unitClass.getCanonicalName() + " has an incompatible target type. " +
            "Expected type " + injectionType.getCanonicalName() + ", actual type " + projectionType.getCanonicalName());
  }

  @Test
  @SuppressWarnings("unchecked,rawtypes")
  public void testValidate_whenValid() throws Exception {
    // Given
    var unit1 = mock(Unit.class);
    Class unitClass1 = String.class;
    when(unit1.isMain()).thenReturn(true);
    when(unit1.unitClass()).thenReturn(unitClass1);

    var projectionProvider1 = mock(UnitProjectionDefinition.class);
    when(projectionProvider1.name()).thenReturn("projection1");
    when(projectionProvider1.unitClass()).thenReturn(unitClass1);
    when(projectionProvider1.projectionMethod()).thenReturn(String.class.getDeclaredMethod("trim"));
    when(unit1.projectionDefinitions()).thenReturn(List.of(projectionProvider1));

    var unit2 = mock(Unit.class);
    Class unitClass2 = Integer.class;
    when(unit2.isMain()).thenReturn(false);
    when(unit2.unitClass()).thenReturn(unitClass2);
    when(unit2.shutdownAction()).thenReturn(Optional.empty());
    var projectionProvider2 = mock(UnitProjectionDefinition.class);
    when(projectionProvider2.unitClass()).thenReturn(unitClass2);
    when(projectionProvider2.projectionMethod()).thenReturn(Integer.class.getDeclaredMethod("intValue"));
    when(projectionProvider2.name()).thenReturn("projection2");
    when(unit2.projectionDefinitions()).thenReturn(List.of(projectionProvider2));

    var module = mock(Module.class);
    when(module.units()).thenReturn(List.of(unit1, unit2));

    // Then
    Assertions.assertThatCode(() -> ModuleValidator.validate(module))
        .doesNotThrowAnyException();
  }
}
