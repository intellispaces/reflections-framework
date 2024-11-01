package intellispaces.jaquarius.id;

import intellispaces.common.base.math.MathFunctions;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.UUID;

public class RepetableUuidIdentifierGenerator implements IdentifierGenerator {
  private final byte[] bytes = new byte[16];
  private final SecureRandom random;

  public RepetableUuidIdentifierGenerator(String seed) {
    random = new SecureRandom(seed.getBytes(StandardCharsets.UTF_8));
  }

  public RepetableUuidIdentifierGenerator(UUID seed) {
    random = new SecureRandom(MathFunctions.uuidToBytes(seed));
  }

  @Override
  public String next() {
    random.nextBytes(bytes);
    return UUID.nameUUIDFromBytes(bytes).toString();
  }
}
