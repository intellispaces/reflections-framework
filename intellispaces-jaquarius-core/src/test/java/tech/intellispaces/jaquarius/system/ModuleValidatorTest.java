package tech.intellispaces.jaquarius.system;

///**
// * Tests for {@link ModuleValidator} class.
// */
public class ModuleValidatorTest {
//  private final ModuleValidator moduleValidator = new ModuleValidator();
//
//  @Test
//  public void testValidate_whenNoMainUnit() {
//    // Given
//    KernelUnit unit1 = mock(KernelUnit.class);
//    when(unit1.isMain()).thenReturn(false);
//
//    KernelUnit unit2 = mock(KernelUnit.class);
//    when(unit2.isMain()).thenReturn(false);
//
//    KernelModule module = mock(KernelModule.class);
//    when(module.units()).thenReturn(List.of(unit1, unit2));
//
//    // Then
//    Assertions.assertThatThrownBy(() -> moduleValidator.validate(module))
//        .isExactlyInstanceOf(ConfigurationException.class)
//        .hasMessage("Main unit is not found");
//  }
//
//  @Test
//  @SuppressWarnings("unchecked,rawtypes")
//  public void testValidate_whenTwoMainUnits() {
//    // Given
//    Class unitClass1 = EmptyModule.class;
//    Class unitClass2 = EmptyUnit.class;
//
//    KernelUnit unit1 = mock(KernelUnit.class);
//    when(unit1.unitClass()).thenReturn(unitClass1);
//    when(unit1.isMain()).thenReturn(true);
//
//    KernelUnit unit2 = mock(KernelUnit.class);
//    when(unit2.unitClass()).thenReturn(unitClass2);
//    when(unit2.isMain()).thenReturn(true);
//
//    KernelModule module = mock(KernelModule.class);
//    when(module.units()).thenReturn(List.of(unit1, unit2));
//
//    // Then
//    Assertions.assertThatThrownBy(() -> moduleValidator.validate(module))
//        .isExactlyInstanceOf(ConfigurationException.class)
//        .hasMessage("Multiple main units found: " + unitClass1.getSimpleName() + ", " + unitClass2.getSimpleName());
//  }
//
//  @Test
//  @SuppressWarnings("unchecked,rawtypes")
//  public void testValidate_whenTwoProjectionsWithSameName() throws Exception {
//    // Given
//    Class unitClass1 = EmptyModule.class;
//    Class unitClass2 = EmptyUnit.class;
//    String projectionName = "projection";
//
//    KernelUnit unit1 = mock(KernelUnit.class);
//    when(unit1.isMain()).thenReturn(true);
//    when(unit1.unitClass()).thenReturn(unitClass1);
//    when(unit1.shutdownAction()).thenReturn(Optional.empty());
//    UnitProjectionDefinition projectionProvider1 = mock(UnitProjectionDefinition.class);
//    when(projectionProvider1.name()).thenReturn(projectionName);
//    when(projectionProvider1.unitClass()).thenReturn(unitClass1);
//    when(projectionProvider1.projectionMethod()).thenReturn(String.class.getDeclaredMethod("trim"));
//    when(unit1.projectionDefinitions()).thenReturn(List.of(projectionProvider1));
//
//    KernelUnit unit2 = mock(KernelUnit.class);
//    when(unit2.isMain()).thenReturn(false);
//    when(unit2.unitClass()).thenReturn(unitClass2);
//    when(unit2.shutdownAction()).thenReturn(Optional.empty());
//    UnitProjectionDefinition projectionProvider2 = mock(UnitProjectionDefinition.class);
//    when(projectionProvider2.unitClass()).thenReturn(unitClass2);
//    when(projectionProvider2.projectionMethod()).thenReturn(Integer.class.getDeclaredMethod("intValue"));
//    when(projectionProvider2.name()).thenReturn(projectionName);
//    when(unit2.projectionDefinitions()).thenReturn(List.of(projectionProvider2));
//
//    KernelModule module = mock(KernelModule.class);
//    when(module.units()).thenReturn(List.of(unit1, unit2));
//
//    // Then
//    Assertions.assertThatThrownBy(() -> moduleValidator.validate(module))
//        .isExactlyInstanceOf(ConfigurationException.class)
//        .hasMessage("Found multiple projections with same name: Projection name '" + projectionName + "', projection providers: " +
//            unitClass1.getCanonicalName() + "#trim, " +
//            unitClass2.getCanonicalName() + "#intValue");
//  }
//
//  @Test
//  @SuppressWarnings("unchecked,rawtypes")
//  public void testValidate_whenUnitProjectionMiss() {
//    // Given
//    Class unitClass = EmptyModule.class;
//    String projectionName = "projection";
//
//    ProjectionInjection injection = mock(ProjectionInjection.class);
//    when(injection.kind()).thenReturn(InjectionKinds.ProjectionInjection);
//    when(injection.name()).thenReturn(projectionName);
//    when(injection.targetClass()).thenReturn((Class) String.class);
//    when(injection.unitClass()).thenReturn(unitClass);
//
//    KernelUnit unit = mock(KernelUnit.class);
//    when(unit.isMain()).thenReturn(true);
//    when(unit.unitClass()).thenReturn(unitClass);
//    when(unit.startupMethod()).thenReturn(Optional.empty());
//    when(unit.shutdownMethod()).thenReturn(Optional.empty());
//    when(unit.injections()).thenReturn(List.of(injection));
//    when(unit.projectionDefinitions()).thenReturn(List.of());
//
//    KernelModule module = mock(KernelModule.class);
//    when(module.units()).thenReturn(List.of(unit));
//
//    // Then
//    Assertions.assertThatThrownBy(() -> moduleValidator.validate(module))
//        .isExactlyInstanceOf(ConfigurationException.class)
//        .hasMessage("Projection injection by name '" + projectionName +
//            "' declared in unit " + EmptyModule.class.getCanonicalName() + " is not found");
//  }
//
//  @Test
//  @SuppressWarnings("unchecked,rawtypes")
//  public void testValidate_whenUnitProjectionTypeIncompatible() {
//    // Given
//    Class unitClass = EmptyModule.class;
//    String projectionName = "projection";
//    Class projectionType = String.class;
//    Class injectionType = Integer.class;
//
//    ProjectionInjection injection = mock(ProjectionInjection.class);
//    when(injection.kind()).thenReturn(InjectionKinds.ProjectionInjection);
//    when(injection.name()).thenReturn(projectionName);
//    when(injection.targetClass()).thenReturn(injectionType);
//    when(injection.unitClass()).thenReturn(unitClass);
//
//    UnitProjectionDefinition projectionProvider = mock(UnitProjectionDefinition.class);
//    when(projectionProvider.name()).thenReturn(projectionName);
//    when(projectionProvider.type()).thenReturn(projectionType);
//
//    KernelUnit unit = mock(KernelUnit.class);
//    when(unit.isMain()).thenReturn(true);
//    when(unit.unitClass()).thenReturn(unitClass);
//    when(unit.startupMethod()).thenReturn(Optional.empty());
//    when(unit.shutdownMethod()).thenReturn(Optional.empty());
//    when(unit.injections()).thenReturn(List.of(injection));
//    when(unit.projectionDefinitions()).thenReturn(List.of(projectionProvider));
//
//    KernelModule module = mock(KernelModule.class);
//    when(module.units()).thenReturn(List.of(unit));
//
//    // Then
//    Assertions.assertThatThrownBy(() -> moduleValidator.validate(module))
//        .isExactlyInstanceOf(ConfigurationException.class)
//        .hasMessage("Projection injection '" + projectionName + "' declared in unit " + unitClass.getCanonicalName() + " has an incompatible target type. " +
//            "Expected type " + injectionType.getCanonicalName() + ", actual type " + projectionType.getCanonicalName());
//  }
//
//  @Test
//  @SuppressWarnings("unchecked,rawtypes")
//  public void testValidate_whenValid() throws Exception {
//    // Given
//    KernelUnit unit1 = mock(KernelUnit.class);
//    Class unitClass1 = String.class;
//    when(unit1.isMain()).thenReturn(true);
//    when(unit1.unitClass()).thenReturn(unitClass1);
//
//    when(unit1.startupMethod()).thenReturn(Optional.of(String.class.getDeclaredMethod("isEmpty")));
//    when(unit1.shutdownMethod()).thenReturn(Optional.of(String.class.getDeclaredMethod("isBlank")));
//    UnitProjectionDefinition projectionProvider1 = mock(UnitProjectionDefinition.class);
//    when(projectionProvider1.name()).thenReturn("projection1");
//    when(projectionProvider1.unitClass()).thenReturn(unitClass1);
//    when(projectionProvider1.projectionMethod()).thenReturn(String.class.getDeclaredMethod("trim"));
//    when(unit1.projectionDefinitions()).thenReturn(List.of(projectionProvider1));
//
//    KernelUnit unit2 = mock(KernelUnit.class);
//    Class unitClass2 = Integer.class;
//    when(unit2.isMain()).thenReturn(false);
//    when(unit2.unitClass()).thenReturn(unitClass2);
//    when(unit2.shutdownAction()).thenReturn(Optional.empty());
//    UnitProjectionDefinition projectionProvider2 = mock(UnitProjectionDefinition.class);
//    when(projectionProvider2.unitClass()).thenReturn(unitClass2);
//    when(projectionProvider2.projectionMethod()).thenReturn(Integer.class.getDeclaredMethod("intValue"));
//    when(projectionProvider2.name()).thenReturn("projection2");
//    when(unit2.projectionDefinitions()).thenReturn(List.of(projectionProvider2));
//
//    KernelModule module = mock(KernelModule.class);
//    when(module.units()).thenReturn(List.of(unit1, unit2));
//
//    // Then
//    Assertions.assertThatCode(() -> moduleValidator.validate(module))
//        .doesNotThrowAnyException();
//  }
}
