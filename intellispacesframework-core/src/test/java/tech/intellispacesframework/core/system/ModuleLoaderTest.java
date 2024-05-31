package tech.intellispacesframework.core.system;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import tech.intellispacesframework.core.exception.ConfigurationException;
import tech.intellispacesframework.core.exception.CyclicDependencyException;
import tech.intellispacesframework.core.samples.domain.DomainEmpty;
import tech.intellispacesframework.core.samples.object.ObjectHandleOfDomainEmpty;
import tech.intellispacesframework.core.samples.system.EmptyModule;
import tech.intellispacesframework.core.samples.system.ModuleWithProjectionProvidersWithSelfCyclicDependency;
import tech.intellispacesframework.core.samples.system.ModuleWithSimpleProjections;
import tech.intellispacesframework.core.samples.system.ModuleWithStartupAndShutdownMethods;
import tech.intellispacesframework.core.samples.system.ModuleWithThreeProjectionProvidersWithCyclicDependency;
import tech.intellispacesframework.core.samples.system.ModuleWithTwoProjectionProvidersInDifferentUnitsWithCyclicDependency1;
import tech.intellispacesframework.core.samples.system.ModuleWithTwoProjectionProvidersInDifferentUnitsWithCyclicDependency2;
import tech.intellispacesframework.core.samples.system.ModuleWithTwoProjectionProvidersInDifferentUnitsWithCyclicDependency3;
import tech.intellispacesframework.core.samples.system.ModuleWithTwoProjectionProvidersWithCyclicDependency;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ModuleLoader} class.
 */
public class ModuleLoaderTest {

  @Test
  public void testCreateModule_whenOneEmptyUnit() {
    // When
    ModuleDefault module = ModuleLoader.loadDefaultModule(EmptyModule.class);

    // Then
    assertThat(module.units()).hasSize(1);
    assertThat(module.units().get(0).unitClass()).isSameAs(EmptyModule.class);
    assertThat(module.units().get(0).startupMethod()).isEmpty();
    assertThat(module.units().get(0).shutdownMethod()).isEmpty();
    assertThat(module.projectionRegistry().projections()).isEmpty();
  }

  @Test
  public void testCreateModule_whenOneUnit_andStartup_andShutdownMethods() {
    // When
    ModuleDefault module = ModuleLoader.loadDefaultModule(ModuleWithStartupAndShutdownMethods.class);

    // Then
    assertThat(module.units()).hasSize(1);
    assertThat(module.units().get(0).unitClass()).isSameAs(ModuleWithStartupAndShutdownMethods.class);
    assertThat(module.units().get(0).startupMethod()).isPresent();
    assertThat(module.units().get(0).startupMethod().orElseThrow().getName()).isEqualTo("startup");
    assertThat(module.units().get(0).shutdownMethod()).isPresent();
    assertThat(module.units().get(0).shutdownMethod().orElseThrow().getName()).isEqualTo("shutdown");
    assertThat(module.projectionRegistry().projections()).isEmpty();
  }

