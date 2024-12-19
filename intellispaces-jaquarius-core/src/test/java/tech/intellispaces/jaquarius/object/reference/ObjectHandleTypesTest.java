package tech.intellispaces.jaquarius.object.reference;

import org.junit.jupiter.api.Test;
import tech.intellispaces.jaquarius.object.handle.ObjectHandleTypes;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ObjectHandleTypes}.
 */
public class ObjectHandleTypesTest {

  @Test
  public void testOrdinal() {
    assertThat(ObjectHandleTypes.Movable.ordinal()).isEqualTo(0);
    assertThat(ObjectHandleTypes.Unmovable.ordinal()).isEqualTo(1);
    assertThat(ObjectHandleTypes.General.ordinal()).isEqualTo(2);
  }
}
