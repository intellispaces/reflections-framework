package tech.intellispacesframework.core.system;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import tech.intellispacesframework.core.exception.ConfigurationException;
import tech.intellispacesframework.core.samples.system.EmptyModule;
import tech.intellispacesframework.core.samples.system.EmptyUnit;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
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
  public void testValidateModule_whenNoMainUnit() {
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
    Class unitClass1 = EmptyModule.class;
    Class unitClass2 = EmptyUnit.class;

    Unit unit1 = mock(Unit.class);
    when(unit1.unitClass()).thenReturn(unitClass1);
    when(unit1.isMain()).thenReturn(true);

    Unit unit2 = mock(Unit.class);
    when(unit2.unitClass()).thenReturn(unitClass2);
    when(unit2.isMain()).thenReturn(true);

    Module module = mock(Module.class);
    when(module.units()).thenReturn(List.of(unit1, unit2));

    // Then
    Assertions.assertThatThrownBy(() -> validator.validateModule(module))
        .isExactlyInstanceOf(ConfigurationException.class)
        .hasMessage("Multiple main units found: " + unitClass1.getSimpleName() + ", " + unitClass2.getSimpleName());
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
    Class unitClass1 = EmptyModule.class;
    Class unitClass2 = EmptyUnit.class;
    String projectionName = "projection";

    Unit unit1 = mock(Unit.class);
    when(unit1.isMain()).thenReturn(true);
    when(unit1.unitClass()).thenReturn(unitClass1);
    when(unit1.shutdownMethod()).thenReturn(Optional.empty());
    UnitProjectionProvider projectionProvider1 = mock(UnitProjectionProvider.class);
    when(projectionProvider1.name()).thenReturn(projectionName);
    when(projectionProvider1.unit()).thenReturn(unit1);
    when(projectionProvider1.providerMethod()).thenReturn(String.class.getDeclaredMethod("trim"));
    when(unit1.projectionProviders()).thenReturn(List.of(projectionProvider1));

    Unit unit2 = mock(Unit.class);
    when(unit2.isMain()).thenReturn(false);
    when(unit2.unitClass()).thenReturn(unitClass2);
    when(unit2.shutdownMethod()).thenReturn(Optional.empty());
    UnitProjectionProvider projectionProvider2 = mock(UnitProjectionProvider.class);
    when(projectionProvider2.unit()).thenReturn(unit2);
    when(projectionProvider2.providerMethod()).thenReturn(Integer.class.getDeclaredMethod("intValue"));
    when(projectionProvider2.name()).thenReturn(projectionName);
    when(unit2.projectionProviders()).thenReturn(List.of(projectionProvider2));

    Module module = mock(Module.class);
    when(module.units()).thenReturn(List.of(unit1, unit2));

    // Then
    Assertions.assertThatThrownBy(() -> validator.validateModule(module))
        .isExactlyInstanceOf(ConfigurationException.class)
        .hasMessage("Found multiple projections with same name: Projection name '" + projectionName + "', projection providers: " +
            unitClass1.getCanonicalName() + "#trim, " +
            unitClass2.getCanonicalName() + "#intValue");
  }

  @Test
  @SuppressWarnings("unchecked,rawtypes")
  public void testValidateModule_whenUnitProjectionInjectionMiss() {
    // Given
    Class unitClass = EmptyModule.class;
    String projectionName = "projection";

    ProjectionInjection injection = mock(ProjectionInjection.class);
    when(injection.type()).thenReturn(InjectionTypes.ProjectionInjection);
    when(injection.name()).thenReturn(projectionName);
    when(injection.targetClass()).thenReturn((Class) String.class);
    when(injection.unitClass()).thenReturn(unitClass);

    Unit unit = mock(Unit.class);
    when(unit.isMain()).thenReturn(true);
    when(unit.unitClass()).thenReturn(unitClass);
    when(unit.startupMethod()).thenReturn(Optional.empty());
    when(unit.shutdownMethod()).thenReturn(Optional.empty());
    when(unit.injections()).thenReturn(List.of(injection));
    when(unit.projectionProviders()).thenReturn(List.of());

    Module module = mock(Module.class);
    when(module.units()).thenReturn(List.of(unit));

    // Then
    Assertions.assertThatThrownBy(() -> validator.validateModule(module))
        .isExactlyInstanceOf(ConfigurationException.class)
        .hasMessage("Projection injection by name '" + projectionName + "' declared in unit " + EmptyModule.class.getCanonicalName() + " was not found");
  }

  @Test
  @SuppressWarnings("unchecked,rawtypes")
  public void testValidateModule_whenUnitProjectionInjectionTypeIncompatible() {
    // Given
    Class unitClass = EmptyModule.class;
    String projectionName = "projection";
    Class projectionType = String.class;
    Class injectionType = Integer.class;

    ProjectionInjection injection = mock(ProjectionInjection.class);
    when(injection.type()).thenReturn(InjectionTypes.ProjectionInjection);
    when(injection.name()).thenReturn(projectionName);
    when(injection.targetClass()).thenReturn(injectionType);
    when(injection.unitClass()).thenReturn(unitClass);

    Method providerMethod = mock((Method.class));
    when(providerMethod.getName()).thenReturn(projectionName);
    when(providerMethod.getReturnType()).thenReturn(projectionType);
    UnitProjectionProvider projectionProvider = mock(UnitProjectionProvider.class);
    when(projectionProvider.name()).thenReturn(projectionName);
    when(projectionProvider.type()).thenReturn(projectionType);

    Unit unit = mock(Unit.class);
    when(unit.isMain()).thenReturn(true);
    when(unit.unitClass()).thenReturn(unitClass);
    when(unit.startupMethod()).thenReturn(Optional.empty());
    when(unit.shutdownMethod()).thenReturn(Optional.empty());
    when(unit.injections()).thenReturn(List.of(injection));
    when(unit.projectionProviders()).thenReturn(List.of(projectionProvider));

    Module module = mock(Module.class);
    when(module.units()).thenReturn(List.of(unit));

    // Then
    Assertions.assertThatThrownBy(() -> validator.validateModule(module))
        .isExactlyInstanceOf(ConfigurationException.class)
        .hasMessage("Projection injection '" + projectionName + "' declared in unit " + unitClass.getCanonicalName() + " has an incompatible target type. " +
            "Expected type " + injectionType.getCanonicalName() + ", actual type " + projectionType.getCanonicalName());
  }

  @Test
  @SuppressWarnings("unchecked,rawtypes")
  public void testValidateModule_whenStartupMethodInjectionMiss() {
    // Given
    Class unitClass = EmptyModule.class;
    String startupMethodName = "startup";
    String projectionName = "projection";

    Parameter param = mock(Parameter.class);
    when(param.getName()).thenReturn(projectionName);

    Method startupMethod = mock(Method.class);
    when(startupMethod.getName()).thenReturn(startupMethodName);
    when(startupMethod.getDeclaringClass()).thenReturn(unitClass);
    when(startupMethod.getParameters()).thenReturn(new Parameter[] { param });

    Unit unit = mock(Unit.class);
    when(unit.isMain()).thenReturn(true);
    when(unit.unitClass()).thenReturn(unitClass);
    when(unit.startupMethod()).thenReturn(Optional.of(startupMethod));
    when(unit.shutdownMethod()).thenReturn(Optional.empty());
    when(unit.injections()).thenReturn(List.of());
    when(unit.projectionProviders()).thenReturn(List.of());

    Module module = mock(Module.class);
    when(module.units()).thenReturn(List.of(unit));

    // Then
    Assertions.assertThatThrownBy(() -> validator.validateModule(module))
        .isExactlyInstanceOf(ConfigurationException.class)
        .hasMessage("Injection '" + projectionName + "' required in method '" + startupMethodName +
            "' declared in unit " + unitClass.getCanonicalName() + " was not found");
  }

  @Test
  @SuppressWarnings("unchecked,rawtypes")
  public void testValidateModule_whenStartupMethodInjectionTypeIncompatible() {
    // Given
    Class unitClass = EmptyModule.class;
    String startupMethodName = "startup";
    String projectionName = "projection";
    Class projectionType = String.class;
    Class injectionType = Integer.class;

    Parameter param = mock(Parameter.class);
    when(param.getName()).thenReturn(projectionName);
    when(param.getType()).thenReturn(injectionType);

    Method startupMethod = mock(Method.class);
    when(startupMethod.getName()).thenReturn(startupMethodName);
    when(startupMethod.getDeclaringClass()).thenReturn(unitClass);
    when(startupMethod.getParameters()).thenReturn(new Parameter[] { param });

    Method providerMethod = mock((Method.class));
    when(providerMethod.getName()).thenReturn(projectionName);
    when(providerMethod.getReturnType()).thenReturn(projectionType);
    UnitProjectionProvider projectionProvider = mock(UnitProjectionProvider.class);
    when(projectionProvider.name()).thenReturn(projectionName);
    when(projectionProvider.type()).thenReturn(projectionType);

    Unit unit = mock(Unit.class);
    when(unit.isMain()).thenReturn(true);
    when(unit.unitClass()).thenReturn(unitClass);
    when(unit.startupMethod()).thenReturn(Optional.of(startupMethod));
    when(unit.shutdownMethod()).thenReturn(Optional.empty());
    when(unit.injections()).thenReturn(List.of());
    when(unit.projectionProviders()).thenReturn(List.of(projectionProvider));

    Module module = mock(Module.class);
    when(module.units()).thenReturn(List.of(unit));

    // Then
    Assertions.assertThatThrownBy(() -> validator.validateModule(module))
        .isExactlyInstanceOf(ConfigurationException.class)
        .hasMessage("Injection '" + projectionName + "' required in method '" + startupMethodName +
            "' declared in unit " + unitClass.getCanonicalName() + " has an incompatible target type. " +
            "Expected type " + injectionType.getCanonicalName() + ", actual type " + projectionType.getCanonicalName());
  }

  @Test
  @SuppressWarnings("unchecked,rawtypes")
  public void testValidateModule_whenShutdownMethodInjectionMiss() {
    // Given
    Class unitClass = EmptyModule.class;
    String shutdownMethodName = "shutdown";
    String projectionName = "projection";

    Parameter param = mock(Parameter.class);
    when(param.getName()).thenReturn(projectionName);

    Method shutdownMethod = mock(Method.class);
    when(shutdownMethod.getName()).thenReturn(shutdownMethodName);
    when(shutdownMethod.getDeclaringClass()).thenReturn(unitClass);
    when(shutdownMethod.getParameters()).thenReturn(new Parameter[] { param });

    Unit unit = mock(Unit.class);
    when(unit.isMain()).thenReturn(true);
    when(unit.unitClass()).thenReturn(unitClass);
    when(unit.startupMethod()).thenReturn(Optional.empty());
    when(unit.shutdownMethod()).thenReturn(Optional.of(shutdownMethod));
    when(unit.injections()).thenReturn(List.of());
    when(unit.projectionProviders()).thenReturn(List.of());

    Module module = mock(Module.class);
    when(module.units()).thenReturn(List.of(unit));

    // Then
    Assertions.assertThatThrownBy(() -> validator.validateModule(module))
        .isExactlyInstanceOf(ConfigurationException.class)
        .hasMessage("Injection '" + projectionName + "' required in method '" + shutdownMethodName +
            "' declared in unit " + unitClass.getCanonicalName() + " was not found");
  }

  @Test
  @SuppressWarnings("unchecked,rawtypes")
  public void testValidateModule_whenShutdownMethodInjectionTypeIncompatible() {
    // Given
    Class unitClass = EmptyModule.class;
    String shutdownMethodName = "shutdown";
    String projectionName = "projection";
    Class projectionType = String.class;
    Class injectionType = Integer.class;

    Parameter param = mock(Parameter.class);
    when(param.getName()).thenReturn(projectionName);
    when(param.getType()).thenReturn(injectionType);

    Method shutdownMethod = mock(Method.class);
    when(shutdownMethod.getName()).thenReturn(shutdownMethodName);
    when(shutdownMethod.getDeclaringClass()).thenReturn(unitClass);
    when(shutdownMethod.getParameters()).thenReturn(new Parameter[] { param });

    Method providerMethod = mock((Method.class));
    when(providerMethod.getName()).thenReturn(projectionName);
    when(providerMethod.getReturnType()).thenReturn(projectionType);
    UnitProjectionProvider projectionProvider = mock(UnitProjectionProvider.class);
    when(projectionProvider.name()).thenReturn(projectionName);
    when(projectionProvider.type()).thenReturn(projectionType);

    Unit unit = mock(Unit.class);
    when(unit.isMain()).thenReturn(true);
    when(unit.unitClass()).thenReturn(unitClass);
    when(unit.startupMethod()).thenReturn(Optional.empty());
    when(unit.shutdownMethod()).thenReturn(Optional.of(shutdownMethod));
    when(unit.injections()).thenReturn(List.of());
    when(unit.projectionProviders()).thenReturn(List.of(projectionProvider));

    Module module = mock(Module.class);
    when(module.units()).thenReturn(List.of(unit));

    // Then
    Assertions.assertThatThrownBy(() -> validator.validateModule(module))
        .isExactlyInstanceOf(ConfigurationException.class)
        .hasMessage("Injection '" + projectionName + "' required in method '" + shutdownMethodName +
            "' declared in unit " + unitClass.getCanonicalName() + " has an incompatible target type. " +
            "Expected type " + injectionType.getCanonicalName() + ", actual type " + projectionType.getCanonicalName());
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