  @Test
  public void testCreateModule_whenOneUnit_andSimpleProjections() {
    // When
    ModuleDefault module = ModuleLoader.loadDefaultModule(ModuleWithSimpleProjections.class);

    // Then
    assertThat(module.units()).hasSize(1);
    assertThat(module.units().get(0).unitClass()).isSameAs(ModuleWithSimpleProjections.class);
    assertThat(module.units().get(0).startupMethod()).isEmpty();
    assertThat(module.units().get(0).shutdownMethod()).isEmpty();
    assertThat(module.projectionRegistry().projections()).isEmpty();

    assertThat(module.projectionRegistry().getProjectionTarget("booleanProjection1", boolean.class)).isEqualTo(true);
    assertThat(module.projectionRegistry().getProjectionTarget("booleanProjection1", Boolean.class)).isEqualTo(true);

    assertThat(module.projectionRegistry().getProjectionTarget("byteProjection1", byte.class)).isEqualTo((byte) 1);
    assertThat(module.projectionRegistry().getProjectionTarget("byteProjection1", Byte.class)).isEqualTo((byte) 1);

    assertThat(module.projectionRegistry().getProjectionTarget("byteProjection2", byte.class)).isEqualTo((byte) 2);
    assertThat(module.projectionRegistry().getProjectionTarget("byteProjection2", Byte.class)).isEqualTo((byte) 2);

    assertThat(module.projectionRegistry().getProjectionTarget("shortProjection1", short.class)).isEqualTo((short) 3);
    assertThat(module.projectionRegistry().getProjectionTarget("shortProjection1", Short.class)).isEqualTo((short) 3);

    assertThat(module.projectionRegistry().getProjectionTarget("shortProjection2", short.class)).isEqualTo((short) 4);
    assertThat(module.projectionRegistry().getProjectionTarget("shortProjection2", Short.class)).isEqualTo((short) 4);

    assertThat(module.projectionRegistry().getProjectionTarget("intProjection1", int.class)).isEqualTo(5);
    assertThat(module.projectionRegistry().getProjectionTarget("intProjection1", Integer.class)).isEqualTo(5);

    assertThat(module.projectionRegistry().getProjectionTarget("intProjection2", int.class)).isEqualTo(6);
    assertThat(module.projectionRegistry().getProjectionTarget("intProjection2", Integer.class)).isEqualTo(6);

    assertThat(module.projectionRegistry().getProjectionTarget("longProjection1", long.class)).isEqualTo(7);
    assertThat(module.projectionRegistry().getProjectionTarget("longProjection1", Long.class)).isEqualTo(7);

    assertThat(module.projectionRegistry().getProjectionTarget("longProjection2", long.class)).isEqualTo(8);
    assertThat(module.projectionRegistry().getProjectionTarget("longProjection2", Long.class)).isEqualTo(8);

    assertThat(module.projectionRegistry().getProjectionTarget("floatProjection1", float.class)).isEqualTo(9.9f);
    assertThat(module.projectionRegistry().getProjectionTarget("floatProjection1", Float.class)).isEqualTo(9.9f);

    assertThat(module.projectionRegistry().getProjectionTarget("floatProjection2", float.class)).isEqualTo(10.1f);
    assertThat(module.projectionRegistry().getProjectionTarget("floatProjection2", Float.class)).isEqualTo(10.1f);

    assertThat(module.projectionRegistry().getProjectionTarget("doubleProjection1", double.class)).isEqualTo(11.11);
    assertThat(module.projectionRegistry().getProjectionTarget("doubleProjection1", Double.class)).isEqualTo(11.11);

    assertThat(module.projectionRegistry().getProjectionTarget("doubleProjection2", double.class)).isEqualTo(12.12);
    assertThat(module.projectionRegistry().getProjectionTarget("doubleProjection2", Double.class)).isEqualTo(12.12);

    assertThat(module.projectionRegistry().getProjectionTarget("charProjection1", char.class)).isEqualTo('a');
    assertThat(module.projectionRegistry().getProjectionTarget("charProjection1", Character.class)).isEqualTo('a');

    assertThat(module.projectionRegistry().getProjectionTarget("charProjection2", char.class)).isEqualTo('b');
    assertThat(module.projectionRegistry().getProjectionTarget("charProjection2", Character.class)).isEqualTo('b');

    assertThat(module.projectionRegistry().getProjectionTarget("stringProjection", String.class)).isEqualTo("string");

    assertThat(module.projectionRegistry().getProjectionTarget("objectHandleProjection", ObjectHandleOfDomainEmpty.class)).isNull();

    assertThat(module.projectionRegistry().getProjectionTarget("domainProjection", DomainEmpty.class)).isNull();

    assertThat(module.projectionRegistry().projections()).hasSize(18);
  }

