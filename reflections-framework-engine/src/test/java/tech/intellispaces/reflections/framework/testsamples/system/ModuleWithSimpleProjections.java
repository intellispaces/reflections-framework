package tech.intellispaces.reflections.framework.testsamples.system;

import tech.intellispaces.reflections.framework.annotation.Module;
import tech.intellispaces.reflections.framework.annotation.Projection;
import tech.intellispaces.reflections.framework.testsamples.reflection.ReflectionOfEmptyDomain;

@Module
public class ModuleWithSimpleProjections {

  @Projection
  public boolean booleanProjection1() {
    return true;
  }

  @Projection
  public byte byteProjection1() {
    return 1;
  }

  @Projection
  public Byte byteProjection2() {
    return 2;
  }

  @Projection
  public short shortProjection1() {
    return 3;
  }

  @Projection
  public Short shortProjection2() {
    return 4;
  }

  @Projection
  public int intProjection1() {
    return 5;
  }

  @Projection
  public Integer intProjection2() {
    return 6;
  }

  @Projection
  public long longProjection1() {
    return 7;
  }

  @Projection
  public Long longProjection2() {
    return (long) 8;
  }

  @Projection
  public float floatProjection1() {
    return 9.9f;
  }

  @Projection
  public Float floatProjection2() {
    return 10.1f;
  }

  @Projection
  public double doubleProjection1() {
    return 11.11;
  }

  @Projection
  public Double doubleProjection2() {
    return 12.12;
  }

  @Projection
  public char charProjection1() {
    return 'a';
  }

  @Projection
  public Character charProjection2() {
    return 'b';
  }

  @Projection
  public String stringProjection() {
    return "string";
  }

  @Projection
  public ReflectionOfEmptyDomain reflectionProjection() {
    return null;
  }
}
