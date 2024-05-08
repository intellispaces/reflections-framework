package tech.intellispacesframework.core.system;

import org.junit.jupiter.api.Test;
import tech.intellispacesframework.core.samples.domain.DomainEmpty;
import tech.intellispacesframework.core.samples.object.ObjectHandleOfDomainEmpty;
import tech.intellispacesframework.core.samples.system.EmptyModule;
import tech.intellispacesframework.core.samples.system.ModuleWithStartupSndShutdownMethods;
import tech.intellispacesframework.core.samples.system.ModuleWithValidProjectionReturnTypes;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ModuleDefaultFactory} class.
 */
public class ModuleDefaultFactoryTest {
  private final ModuleDefaultFactory factory = ModuleFactories.buildSystemModuleDefaultFactory();

  @Test
  public void testCreateEmptyModule() {
    // When
    ModuleDefault module = factory.createModule(EmptyModule.class, null);

    // Then
    assertThat(module.isStarted()).isFalse();
    assertThat(module.units()).hasSize(1);
    assertThat(module.units().get(0).unitClass()).isSameAs(EmptyModule.class);
    assertThat(module.units().get(0).startupMethod()).isEmpty();
    assertThat(module.units().get(0).shutdownMethod()).isEmpty();
    assertThat(module.projectionRegistry().allProjections()).isEmpty();
  }

  @Test
  public void testCreateModuleWithStartupAndShutdownMethods() {
    // When
    ModuleDefault module = factory.createModule(ModuleWithStartupSndShutdownMethods.class, null);

    // Then
    assertThat(module.isStarted()).isFalse();
    assertThat(module.units()).hasSize(1);
    assertThat(module.units().get(0).unitClass()).isSameAs(ModuleWithStartupSndShutdownMethods.class);
    assertThat(module.units().get(0).startupMethod()).isPresent();
    assertThat(module.units().get(0).startupMethod().orElseThrow().getName()).isEqualTo("startup");
    assertThat(module.units().get(0).shutdownMethod()).isPresent();
    assertThat(module.units().get(0).shutdownMethod().orElseThrow().getName()).isEqualTo("shutdown");
    assertThat(module.projectionRegistry().allProjections()).isEmpty();
  }

  @Test
  public void testCreateModule_whenUnitWithValidProjections() {
    // When
    ModuleDefault module = factory.createModule(ModuleWithValidProjectionReturnTypes.class, null);

    // Then
    assertThat(module.isStarted()).isFalse();
    assertThat(module.units()).hasSize(1);
    assertThat(module.units().get(0).unitClass()).isSameAs(ModuleWithValidProjectionReturnTypes.class);
    assertThat(module.units().get(0).startupMethod()).isEmpty();
    assertThat(module.units().get(0).shutdownMethod()).isEmpty();
    assertThat(module.projectionRegistry().allProjections()).hasSize(18);

    assertThat(module.projection("booleanProjection1", boolean.class)).isPresent().contains(true);
    assertThat(module.projection("booleanProjection1", Boolean.class)).isPresent().contains(true);

    assertThat(module.projection("byteProjection1", byte.class)).isPresent().contains((byte) 1);
    assertThat(module.projection("byteProjection1", Byte.class)).isPresent().contains((byte) 1);

    assertThat(module.projection("byteProjection2", byte.class)).isPresent().contains((byte) 2);
    assertThat(module.projection("byteProjection2", Byte.class)).isPresent().contains((byte) 2);

    assertThat(module.projection("shortProjection1", short.class)).isPresent().contains((short) 3);
    assertThat(module.projection("shortProjection1", Short.class)).isPresent().contains((short) 3);

    assertThat(module.projection("shortProjection2", short.class)).isPresent().contains((short) 4);
    assertThat(module.projection("shortProjection2", Short.class)).isPresent().contains((short) 4);

    assertThat(module.projection("intProjection1", int.class)).isPresent().contains(5);
    assertThat(module.projection("intProjection1", Integer.class)).isPresent().contains(5);

    assertThat(module.projection("intProjection2", int.class)).isPresent().contains(6);
    assertThat(module.projection("intProjection2", Integer.class)).isPresent().contains(6);

    assertThat(module.projection("longProjection1", long.class)).isPresent().contains((long) 7);
    assertThat(module.projection("longProjection1", Long.class)).isPresent().contains((long) 7);

    assertThat(module.projection("longProjection2", long.class)).isPresent().contains((long) 8);
    assertThat(module.projection("longProjection2", Long.class)).isPresent().contains((long) 8);

    assertThat(module.projection("floatProjection1", float.class)).isPresent().contains(9.9f);
    assertThat(module.projection("floatProjection1", Float.class)).isPresent().contains(9.9f);

    assertThat(module.projection("floatProjection2", float.class)).isPresent().contains(10.1f);
    assertThat(module.projection("floatProjection2", Float.class)).isPresent().contains(10.1f);

    assertThat(module.projection("doubleProjection1", double.class)).isPresent().contains(11.11);
    assertThat(module.projection("doubleProjection1", Double.class)).isPresent().contains(11.11);

    assertThat(module.projection("doubleProjection2", double.class)).isPresent().contains(12.12);
    assertThat(module.projection("doubleProjection2", Double.class)).isPresent().contains(12.12);

    assertThat(module.projection("charProjection1", char.class)).isPresent().contains('a');
    assertThat(module.projection("charProjection1", Character.class)).isPresent().contains('a');

    assertThat(module.projection("charProjection2", char.class)).isPresent().contains('b');
    assertThat(module.projection("charProjection2", Character.class)).isPresent().contains('b');

    assertThat(module.projection("stringProjection", String.class)).isPresent().contains("string");

    assertThat(module.projection("objectHandleProjection", ObjectHandleOfDomainEmpty.class)).isEmpty();

    assertThat(module.projection("domainProjection", DomainEmpty.class)).isEmpty();
  }
}
