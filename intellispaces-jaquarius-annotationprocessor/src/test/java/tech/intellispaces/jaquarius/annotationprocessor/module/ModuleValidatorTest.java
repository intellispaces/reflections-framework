package tech.intellispaces.jaquarius.annotationprocessor.module;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.CompilationSubject;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import tech.intellispaces.jaquarius.annotationprocessor.SampleAnnotatedTypeProcessor;
import tech.intellispaces.jaquarius.exception.JaquariusException;
import tech.intellispaces.java.reflection.customtype.CustomType;

import javax.tools.JavaFileObject;

/**
 * Tests for {@link ModuleValidator} class.
 */
public class ModuleValidatorTest {
  private final ModuleValidator moduleValidator = new ModuleValidator();

  @Test
  public void testValidate_whenEmptyModule() {
    // Given
    CustomType moduleType = getModuleType("samples/EmptyModule.java");

    // Then
    Assertions.assertThatCode(() -> moduleValidator.validate(moduleType))
        .doesNotThrowAnyException();
  }

  @Test
  public void testValidate_whenModuleWithOneStartupMethod() {
    // Given
    CustomType moduleType = getModuleType("samples/ModuleWithOneStartupMethod.java");

    // Then
    Assertions.assertThatCode(() -> moduleValidator.validate(moduleType))
        .doesNotThrowAnyException();
  }

  @Test
  public void testValidate_whenModuleWithOneShutdownMethod() {
    // Given
    CustomType moduleType = getModuleType("samples/ModuleWithOneShutdownMethod.java");

    // Then
    Assertions.assertThatCode(() -> moduleValidator.validate(moduleType))
        .doesNotThrowAnyException();
  }

  @Test
  public void testValidate_whenTwoStartupMethods() {
    // Given
    CustomType moduleType = getModuleType("samples/ModuleWithTwoStartupMethods.java");

    // Then
    Assertions.assertThatThrownBy(() -> moduleValidator.validate(moduleType))
        .isExactlyInstanceOf(JaquariusException.class)
        .hasMessage("Module unit samples.ModuleWithTwoStartupMethods contains more that one startup methods");
  }

  @Test
  public void testValidate_whenTwoShutdownMethods() {
    // Given
    CustomType moduleType = getModuleType("samples/ModuleWithTwoShutdownMethods.java");

    // Then
    Assertions.assertThatThrownBy(() -> moduleValidator.validate(moduleType))
        .isExactlyInstanceOf(JaquariusException.class)
        .hasMessage("Module unit samples.ModuleWithTwoShutdownMethods contains more that one shutdown methods");
  }

  @Test
  public void testValidate_whenStartupMethodReturnedString() {
    // Given
    CustomType moduleType = getModuleType("samples/ModuleWithStartupMethodReturnedString.java");

    // Then
    Assertions.assertThatCode(() -> moduleValidator.validate(moduleType))
        .doesNotThrowAnyException();
  }

  @Test
  public void testValidate_whenStartupMethodAndStringParameter() {
    // Given
    CustomType moduleType = getModuleType("samples/ModuleWithStartupMethodAndStringParameter.java");

    // Then
    Assertions.assertThatCode(() -> moduleValidator.validate(moduleType))
        .doesNotThrowAnyException();
  }

  @Test
  public void testValidate_whenStartupMethodAndFileParameter() {
    // Given
    CustomType moduleType = getModuleType("samples/ModuleWithStartupMethodAndFileParameter.java");

    // Then
    Assertions.assertThatThrownBy(() -> moduleValidator.validate(moduleType))
        .isExactlyInstanceOf(JaquariusException.class)
        .hasMessage("Parameter 'value' of method 'startup' in unit samples.ModuleWithStartupMethodAndFileParameter should be object handle class");
  }

  @Test
  public void testValidate_whenShutdownMethodReturnedString() {
    // Given
    CustomType moduleType = getModuleType("samples/ModuleWithShutdownMethodReturnedString.java");

    // Then
    Assertions.assertThatCode(() -> moduleValidator.validate(moduleType))
        .doesNotThrowAnyException();
  }

  @Test
  public void testValidate_whenShutdownMethodAndStringParameter() {
    // Given
    CustomType moduleType = getModuleType("samples/ModuleWithShutdownMethodAndStringParameter.java");

    // Then
    Assertions.assertThatCode(() -> moduleValidator.validate(moduleType))
        .doesNotThrowAnyException();
  }

  @Test
  public void testValidate_whenShutdownMethodAndFileParameter() {
    // Given
    CustomType moduleType = getModuleType("samples/ModuleWithShutdownMethodAndFileParameter.java");

    // Then
    Assertions.assertThatThrownBy(() -> moduleValidator.validate(moduleType))
        .isExactlyInstanceOf(JaquariusException.class)
        .hasMessage("Parameter 'value' of method 'shutdown' in unit samples.ModuleWithShutdownMethodAndFileParameter should be object handle class");
  }

