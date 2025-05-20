package tech.intellispaces.reflections.framework.engine.impl;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import tech.intellispaces.reflections.framework.action.InvokeUnitMethodAction;
import tech.intellispaces.reflections.framework.engine.Engine;
import tech.intellispaces.reflections.framework.exception.ConfigurationException;
import tech.intellispaces.reflections.framework.exception.CyclicDependencyException;
import tech.intellispaces.reflections.framework.system.ModuleFactory;
import tech.intellispaces.reflections.framework.system.ModuleHandleImpl;
import tech.intellispaces.reflections.framework.testsamples.reflection.ReflectionOfEmptyDomain;
import tech.intellispaces.reflections.framework.testsamples.system.EmptyTestModule;
import tech.intellispaces.reflections.framework.testsamples.system.ModuleWithProjectionProvidersWithSelfCyclicDependency;
import tech.intellispaces.reflections.framework.testsamples.system.ModuleWithSimpleProjections;
import tech.intellispaces.reflections.framework.testsamples.system.ModuleWithStartupAndShutdownMethods;
import tech.intellispaces.reflections.framework.testsamples.system.ModuleWithThreeProjectionProvidersWithCyclicDependency;
import tech.intellispaces.reflections.framework.testsamples.system.ModuleWithTwoProjectionProvidersInDifferentUnitsWithCyclicDependency1;
import tech.intellispaces.reflections.framework.testsamples.system.ModuleWithTwoProjectionProvidersInDifferentUnitsWithCyclicDependency2;
import tech.intellispaces.reflections.framework.testsamples.system.ModuleWithTwoProjectionProvidersInDifferentUnitsWithCyclicDependency3;
import tech.intellispaces.reflections.framework.testsamples.system.ModuleWithTwoProjectionProvidersWithCyclicDependency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static tech.intellispaces.reflections.framework.engine.EngineLoader.loadEngine;

/**
 * Integration tests for {@link ModuleFactory}, {@link DefaultEngine} classes.
 */
public class ModuleFactoryTest {

  @Test
  public void testCreateModule_whenOneEmptyUnit() {
    // When
    Engine engine = new DefaultEngineFactory().create(null);
    ModuleHandleImpl module = ModuleFactory.createModule(List.of(EmptyTestModule.class), engine);
    loadEngine(engine, module);

    // Then
    assertThat(module.unitHandles()).hasSize(1);
    assertThat(module.unitHandles().get(0).unitClass()).isSameAs(EmptyTestModule.class);
    assertThat(module.unitHandles().get(0).startupAction()).isEmpty();
    assertThat(module.unitHandles().get(0).startupAction()).isEmpty();
    Assertions.assertThat(module.moduleProjections()).isEmpty();
  }

  @Test
  public void testCreateModule_whenOneUnit_andStartup_andShutdownMethods() {
    // When
    Engine engine = new DefaultEngineFactory().create(null);
    ModuleHandleImpl module = ModuleFactory.createModule(List.of(ModuleWithStartupAndShutdownMethods.class), engine);
    loadEngine(engine, module);

    // Then
    assertThat(module.unitHandles()).hasSize(1);
    assertThat(module.unitHandles().get(0).unitClass()).isSameAs(ModuleWithStartupAndShutdownMethods.class);
    assertThat(module.unitHandles().get(0).startupAction()).isPresent();
    assertThat(((InvokeUnitMethodAction<?>) module.unitHandles().get(0).startupAction().orElseThrow()).method().name()).isEqualTo("startup");
    assertThat(module.unitHandles().get(0).shutdownAction()).isPresent();
    assertThat(((InvokeUnitMethodAction<?>) module.unitHandles().get(0).shutdownAction().orElseThrow()).method().name()).isEqualTo("shutdown");
    Assertions.assertThat(module.moduleProjections()).isEmpty();
  }

