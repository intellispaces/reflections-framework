package intellispaces.jaquarius.id;

import intellispaces.common.base.math.MathFunctions;

import java.security.SecureRandom;
import java.util.UUID;

public class RepetableUuidIdentifierGenerator implements IdentifierGenerator {
  private final byte[] bytes = new byte[16];
  private final SecureRandom random;

  public RepetableUuidIdentifierGenerator(String seed) {
    var uuid = UUID.fromString(seed);
    random = new SecureRandom(MathFunctions.uuidToBytes(uuid));
  }

  @Override
  public String next() {
    random.nextBytes(bytes);
    return UUID.nameUUIDFromBytes(bytes).toString();
  }
}