  @Test
  public void testValidate_whenAbstractMethodAndParameter() {
    // Given
    CustomType moduleType = getModuleType("samples/ModuleWithAbstractMethodAndParameter.java");

    // Then
    Assertions.assertThatThrownBy(() -> moduleValidator.validate(moduleType))
        .isExactlyInstanceOf(JaquariusException.class)
        .hasMessage("Abstract method 'projection' in unit samples.ModuleWithAbstractMethodAndParameter should have no parameters");
  }

  @Test
  public void testValidate_whenValidProjections() {
    // Given
    CustomType moduleType = getModuleType("samples/ModuleWithSimpleProjections.java");

    // Then
    Assertions.assertThatCode(() -> moduleValidator.validate(moduleType))
        .doesNotThrowAnyException();
  }

  @Test
  public void testValidate_whenProjectionWithoutReturnedType() {
    // Given
    CustomType moduleType = getModuleType("samples/ModuleWithProjectionWithoutReturnedType.java");

    // Then
    Assertions.assertThatThrownBy(() -> moduleValidator.validate(moduleType))
        .isExactlyInstanceOf(JaquariusException.class)
        .hasMessage("Method of the projection 'projection' in unit samples.ModuleWithProjectionWithoutReturnedType should return value");
  }

  @Test
  public void testValidate_whenProjectionAndStringParameter() {
    // Given
    CustomType moduleType = getModuleType("samples/ModuleWithProjectionAndStringParameter.java");

    // Then
    Assertions.assertThatCode(() -> moduleValidator.validate(moduleType))
        .doesNotThrowAnyException();
  }

  @Test
  public void testValidate_whenProjectionAndFileParameter() {
    // Given
    CustomType moduleType = getModuleType("samples/ModuleWithProjectionAndFileParameter.java");

    // Then
    Assertions.assertThatThrownBy(() -> moduleValidator.validate(moduleType))
        .isExactlyInstanceOf(JaquariusException.class)
        .hasMessage("Parameter 'value' of method 'projection' in unit samples.ModuleWithProjectionAndFileParameter should be object handle class");
  }

  @Test
  public void testValidate_whenAbstractProjection() {
    // Given
    CustomType moduleType = getModuleType("samples/ModuleWithAbstractProjection.java");

    // Then
    Assertions.assertThatThrownBy(() -> moduleValidator.validate(moduleType))
        .isExactlyInstanceOf(JaquariusException.class)
        .hasMessage("Abstract projection method 'projection' in unit samples.ModuleWithAbstractProjection should have a Projection Definition annotation");
  }

  @Test
  public void testValidate_whenAbstractProjectionAndSingleProjectionDefinitionAnnotation() {
    // Given
    CustomType moduleType = getModuleType("samples/ModuleWithAbstractProjectionAndSingleProjectionDefinitionAnnotation.java");

    // Then
    Assertions.assertThatCode(() -> moduleValidator.validate(moduleType))
        .doesNotThrowAnyException();
  }

  @Test
  public void testValidate_whenAbstractProjectionAndSingleProjectionDefinitionAnnotationAndParameter() {
    // Given
    CustomType moduleType = getModuleType("samples/ModuleWithAbstractProjectionAndSingleProjectionDefinitionAnnotationAndParameter.java");

    // Then
    Assertions.assertThatThrownBy(() -> moduleValidator.validate(moduleType))
        .isExactlyInstanceOf(JaquariusException.class)
        .hasMessage("Abstract method 'projection' in unit samples.ModuleWithAbstractProjectionAndSingleProjectionDefinitionAnnotationAndParameter should have no parameters");
  }

  @Test
  public void testValidate_whenUnitWithOneStartupMethod() {
    // Given
    CustomType moduleType = getModuleType("samples/UnitWithOneStartupMethod.java");

    // Then
    Assertions.assertThatThrownBy(() -> moduleValidator.validate(moduleType))
        .isExactlyInstanceOf(JaquariusException.class)
        .hasMessage("Included unit should not have a starting method. But method 'startup' in unit samples.UnitWithOneStartupMethod.IncludedUnit is marked with annotation @Startup");
  }

  @Test
  public void testValidate_whenUnitWithOneShutdownMethod() {
    // Given
    CustomType moduleType = getModuleType("samples/UnitWithOneShutdownMethod.java");

    // Then
    Assertions.assertThatThrownBy(() -> moduleValidator.validate(moduleType))
        .isExactlyInstanceOf(JaquariusException.class)
        .hasMessage("Included unit should not have a shutdown method. But method 'shutdown' in unit samples.UnitWithOneShutdownMethod.IncludedUnit is marked with annotation @Shutdown");
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