  @Test
  public void testCreateModule_whenOneUnit_andSimpleProjections() {
    // When
    Engine engine = new DefaultEngineFactory().create(null);
    ModuleHandleImpl module = ModuleFactory.createModule(List.of(ModuleWithSimpleProjections.class), engine);
    loadEngine(engine, module);

    // Then
    assertThat(module.unitHandles()).hasSize(1);
    assertThat(module.unitHandles().get(0).unitClass()).isSameAs(ModuleWithSimpleProjections.class);
    assertThat(module.unitHandles().get(0).startupAction()).isEmpty();
    assertThat(module.unitHandles().get(0).shutdownAction()).isEmpty();
    assertThat(module.moduleProjections()).hasSize(17);

    Assertions.assertThat(module.getProjection("booleanProjection1", boolean.class)).isEqualTo(true);
    Assertions.assertThat(module.getProjection("booleanProjection1", Boolean.class)).isEqualTo(true);

    Assertions.assertThat(module.getProjection("byteProjection1", byte.class)).isEqualTo((byte) 1);
    Assertions.assertThat(module.getProjection("byteProjection1", Byte.class)).isEqualTo((byte) 1);

    Assertions.assertThat(module.getProjection("byteProjection2", byte.class)).isEqualTo((byte) 2);
    Assertions.assertThat(module.getProjection("byteProjection2", Byte.class)).isEqualTo((byte) 2);

    Assertions.assertThat(module.getProjection("shortProjection1", short.class)).isEqualTo((short) 3);
    Assertions.assertThat(module.getProjection("shortProjection1", Short.class)).isEqualTo((short) 3);

    Assertions.assertThat(module.getProjection("shortProjection2", short.class)).isEqualTo((short) 4);
    Assertions.assertThat(module.getProjection("shortProjection2", Short.class)).isEqualTo((short) 4);

    Assertions.assertThat(module.getProjection("intProjection1", int.class)).isEqualTo(5);
    Assertions.assertThat(module.getProjection("intProjection1", Integer.class)).isEqualTo(5);

    Assertions.assertThat(module.getProjection("intProjection2", int.class)).isEqualTo(6);
    Assertions.assertThat(module.getProjection("intProjection2", Integer.class)).isEqualTo(6);

    Assertions.assertThat(module.getProjection("longProjection1", long.class)).isEqualTo(7);
    Assertions.assertThat(module.getProjection("longProjection1", Long.class)).isEqualTo(7);

    Assertions.assertThat(module.getProjection("longProjection2", long.class)).isEqualTo(8);
    Assertions.assertThat(module.getProjection("longProjection2", Long.class)).isEqualTo(8);

    Assertions.assertThat(module.getProjection("floatProjection1", float.class)).isEqualTo(9.9f);
    Assertions.assertThat(module.getProjection("floatProjection1", Float.class)).isEqualTo(9.9f);

    Assertions.assertThat(module.getProjection("floatProjection2", float.class)).isEqualTo(10.1f);
    Assertions.assertThat(module.getProjection("floatProjection2", Float.class)).isEqualTo(10.1f);

    Assertions.assertThat(module.getProjection("doubleProjection1", double.class)).isEqualTo(11.11);
    Assertions.assertThat(module.getProjection("doubleProjection1", Double.class)).isEqualTo(11.11);

    Assertions.assertThat(module.getProjection("doubleProjection2", double.class)).isEqualTo(12.12);
    Assertions.assertThat(module.getProjection("doubleProjection2", Double.class)).isEqualTo(12.12);

    Assertions.assertThat(module.getProjection("charProjection1", char.class)).isEqualTo('a');
    Assertions.assertThat(module.getProjection("charProjection1", Character.class)).isEqualTo('a');

    Assertions.assertThat(module.getProjection("charProjection2", char.class)).isEqualTo('b');
    Assertions.assertThat(module.getProjection("charProjection2", Character.class)).isEqualTo('b');

    Assertions.assertThat(module.getProjection("stringProjection", String.class)).isEqualTo("string");

    Assertions.assertThat(module.getProjection("reflectionrojection", ReflectionOfEmptyDomain.class)).isNull();

    assertThat(module.moduleProjections()).hasSize(17);
  }

  @Test
  public void testCreateModule_whenOneUnit_andProjectionProvidersWithSelfCyclicDependency() {
    Engine engine = new DefaultEngineFactory().create(null);
    ModuleHandleImpl module = ModuleFactory.createModule(List.of(ModuleWithProjectionProvidersWithSelfCyclicDependency.class), engine);
    loadEngine(engine, module);

    assertThatThrownBy(module::start)
        .isExactlyInstanceOf(CyclicDependencyException.class)
        .hasMessage("Cyclic dependency between projections 'projection1' and 'projection1' in unit " +
            ModuleWithProjectionProvidersWithSelfCyclicDependency.class.getCanonicalName()
            + ". Dependency path: 'projection1' -> 'projection1'");
  }

