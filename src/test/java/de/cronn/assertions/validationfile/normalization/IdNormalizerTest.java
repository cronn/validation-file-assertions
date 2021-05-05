package de.cronn.assertions.validationfile.normalization;

import static org.assertj.core.api.Assertions.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

class IdNormalizerTest {

	@Test
	void testExtract() {
		String input = "id=8 id=6 id=8";

		Pattern p = Pattern.compile("id=(\\p{Alnum}*)");

		Matcher m = p.matcher(input);
		assertThat(m.find()).isTrue();
		assertThat(m.group(1)).isEqualTo("8");
		assertThat(m.find()).isTrue();
		assertThat(m.group(1)).isEqualTo("6");
		assertThat(m.find()).isTrue();
		assertThat(m.group(1)).isEqualTo("8");
		assertThat(m.find()).isFalse();
	}

	@Test
	void testNormalize() {
		String input = "id=8 id=6 id=8 xid=1 id=2";
		String expected = "id=1 id=2 id=1 xid=3 id=4";
		String regex = "id=(\\p{Alnum}*)";

		assertThat(new IdNormalizer(regex).normalize(input)).isEqualTo(expected);
	}

	@Test
	void testNormalizeWithoutIdMapping() {
		String input = "id=8 id=6 id=8 xid=1 id=2";
		String expected = "id=1 id=2 id=3 xid=4 id=5";
		String regex = "id=(\\p{Alnum}*)";

		assertThat(new IdNormalizer(new IncrementingIdProvider(), "", false, regex).normalize(input)).isEqualTo(expected);
	}

	@Test
	void testNormalize2() {
		String input = "id=8 xid=6 id=8ssss id=1 id=2";
		String expected = "id=1 xid=6 id=2 id=3 id=4";
		String regex = "\\bid=(\\p{Alnum}*)";

		assertThat(new IdNormalizer(regex).normalize(input)).isEqualTo(expected);
	}

	@Test
	void testNormalizeWithTwoRegexps() {
		String input = "id=8 xid=6 id=8 id=6 id=2 xid=8";
		String expected = "id=1 xid=2 id=1 id=2 id=3 xid=1";
		String[] regex = { "\\bid=(\\p{Alnum}*)", "\\bxid=(\\p{Alnum}*)" };

		assertThat(new IdNormalizer(regex).normalize(input)).isEqualTo(expected);
	}

	@Test
	void testNormalizeCanGrowSize() {
		String input = "id=1 id=2 id=3 id=4 id=5 id=6 id=7 id=8 id=9 id=0 id=A id=B";
		String expected = "id=1 id=2 id=3 id=4 id=5 id=6 id=7 id=8 id=9 id=10 id=11 id=12";
		String regex = "\\bid=(\\p{Alnum}*)";

		assertThat(new IdNormalizer(regex).normalize(input)).isEqualTo(expected);
	}

	@Test
	void testNormalize3() {
		String input = "id=id\r\n"
			+ "id=5 id=<null> id=<null> id=<foo> id=\"id=2\" bar==id baz";
		String expected = "id=1\r\n"
			+ "id=2 id=3 id=3 id=4 id=5 bar==id baz";
		String regex = "\\bid=(\\p{Graph}+)";

		assertThat(new IdNormalizer(regex).normalize(input)).isEqualTo(expected);
	}

	@Test
	void testNormalize4() {
		String input = "foo id=id\r\nid=id=\"id=self\" bar";
		String expected = "foo id=1\r\nid=2 bar";
		String regex = "\\bid=(\\p{Graph}+)";

		assertThat(new IdNormalizer(regex).normalize(input)).isEqualTo(expected);
	}

	@Test
	void testNormalizeWithRegexpContainingTextAfterGroup() {
		String input = "id=\"8\" id=\"6\" id=\"8\"";
		String expected = "id=\"1\" id=\"2\" id=\"1\"";
		String regex = "\\bid=\"(\\p{Alnum}*)\"";

		assertThat(new IdNormalizer(regex).normalize(input)).isEqualTo(expected);
	}

	@Test
	void testNormalizeEscapedString() throws Exception {
		String input = "change: \"jw,lq9F37x/;b\\\\\" --> \"Q;!zio$E\\\"ea,y\\\\a{\", field : \"Q;!zio$E\\\"ea,y\\\\a{\"";

		// Q;!zioEea,y\\a{

		String result = new IdNormalizer(new IncrementingIdProvider(), "id-", //
			"change: \"(" + IdNormalizer.ESCAPED_STRING + ")\" --> \"" + IdNormalizer.ESCAPED_STRING + "\"", //
			"change: \"" + IdNormalizer.ESCAPED_STRING + "\" --> \"(" + IdNormalizer.ESCAPED_STRING + ")\"", //
			"field : \"(" + IdNormalizer.ESCAPED_STRING + ")\"").normalize(input);

		assertThat(result).isEqualTo("change: \"id-1\" --> \"id-2\", field : \"id-2\"");
	}
}
