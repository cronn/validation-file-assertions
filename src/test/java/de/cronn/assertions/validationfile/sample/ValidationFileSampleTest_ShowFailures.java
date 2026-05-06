package de.cronn.assertions.validationfile.sample;

import de.cronn.assertions.validationfile.normalization.ValidationNormalizer;
import org.junit.jupiter.api.Disabled;

@Disabled("Enable to demonstrate how fails work")
public class ValidationFileSampleTest_ShowFailures extends ValidationFileSampleTest {

  @Override
  public String getTestName() {
    return super.getTestName().replace("_ShowFailures", "");
  }

  @Override
  public void assertWithFile(
      String actualOutput, String filename, ValidationNormalizer normalizer) {
    actualOutput = "extra text\n" + actualOutput;
    super.assertWithFile(actualOutput, filename, null);
  }
}
