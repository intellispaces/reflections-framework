package tech.intellispacesframework.core.system;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import tech.intellispacesframework.core.exception.ConfigurationException;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link ModuleValidator} class.
 */
public class ModuleValidatorTest {
  private final ModuleValidator validator = new ModuleValidator();

  @Test
  public void testValidateModule_whenNoMainUnits() {
    // Given
    Unit unit1 = mock(Unit.class);
    when(unit1.isMain()).thenReturn(false);

    Unit unit2 = mock(Unit.class);
    when(unit2.isMain()).thenReturn(false);

    Module module = mock(Module.class);
    when(module.units()).thenReturn(List.of(unit1, unit2));

    // Then
    Assertions.assertThatThrownBy(() -> validator.validateModule(module))
        .isExactlyInstanceOf(ConfigurationException.class)
        .hasMessage("Main unit was not found");
  }

  @Test
  @SuppressWarnings("unchecked,rawtypes")
  public void testValidateModule_whenTwoMainUnits() {
    // Given
    Unit unit1 = mock(Unit.class);
    when(unit1.unitClass()).thenReturn((Class) String.class);
    when(unit1.isMain()).thenReturn(true);

    Unit unit2 = mock(Unit.class);
    when(unit2.unitClass()).thenReturn((Class) Integer.class);
    when(unit2.isMain()).thenReturn(true);

    Module module = mock(Module.class);
    when(module.units()).thenReturn(List.of(unit1, unit2));

    // Then
    Assertions.assertThatThrownBy(() -> validator.validateModule(module))
        .isExactlyInstanceOf(ConfigurationException.class)
        .hasMessage("Multiple main units found: " + String.class.getSimpleName() + ", " + Integer.class.getSimpleName());
  }

  @Test
  public void testValidateModule_whenTwoStartupMethods() throws Exception {
    // Given
    Unit unit1 = mock(Unit.class);
    when(unit1.isMain()).thenReturn(true);
    when(unit1.startupMethod()).thenReturn(Optional.of(String.class.getDeclaredMethod("isEmpty")));

    Unit unit2 = mock(Unit.class);
    when(unit2.isMain()).thenReturn(false);
    when(unit2.startupMethod()).thenReturn(Optional.of(String.class.getDeclaredMethod("isBlank")));

    Module module = mock(Module.class);
    when(module.units()).thenReturn(List.of(unit1, unit2));

    // Then
    Assertions.assertThatThrownBy(() -> validator.validateModule(module))
        .isExactlyInstanceOf(ConfigurationException.class)
        .hasMessage("Multiple startup methods found: String#isEmpty, String#isBlank");
  }

  @Test
  public void testValidateModule_whenTwoShutdownMethods() throws Exception {
    // Given
    Unit unit1 = mock(Unit.class);
    when(unit1.isMain()).thenReturn(true);
    when(unit1.shutdownMethod()).thenReturn(Optional.of(String.class.getDeclaredMethod("isEmpty")));

    Unit unit2 = mock(Unit.class);
    when(unit2.isMain()).thenReturn(false);
    when(unit2.shutdownMethod()).thenReturn(Optional.of(String.class.getDeclaredMethod("isBlank")));

    Module module = mock(Module.class);
    when(module.units()).thenReturn(List.of(unit1, unit2));

    // Then
    Assertions.assertThatThrownBy(() -> validator.validateModule(module))
        .isExactlyInstanceOf(ConfigurationException.class)
        .hasMessage("Multiple shutdown methods found: String#isEmpty, String#isBlank");
  }

  @Test
  @SuppressWarnings("unchecked,rawtypes")
  public void testValidateModule_whenTwoProjectionsWithSameName() throws Exception {
    // Given
    Unit unit1 = mock(Unit.class);
    when(unit1.isMain()).thenReturn(true);
    when(unit1.unitClass()).thenReturn((Class) String.class);
    when(unit1.shutdownMethod()).thenReturn(Optional.empty());
    UnitProjectionProvider projectionProvider1 = mock(UnitProjectionProvider.class);
    when(projectionProvider1.name()).thenReturn("projection1");
    when(projectionProvider1.unit()).thenReturn(unit1);
    when(projectionProvider1.providerMethod()).thenReturn(String.class.getDeclaredMethod("trim"));
    when(unit1.projectionProviders()).thenReturn(List.of(projectionProvider1));

    Unit unit2 = mock(Unit.class);
    when(unit2.isMain()).thenReturn(false);
    when(unit2.unitClass()).thenReturn((Class) Integer.class);
    when(unit2.shutdownMethod()).thenReturn(Optional.empty());
    UnitProjectionProvider projectionProvider2 = mock(UnitProjectionProvider.class);
    when(projectionProvider2.unit()).thenReturn(unit1);
    when(projectionProvider2.providerMethod()).thenReturn(Integer.class.getDeclaredMethod("intValue"));
    when(projectionProvider2.name()).thenReturn("projection1");
    when(unit2.projectionProviders()).thenReturn(List.of(projectionProvider2));

    Module module = mock(Module.class);
    when(module.units()).thenReturn(List.of(unit1, unit2));

    // Then
    Assertions.assertThatThrownBy(() -> validator.validateModule(module))
        .isExactlyInstanceOf(ConfigurationException.class)
        .hasMessage("Found multiple projections with same name: Projection name 'projection1', projection providers: java.lang.String#trim, java.lang.String#intValue");
  }

  @Test
  @SuppressWarnings("unchecked,rawtypes")
  public void testValidateModule_whenValid() throws Exception {
    // Given
    Unit unit1 = mock(Unit.class);
    when(unit1.isMain()).thenReturn(true);
    when(unit1.unitClass()).thenReturn((Class) String.class);
    when(unit1.startupMethod()).thenReturn(Optional.of(String.class.getDeclaredMethod("isEmpty")));
    when(unit1.shutdownMethod()).thenReturn(Optional.of(String.class.getDeclaredMethod("isBlank")));
    UnitProjectionProvider projectionProvider1 = mock(UnitProjectionProvider.class);
    when(projectionProvider1.name()).thenReturn("projection1");
    when(projectionProvider1.unit()).thenReturn(unit1);
    when(projectionProvider1.providerMethod()).thenReturn(String.class.getDeclaredMethod("trim"));
    when(unit1.projectionProviders()).thenReturn(List.of(projectionProvider1));

    Unit unit2 = mock(Unit.class);
    when(unit2.isMain()).thenReturn(false);
    when(unit2.unitClass()).thenReturn((Class) Integer.class);
    when(unit2.shutdownMethod()).thenReturn(Optional.empty());
    UnitProjectionProvider projectionProvider2 = mock(UnitProjectionProvider.class);
    when(projectionProvider2.unit()).thenReturn(unit1);
    when(projectionProvider2.providerMethod()).thenReturn(Integer.class.getDeclaredMethod("intValue"));
    when(projectionProvider2.name()).thenReturn("projection2");
    when(unit2.projectionProviders()).thenReturn(List.of(projectionProvider2));

    Module module = mock(Module.class);
    when(module.units()).thenReturn(List.of(unit1, unit2));

    // Then
    Assertions.assertThatCode(() -> validator.validateModule(module))
        .doesNotThrowAnyException();
  }
}