  @Test
  public void testCreateModule_whenOneUnit_andTwoProjectionProvidersWithCyclicDependency() {
    Engine engine = new DefaultEngineFactory().create(null);
    ModuleHandleImpl module = ModuleFactory.createModule(List.of(ModuleWithTwoProjectionProvidersWithCyclicDependency.class), engine);
    loadEngine(engine, module);

    assertThatThrownBy(module::start)
        .isExactlyInstanceOf(CyclicDependencyException.class)
        .hasMessage("Cyclic dependency between projections 'projection1' and 'projection2' in unit " +
            ModuleWithTwoProjectionProvidersWithCyclicDependency.class.getCanonicalName()
            + ". Dependency path: 'projection1' -> 'projection2' -> 'projection1'");
  }

  @Test
  public void testCreateModule_whenOneUnit_andThreeProjectionProvidersWithCyclicDependency() {
    Engine engine = new DefaultEngineFactory().create(null);
    ModuleHandleImpl module = ModuleFactory.createModule(List.of(ModuleWithThreeProjectionProvidersWithCyclicDependency.class), engine);
    loadEngine(engine, module);

    assertThatThrownBy(module::start)
        .isExactlyInstanceOf(CyclicDependencyException.class)
        .hasMessage("Cyclic dependency between projections 'projection1' and 'projection3' in unit " +
            ModuleWithThreeProjectionProvidersWithCyclicDependency.class.getCanonicalName()
            + ". Dependency path: 'projection1' -> 'projection2' -> 'projection3' -> 'projection1'");
  }

  @Test
  public void testCreateModule_whenTwoUnit_andTwoProjectionProvidersInDifferentUnitsWithCyclicDependency1() {
    Engine engine = new DefaultEngineFactory().create(null);
    ModuleHandleImpl module = ModuleFactory.createModule(List.of(ModuleWithTwoProjectionProvidersInDifferentUnitsWithCyclicDependency1.ModuleSample.class), engine);
    loadEngine(engine, module);

    assertThatThrownBy(module::start)
        .isExactlyInstanceOf(CyclicDependencyException.class)
        .hasMessage("Cyclic dependency between projection 'projection1' in unit " +
            ModuleWithTwoProjectionProvidersInDifferentUnitsWithCyclicDependency1.ModuleSample.class.getCanonicalName()
            + " and projection 'projection2' in unit " +
            ModuleWithTwoProjectionProvidersInDifferentUnitsWithCyclicDependency1.UnitSample.class.getCanonicalName()
            + ". Dependency path: 'projection1' -> 'projection2' -> 'projection1'");
  }

  @Test
  public void testCreateModule_whenTwoUnit_andTwoProjectionProvidersInDifferentUnitsWithCyclicDependency2() {
    Engine engine = new DefaultEngineFactory().create(null);
    ModuleHandleImpl module = ModuleFactory.createModule(List.of(ModuleWithTwoProjectionProvidersInDifferentUnitsWithCyclicDependency2.ModuleSample.class), engine);
    loadEngine(engine, module);

    assertThatThrownBy(module::start)
        .isExactlyInstanceOf(CyclicDependencyException.class)
        .hasMessage("Cyclic dependency between projections 'projection2' and 'projection2' in unit " +
            ModuleWithTwoProjectionProvidersInDifferentUnitsWithCyclicDependency2.UnitSample.class.getCanonicalName()
            + ". Dependency path: 'projection2' -> 'projection2'");
  }

  @Test
  public void testCreateModule_whenTwoUnit_andTwoProjectionProvidersInDifferentUnitsWithCyclicDependency3() {
    Engine engine = new DefaultEngineFactory().create(null);
    ModuleHandleImpl module = ModuleFactory.createModule(List.of(ModuleWithTwoProjectionProvidersInDifferentUnitsWithCyclicDependency3.ModuleSample.class), engine);
    loadEngine(engine, module);

    assertThatThrownBy(module::start)
        .isExactlyInstanceOf(ConfigurationException.class)
        .hasMessage("Cannot to resolve required projection 'projection3' in projection definition 'projection2' of unit " +
            ModuleWithTwoProjectionProvidersInDifferentUnitsWithCyclicDependency3.UnitSample.class.getCanonicalName());
  }
}
