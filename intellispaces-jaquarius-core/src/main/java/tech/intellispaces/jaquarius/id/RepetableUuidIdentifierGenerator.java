package tech.intellispaces.jaquarius.id;

import tech.intellispaces.commons.data.UuidFunctions;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.UUID;

public class RepetableUuidIdentifierGenerator implements IdentifierGenerator {
  private final byte[] bytes = new byte[16];
  private final SecureRandom random;

  public RepetableUuidIdentifierGenerator(byte[] seed) {
    random = new SecureRandom(seed);
  }

  public RepetableUuidIdentifierGenerator(String seed) {
    random = new SecureRandom(seed.getBytes(StandardCharsets.UTF_8));
  }

  public RepetableUuidIdentifierGenerator(UUID seed) {
    random = new SecureRandom(UuidFunctions.uuidToBytes(seed));
  }

  @Override
  public byte[] next() {
    random.nextBytes(bytes);
    return UuidFunctions.uuidToBytes(UUID.nameUUIDFromBytes(bytes));
  }
}
