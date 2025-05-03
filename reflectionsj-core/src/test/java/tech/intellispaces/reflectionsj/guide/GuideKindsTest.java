package tech.intellispaces.reflectionsj.guide;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link GuideKinds}.
 */
public class GuideKindsTest {

  @Test
  public void testOrdinal() {
    assertThat(GuideKinds.Mapper0.ordinal()).isEqualTo(0);
    assertThat(GuideKinds.Mapper1.ordinal()).isEqualTo(1);
    assertThat(GuideKinds.Mapper2.ordinal()).isEqualTo(2);
    assertThat(GuideKinds.Mapper3.ordinal()).isEqualTo(3);
    assertThat(GuideKinds.Mapper4.ordinal()).isEqualTo(4);
    assertThat(GuideKinds.Mapper5.ordinal()).isEqualTo(5);
    assertThat(GuideKinds.Mover0.ordinal()).isEqualTo(6);
    assertThat(GuideKinds.Mover1.ordinal()).isEqualTo(7);
    assertThat(GuideKinds.Mover2.ordinal()).isEqualTo(8);
    assertThat(GuideKinds.Mover3.ordinal()).isEqualTo(9);
    assertThat(GuideKinds.Mover4.ordinal()).isEqualTo(10);
    assertThat(GuideKinds.Mover5.ordinal()).isEqualTo(11);
    assertThat(GuideKinds.MapperOfMoving0.ordinal()).isEqualTo(12);
    assertThat(GuideKinds.MapperOfMoving1.ordinal()).isEqualTo(13);
    assertThat(GuideKinds.MapperOfMoving2.ordinal()).isEqualTo(14);
    assertThat(GuideKinds.MapperOfMoving3.ordinal()).isEqualTo(15);
    assertThat(GuideKinds.MapperOfMoving4.ordinal()).isEqualTo(16);
    assertThat(GuideKinds.MapperOfMoving5.ordinal()).isEqualTo(17);
  }
}
