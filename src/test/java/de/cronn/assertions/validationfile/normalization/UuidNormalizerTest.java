package de.cronn.assertions.validationfile.normalization;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UuidNormalizerTest {

  @Test
  void testNormalizeLowercase() {
    String input = "id=550e8400-e29b-41d4-a716-446655440000";
    String expected = "id=UUID_1";

    assertThat(new UuidNormalizer().normalize(input)).isEqualTo(expected);
  }

  @Test
  void testNormalizeUppercase() {
    String input = "id=550E8400-E29B-41D4-A716-446655440000";
    String expected = "id=UUID_1";

    assertThat(new UuidNormalizer().normalize(input)).isEqualTo(expected);
  }

  @Test
  void testNormalizeMixedCase() {
    String input = "id=550e8400-E29B-41d4-A716-446655440000";
    String expected = "id=UUID_1";

    assertThat(new UuidNormalizer().normalize(input)).isEqualTo(expected);
  }

  @Test
  void testNormalizeMultipleUuids() {
    String input =
        "a=550e8400-e29b-41d4-a716-446655440000 b=661f9511-f30c-52e5-b827-557766551111 a=550e8400-e29b-41d4-a716-446655440000";
    String expected = "a=UUID_1 b=UUID_2 a=UUID_1";

    assertThat(new UuidNormalizer().normalize(input)).isEqualTo(expected);
  }

  @Test
  void testNoUuid() {
    String input = "no uuids here";

    assertThat(new UuidNormalizer().normalize(input)).isEqualTo(input);
  }
}
