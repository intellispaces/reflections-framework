package tech.intellispaces.framework.core.validate;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.CompilationSubject;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import tech.intellispaces.framework.core.exception.ConfigurationException;
import tech.intellispaces.framework.core.exception.IntelliSpacesException;
import tech.intellispaces.framework.core.samples.system.EmptyModule;
import tech.intellispaces.framework.core.samples.system.EmptyUnit;
import tech.intellispaces.framework.core.system.InjectionTypes;
import tech.intellispaces.framework.core.system.ModuleDefault;
import tech.intellispaces.framework.core.system.ProjectionInjection;
import tech.intellispaces.framework.core.system.Unit;
import tech.intellispaces.framework.core.system.UnitProjectionDefinition;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;

import javax.tools.JavaFileObject;
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
  private final ModuleValidator moduleValidator = new ModuleValidator();

  @Test
  public void testValidateModuleType_whenEmptyModule() {
    // Given
    CustomType moduleType = getModuleType("samples/EmptyModule.java");

    // Then
    Assertions.assertThatCode(() -> moduleValidator.validateModuleType(moduleType))
        .doesNotThrowAnyException();
  }

  @Test
  public void testValidateModuleType_whenModuleWithOneStartupMethod() {
    // Given
    CustomType moduleType = getModuleType("samples/ModuleWithOneStartupMethod.java");

    // Then
    Assertions.assertThatCode(() -> moduleValidator.validateModuleType(moduleType))
        .doesNotThrowAnyException();
  }

  @Test
  public void testValidateModuleType_whenModuleWithOneShutdownMethod() {
    // Given
    CustomType moduleType = getModuleType("samples/ModuleWithOneShutdownMethod.java");

    // Then
    Assertions.assertThatCode(() -> moduleValidator.validateModuleType(moduleType))
        .doesNotThrowAnyException();
  }

  @Test
  public void testValidateModuleType_whenTwoStartupMethods() {
    // Given
    CustomType moduleType = getModuleType("samples/ModuleWithTwoStartupMethods.java");

    // Then
    Assertions.assertThatThrownBy(() -> moduleValidator.validateModuleType(moduleType))
        .isExactlyInstanceOf(IntelliSpacesException.class)
        .hasMessage("Module unit samples.ModuleWithTwoStartupMethods contains more that one startup methods");
  }

  @Test
  public void testValidateModuleType_whenTwoShutdownMethods() {
    // Given
    CustomType moduleType = getModuleType("samples/ModuleWithTwoShutdownMethods.java");

    // Then
    Assertions.assertThatThrownBy(() -> moduleValidator.validateModuleType(moduleType))
        .isExactlyInstanceOf(IntelliSpacesException.class)
        .hasMessage("Module unit samples.ModuleWithTwoShutdownMethods contains more that one shutdown methods");
  }

  @Test
  public void testValidateModuleType_whenStartupMethodReturnedString() {
    // Given
    CustomType moduleType = getModuleType("samples/ModuleWithStartupMethodReturnedString.java");

    // Then
    Assertions.assertThatCode(() -> moduleValidator.validateModuleType(moduleType))
        .doesNotThrowAnyException();
  }

  @Test
  public void testValidateModuleType_whenStartupMethodAndStringParameter() {
    // Given
    CustomType moduleType = getModuleType("samples/ModuleWithStartupMethodAndStringParameter.java");

    // Then
    Assertions.assertThatCode(() -> moduleValidator.validateModuleType(moduleType))
        .doesNotThrowAnyException();
  }

  @Test
  public void testValidateModule_whenStartupMethodAndFileParameter() {
    // Given
    CustomType moduleType = getModuleType("samples/ModuleWithStartupMethodAndFileParameter.java");

    // Then
    Assertions.assertThatThrownBy(() -> moduleValidator.validateModuleType(moduleType))
        .isExactlyInstanceOf(IntelliSpacesException.class)
        .hasMessage("Parameter 'value' of method 'startup' in unit samples.ModuleWithStartupMethodAndFileParameter should be object handle or domain class");
  }

  @Test
  public void testValidateModuleType_whenShutdownMethodReturnedString() {
    // Given
    CustomType moduleType = getModuleType("samples/ModuleWithShutdownMethodReturnedString.java");

    // Then
    Assertions.assertThatCode(() -> moduleValidator.validateModuleType(moduleType))
        .doesNotThrowAnyException();
  }

  @Test
  public void testValidateModuleType_whenShutdownMethodAndStringParameter() {
    // Given
    CustomType moduleType = getModuleType("samples/ModuleWithShutdownMethodAndStringParameter.java");

    // Then
    Assertions.assertThatCode(() -> moduleValidator.validateModuleType(moduleType))
        .doesNotThrowAnyException();
  }

  @Test
  public void testValidateModuleType_whenShutdownMethodAndFileParameter() {
    // Given
    CustomType moduleType = getModuleType("samples/ModuleWithShutdownMethodAndFileParameter.java");

    // Then
    Assertions.assertThatThrownBy(() -> moduleValidator.validateModuleType(moduleType))
        .isExactlyInstanceOf(IntelliSpacesException.class)
        .hasMessage("Parameter 'value' of method 'shutdown' in unit samples.ModuleWithShutdownMethodAndFileParameter should be object handle or domain class");
  }

  @Test
  public void testValidateModuleType_whenAbstractMethodAndParameter() {
    // Given
    CustomType moduleType = getModuleType("samples/ModuleWithAbstractMethodAndParameter.java");

    // Then
    Assertions.assertThatThrownBy(() -> moduleValidator.validateModuleType(moduleType))
        .isExactlyInstanceOf(IntelliSpacesException.class)
        .hasMessage("Abstract method 'projection' in unit samples.ModuleWithAbstractMethodAndParameter should have no parameters");
  }

  @Test
  public void testValidateModuleType_whenValidProjections() {
    // Given
    CustomType moduleType = getModuleType("samples/ModuleWithSimpleProjections.java");

    // Then
    Assertions.assertThatCode(() -> moduleValidator.validateModuleType(moduleType))
        .doesNotThrowAnyException();
  }

  @Test
  public void testValidateModuleType_whenProjectionWithoutReturnedType() {
    // Given
    CustomType moduleType = getModuleType("samples/ModuleWithProjectionWithoutReturnedType.java");

    // Then
    Assertions.assertThatThrownBy(() -> moduleValidator.validateModuleType(moduleType))
        .isExactlyInstanceOf(IntelliSpacesException.class)
        .hasMessage("Method of the projection 'projection' in unit samples.ModuleWithProjectionWithoutReturnedType should return value");
  }

  @Test
  public void testValidateModuleType_whenProjectionAndStringParameter() {
    // Given
    CustomType moduleType = getModuleType("samples/ModuleWithProjectionAndStringParameter.java");

    // Then
    Assertions.assertThatCode(() -> moduleValidator.validateModuleType(moduleType))
        .doesNotThrowAnyException();
  }

  @Test
  public void testValidateModuleType_whenProjectionAndFileParameter() {
    // Given
    CustomType moduleType = getModuleType("samples/ModuleWithProjectionAndFileParameter.java");

    // Then
    Assertions.assertThatThrownBy(() -> moduleValidator.validateModuleType(moduleType))
        .isExactlyInstanceOf(IntelliSpacesException.class)
        .hasMessage("Parameter 'value' of method 'projection' in unit samples.ModuleWithProjectionAndFileParameter should be object handle or domain class");
  }

  @Test
  public void testValidateModuleType_whenAbstractProjection() {
    // Given
    CustomType moduleType = getModuleType("samples/ModuleWithAbstractProjection.java");

    // Then
    Assertions.assertThatThrownBy(() -> moduleValidator.validateModuleType(moduleType))
        .isExactlyInstanceOf(IntelliSpacesException.class)
        .hasMessage("Abstract projection method 'projection' in unit samples.ModuleWithAbstractProjection should have a Projection Definition annotation");
  }

  @Test
  public void testValidateModuleType_whenAbstractProjectionAndSingleProjectionDefinitionAnnotation() {
    // Given
    CustomType moduleType = getModuleType("samples/ModuleWithAbstractProjectionAndSingleProjectionDefinitionAnnotation.java");

    // Then
    Assertions.assertThatCode(() -> moduleValidator.validateModuleType(moduleType))
        .doesNotThrowAnyException();
  }

  @Test
  public void testValidateModuleType_whenAbstractProjectionAndSingleProjectionDefinitionAnnotationAndParameter() {
    // Given
    CustomType moduleType = getModuleType("samples/ModuleWithAbstractProjectionAndSingleProjectionDefinitionAnnotationAndParameter.java");

    // Then
    Assertions.assertThatThrownBy(() -> moduleValidator.validateModuleType(moduleType))
        .isExactlyInstanceOf(IntelliSpacesException.class)
        .hasMessage("Abstract method 'projection' in unit samples.ModuleWithAbstractProjectionAndSingleProjectionDefinitionAnnotationAndParameter should have no parameters");
  }




  @Test
  public void testValidateModuleType_whenUnitWithOneStartupMethod() {
    // Given
    CustomType moduleType = getModuleType("samples/UnitWithOneStartupMethod.java");

    // Then
    Assertions.assertThatThrownBy(() -> moduleValidator.validateModuleType(moduleType))
        .isExactlyInstanceOf(IntelliSpacesException.class)
        .hasMessage("Included unit should not have a starting method. But method 'startup' in unit samples.UnitWithOneStartupMethod.IncludedUnit is marked with annotation @Startup");
  }

  @Test
  public void testValidateModuleType_whenUnitWithOneShutdownMethod() {
    // Given
    CustomType moduleType = getModuleType("samples/UnitWithOneShutdownMethod.java");

    // Then
    Assertions.assertThatThrownBy(() -> moduleValidator.validateModuleType(moduleType))
        .isExactlyInstanceOf(IntelliSpacesException.class)
        .hasMessage("Included unit should not have a shutdown method. But method 'shutdown' in unit samples.UnitWithOneShutdownMethod.IncludedUnit is marked with annotation @Shutdown");
  }

  @Test
  public void testValidateModuleInstance_whenNoMainUnit() {
    // Given
    Unit unit1 = mock(Unit.class);
    when(unit1.isMain()).thenReturn(false);

    Unit unit2 = mock(Unit.class);
    when(unit2.isMain()).thenReturn(false);

    ModuleDefault module = mock(ModuleDefault.class);
    when(module.units()).thenReturn(List.of(unit1, unit2));

    // Then
    Assertions.assertThatThrownBy(() -> moduleValidator.validateModuleInstance(module))
        .isExactlyInstanceOf(ConfigurationException.class)
        .hasMessage("Main unit was not found");
  }

  @Test
  @SuppressWarnings("unchecked,rawtypes")
  public void testValidateModuleInstance_whenTwoMainUnits() {
    // Given
    Class unitClass1 = EmptyModule.class;
    Class unitClass2 = EmptyUnit.class;

    Unit unit1 = mock(Unit.class);
    when(unit1.unitClass()).thenReturn(unitClass1);
    when(unit1.isMain()).thenReturn(true);

    Unit unit2 = mock(Unit.class);
    when(unit2.unitClass()).thenReturn(unitClass2);
    when(unit2.isMain()).thenReturn(true);

    ModuleDefault module = mock(ModuleDefault.class);
    when(module.units()).thenReturn(List.of(unit1, unit2));

    // Then
    Assertions.assertThatThrownBy(() -> moduleValidator.validateModuleInstance(module))
        .isExactlyInstanceOf(ConfigurationException.class)
        .hasMessage("Multiple main units found: " + unitClass1.getSimpleName() + ", " + unitClass2.getSimpleName());
  }

  @Test
  @SuppressWarnings("unchecked,rawtypes")
  public void testValidateModuleInstance_whenTwoProjectionsWithSameName() throws Exception {
    // Given
    Class unitClass1 = EmptyModule.class;
    Class unitClass2 = EmptyUnit.class;
    String projectionName = "projection";

    Unit unit1 = mock(Unit.class);
    when(unit1.isMain()).thenReturn(true);
    when(unit1.unitClass()).thenReturn(unitClass1);
    when(unit1.shutdownMethod()).thenReturn(Optional.empty());
    UnitProjectionDefinition projectionProvider1 = mock(UnitProjectionDefinition.class);
    when(projectionProvider1.name()).thenReturn(projectionName);
    when(projectionProvider1.unit()).thenReturn(unit1);
    when(projectionProvider1.projectionMethod()).thenReturn(String.class.getDeclaredMethod("trim"));
    when(unit1.projectionProviders()).thenReturn(List.of(projectionProvider1));

    Unit unit2 = mock(Unit.class);
    when(unit2.isMain()).thenReturn(false);
    when(unit2.unitClass()).thenReturn(unitClass2);
    when(unit2.shutdownMethod()).thenReturn(Optional.empty());
    UnitProjectionDefinition projectionProvider2 = mock(UnitProjectionDefinition.class);
    when(projectionProvider2.unit()).thenReturn(unit2);
    when(projectionProvider2.projectionMethod()).thenReturn(Integer.class.getDeclaredMethod("intValue"));
    when(projectionProvider2.name()).thenReturn(projectionName);
    when(unit2.projectionProviders()).thenReturn(List.of(projectionProvider2));

    ModuleDefault module = mock(ModuleDefault.class);
    when(module.units()).thenReturn(List.of(unit1, unit2));

    // Then
    Assertions.assertThatThrownBy(() -> moduleValidator.validateModuleInstance(module))
        .isExactlyInstanceOf(ConfigurationException.class)
        .hasMessage("Found multiple projections with same name: Projection name '" + projectionName + "', projection providers: " +
            unitClass1.getCanonicalName() + "#trim, " +
            unitClass2.getCanonicalName() + "#intValue");
  }

  @Test
  @SuppressWarnings("unchecked,rawtypes")
  public void testValidateModuleInstance_whenUnitProjectionMiss() {
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

    ModuleDefault module = mock(ModuleDefault.class);
    when(module.units()).thenReturn(List.of(unit));

    // Then
    Assertions.assertThatThrownBy(() -> moduleValidator.validateModuleInstance(module))
        .isExactlyInstanceOf(ConfigurationException.class)
        .hasMessage("Projection injection by name '" + projectionName + "' declared in unit " + EmptyModule.class.getCanonicalName() + " was not found");
  }

  @Test
  @SuppressWarnings("unchecked,rawtypes")
  public void testValidateModuleInstance_whenUnitProjectionTypeIncompatible() {
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
    UnitProjectionDefinition projectionProvider = mock(UnitProjectionDefinition.class);
    when(projectionProvider.name()).thenReturn(projectionName);
    when(projectionProvider.type()).thenReturn(projectionType);

    Unit unit = mock(Unit.class);
    when(unit.isMain()).thenReturn(true);
    when(unit.unitClass()).thenReturn(unitClass);
    when(unit.startupMethod()).thenReturn(Optional.empty());
    when(unit.shutdownMethod()).thenReturn(Optional.empty());
    when(unit.injections()).thenReturn(List.of(injection));
    when(unit.projectionProviders()).thenReturn(List.of(projectionProvider));

    ModuleDefault module = mock(ModuleDefault.class);
    when(module.units()).thenReturn(List.of(unit));

    // Then
    Assertions.assertThatThrownBy(() -> moduleValidator.validateModuleInstance(module))
        .isExactlyInstanceOf(ConfigurationException.class)
        .hasMessage("Projection injection '" + projectionName + "' declared in unit " + unitClass.getCanonicalName() + " has an incompatible target type. " +
            "Expected type " + injectionType.getCanonicalName() + ", actual type " + projectionType.getCanonicalName());
  }

  @Test
  @SuppressWarnings("unchecked,rawtypes")
  public void testValidateModuleInstance_whenStartupMethodInjectionMiss() {
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

    ModuleDefault module = mock(ModuleDefault.class);
    when(module.units()).thenReturn(List.of(unit));

    // Then
    Assertions.assertThatThrownBy(() -> moduleValidator.validateModuleInstance(module))
        .isExactlyInstanceOf(ConfigurationException.class)
        .hasMessage("Injection '" + projectionName + "' required in method '" + startupMethodName +
            "' declared in unit " + unitClass.getCanonicalName() + " was not found");
  }

  @Test
  @SuppressWarnings("unchecked,rawtypes")
  public void testValidateModuleInstance_whenStartupMethodInjectionTypeIncompatible() {
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
    UnitProjectionDefinition projectionProvider = mock(UnitProjectionDefinition.class);
    when(projectionProvider.name()).thenReturn(projectionName);
    when(projectionProvider.type()).thenReturn(projectionType);

    Unit unit = mock(Unit.class);
    when(unit.isMain()).thenReturn(true);
    when(unit.unitClass()).thenReturn(unitClass);
    when(unit.startupMethod()).thenReturn(Optional.of(startupMethod));
    when(unit.shutdownMethod()).thenReturn(Optional.empty());
    when(unit.injections()).thenReturn(List.of());
    when(unit.projectionProviders()).thenReturn(List.of(projectionProvider));

    ModuleDefault module = mock(ModuleDefault.class);
    when(module.units()).thenReturn(List.of(unit));

    // Then
    Assertions.assertThatThrownBy(() -> moduleValidator.validateModuleInstance(module))
        .isExactlyInstanceOf(ConfigurationException.class)
        .hasMessage("Injection '" + projectionName + "' required in method '" + startupMethodName +
            "' declared in unit " + unitClass.getCanonicalName() + " has an incompatible target type. " +
            "Expected type " + injectionType.getCanonicalName() + ", actual type " + projectionType.getCanonicalName());
  }

  @Test
  @SuppressWarnings("unchecked,rawtypes")
  public void testValidateModuleInstance_whenShutdownMethodInjectionMiss() {
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

    ModuleDefault module = mock(ModuleDefault.class);
    when(module.units()).thenReturn(List.of(unit));

    // Then
    Assertions.assertThatThrownBy(() -> moduleValidator.validateModuleInstance(module))
        .isExactlyInstanceOf(ConfigurationException.class)
        .hasMessage("Injection '" + projectionName + "' required in method '" + shutdownMethodName +
            "' declared in unit " + unitClass.getCanonicalName() + " was not found");
  }

  @Test
  @SuppressWarnings("unchecked,rawtypes")
  public void testValidateModuleInstance_whenShutdownMethodInjectionTypeIncompatible() {
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
    UnitProjectionDefinition projectionProvider = mock(UnitProjectionDefinition.class);
    when(projectionProvider.name()).thenReturn(projectionName);
    when(projectionProvider.type()).thenReturn(projectionType);

    Unit unit = mock(Unit.class);
    when(unit.isMain()).thenReturn(true);
    when(unit.unitClass()).thenReturn(unitClass);
    when(unit.startupMethod()).thenReturn(Optional.empty());
    when(unit.shutdownMethod()).thenReturn(Optional.of(shutdownMethod));
    when(unit.injections()).thenReturn(List.of());
    when(unit.projectionProviders()).thenReturn(List.of(projectionProvider));

    ModuleDefault module = mock(ModuleDefault.class);
    when(module.units()).thenReturn(List.of(unit));

    // Then
    Assertions.assertThatThrownBy(() -> moduleValidator.validateModuleInstance(module))
        .isExactlyInstanceOf(ConfigurationException.class)
        .hasMessage("Injection '" + projectionName + "' required in method '" + shutdownMethodName +
            "' declared in unit " + unitClass.getCanonicalName() + " has an incompatible target type. " +
            "Expected type " + injectionType.getCanonicalName() + ", actual type " + projectionType.getCanonicalName());
  }

  @Test
  @SuppressWarnings("unchecked,rawtypes")
  public void testValidateModuleInstance_whenValid() throws Exception {
    // Given
    Unit unit1 = mock(Unit.class);
    when(unit1.isMain()).thenReturn(true);
    when(unit1.unitClass()).thenReturn((Class) String.class);
    when(unit1.startupMethod()).thenReturn(Optional.of(String.class.getDeclaredMethod("isEmpty")));
    when(unit1.shutdownMethod()).thenReturn(Optional.of(String.class.getDeclaredMethod("isBlank")));
    UnitProjectionDefinition projectionProvider1 = mock(UnitProjectionDefinition.class);
    when(projectionProvider1.name()).thenReturn("projection1");
    when(projectionProvider1.unit()).thenReturn(unit1);
    when(projectionProvider1.projectionMethod()).thenReturn(String.class.getDeclaredMethod("trim"));
    when(unit1.projectionProviders()).thenReturn(List.of(projectionProvider1));

    Unit unit2 = mock(Unit.class);
    when(unit2.isMain()).thenReturn(false);
    when(unit2.unitClass()).thenReturn((Class) Integer.class);
    when(unit2.shutdownMethod()).thenReturn(Optional.empty());
    UnitProjectionDefinition projectionProvider2 = mock(UnitProjectionDefinition.class);
    when(projectionProvider2.unit()).thenReturn(unit1);
    when(projectionProvider2.projectionMethod()).thenReturn(Integer.class.getDeclaredMethod("intValue"));
    when(projectionProvider2.name()).thenReturn("projection2");
    when(unit2.projectionProviders()).thenReturn(List.of(projectionProvider2));

    ModuleDefault module = mock(ModuleDefault.class);
    when(module.units()).thenReturn(List.of(unit1, unit2));

    // Then
    Assertions.assertThatCode(() -> moduleValidator.validateModuleInstance(module))
        .doesNotThrowAnyException();
  }

  private CustomType getModuleType(String testSamplePath) {
    var annotationProcessor = new SampleAnnotatedTypeProcessor();
    Compiler compiler = Compiler.javac().withProcessors(annotationProcessor);
    JavaFileObject sourceFile = JavaFileObjects.forResource(testSamplePath);
    Compilation compilation = compiler.compile(sourceFile);
    CompilationSubject.assertThat(compilation).succeeded();

    return annotationProcessor.getAnnotatedType();
  }
}
