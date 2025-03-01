package tech.intellispaces.jaquarius.object.reference;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ObjectHandleTypes}.
 */
public class ObjectHandleTypesTest {

  @Test
  public void testOrdinal() {
    assertThat(ObjectHandleTypes.UnmovablePureObject.ordinal()).isEqualTo(0);
    assertThat(ObjectHandleTypes.MovablePureObject.ordinal()).isEqualTo(1);
    assertThat(ObjectHandleTypes.UndefinedPureObject.ordinal()).isEqualTo(2);
    assertThat(ObjectHandleTypes.UnmovableHandle.ordinal()).isEqualTo(3);
    assertThat(ObjectHandleTypes.MovableHandle.ordinal()).isEqualTo(4);
    assertThat(ObjectHandleTypes.UndefinedHandle.ordinal()).isEqualTo(5);
  }
}