  @Test
  public void testCreateModule_whenOneUnit_andProjectionProvidersWithSelfCyclicDependency() {
    Module module = ModuleLoader.loadDefaultModule(ModuleWithProjectionProvidersWithSelfCyclicDependency.class);
    Assertions.assertThatThrownBy(module::run)
        .isExactlyInstanceOf(CyclicDependencyException.class)
        .hasMessage("Cyclic dependency between projections 'projection1' and 'projection1' in unit " + ModuleWithProjectionProvidersWithSelfCyclicDependency.class.getCanonicalName()
            + ". Dependency path: 'projection1' -> 'projection1'");
  }

  @Test
  public void testCreateModule_whenOneUnit_andTwoProjectionProvidersWithCyclicDependency() {
    Module module = ModuleLoader.loadDefaultModule(ModuleWithTwoProjectionProvidersWithCyclicDependency.class);
    Assertions.assertThatThrownBy(module::run)
        .isExactlyInstanceOf(CyclicDependencyException.class)
        .hasMessage("Cyclic dependency between projections 'projection1' and 'projection2' in unit " + ModuleWithTwoProjectionProvidersWithCyclicDependency.class.getCanonicalName()
            + ". Dependency path: 'projection1' -> 'projection2' -> 'projection1'");
  }

  @Test
  public void testCreateModule_whenOneUnit_andThreeProjectionProvidersWithCyclicDependency() {
    Module module = ModuleLoader.loadDefaultModule(ModuleWithThreeProjectionProvidersWithCyclicDependency.class);
    Assertions.assertThatThrownBy(module::run)
        .isExactlyInstanceOf(CyclicDependencyException.class)
        .hasMessage("Cyclic dependency between projections 'projection1' and 'projection3' in unit " + ModuleWithThreeProjectionProvidersWithCyclicDependency.class.getCanonicalName()
            + ". Dependency path: 'projection1' -> 'projection2' -> 'projection3' -> 'projection1'");
  }

  @Test
  public void testCreateModule_whenTwoUnit_andTwoProjectionProvidersInDifferentUnitsWithCyclicDependency1() {
    Module module = ModuleLoader.loadDefaultModule(ModuleWithTwoProjectionProvidersInDifferentUnitsWithCyclicDependency1.ModuleSample.class);
    Assertions.assertThatThrownBy(module::run)
        .isExactlyInstanceOf(CyclicDependencyException.class)
        .hasMessage("Cyclic dependency between projection 'projection1' in unit " + ModuleWithTwoProjectionProvidersInDifferentUnitsWithCyclicDependency1.ModuleSample.class.getCanonicalName()
            + " and projection 'projection2' in unit " + ModuleWithTwoProjectionProvidersInDifferentUnitsWithCyclicDependency1.UnitSample.class.getCanonicalName()
            + ". Dependency path: 'projection1' -> 'projection2' -> 'projection1'");
  }

  @Test
  public void testCreateModule_whenTwoUnit_andTwoProjectionProvidersInDifferentUnitsWithCyclicDependency2() {
    Module module = ModuleLoader.loadDefaultModule(ModuleWithTwoProjectionProvidersInDifferentUnitsWithCyclicDependency2.ModuleSample.class);
    Assertions.assertThatThrownBy(module::run)
        .isExactlyInstanceOf(CyclicDependencyException.class)
        .hasMessage("Cyclic dependency between projections 'projection2' and 'projection2' in unit " + ModuleWithTwoProjectionProvidersInDifferentUnitsWithCyclicDependency2.UnitSample.class.getCanonicalName()
            + ". Dependency path: 'projection2' -> 'projection2'");
  }

  @Test
  public void testCreateModule_whenTwoUnit_andTwoProjectionProvidersInDifferentUnitsWithCyclicDependency3() {
    Module module = ModuleLoader.loadDefaultModule(ModuleWithTwoProjectionProvidersInDifferentUnitsWithCyclicDependency3.ModuleSample.class);
    Assertions.assertThatThrownBy(module::run)
        .isExactlyInstanceOf(ConfigurationException.class)
        .hasMessage("Cannot to resolve parameter 'projection3' in method 'projection2' of unit " + ModuleWithTwoProjectionProvidersInDifferentUnitsWithCyclicDependency3.UnitSample.class.getCanonicalName());
  }
}
