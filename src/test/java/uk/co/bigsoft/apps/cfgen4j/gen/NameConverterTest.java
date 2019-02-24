package uk.co.bigsoft.apps.cfgen4j.gen;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NameConverterTest {

	private NameConverter subject = new NameConverter();

	@Test
	public void numbers() {
		assertEquals("cf123", subject.generateJavaFieldName("123"));
		assertEquals("xxx123", subject.generateJavaFieldName("123xxx"));
		assertEquals("zzz123", subject.generateJavaFieldName("123 zzz"));
		assertEquals("cf123", subject.generateJavaFieldName("12 3 "));
		assertEquals("x312", subject.generateJavaFieldName("12 x3 "));

		assertEquals("Cf123", subject.generateJavaClassName("123"));
		assertEquals("Xxx123", subject.generateJavaClassName("123xxx"));
		assertEquals("Zzz123", subject.generateJavaClassName("123 zzz"));
		assertEquals("Cf123", subject.generateJavaClassName("12 3 "));
		assertEquals("X312", subject.generateJavaClassName("12 x3 "));

	}

	@Test
	public void spaces() {
		assertEquals("x", subject.generateJavaFieldName(" x "));
		assertEquals("x", subject.generateJavaFieldName(" x"));
		assertEquals("x", subject.generateJavaFieldName("x "));

		assertEquals("X", subject.generateJavaClassName(" x "));
		assertEquals("X", subject.generateJavaClassName(" x"));
		assertEquals("X", subject.generateJavaClassName("x "));
	}

	@Test
	public void caps() {
		assertEquals("xxx", subject.generateJavaFieldName("xxx"));
		assertEquals("xxx", subject.generateJavaFieldName("XXX"));
		assertEquals("xxx123", subject.generateJavaFieldName("XXX123"));
		assertEquals("xxxHello", subject.generateJavaFieldName("XXXhello"));

		assertEquals("Xxx", subject.generateJavaClassName("xxx"));
		assertEquals("Xxx", subject.generateJavaClassName("XXX"));
		assertEquals("Xxx123", subject.generateJavaClassName("XXX123"));
		assertEquals("XxxHello", subject.generateJavaClassName("XXXhello"));

	}

	@Test
	public void others() {

		assertEquals("MenuItem", subject.generateJavaClassName("menuItem"));
		assertEquals("EventTile", subject.generateJavaClassName("eventTile"));
		assertEquals("OpeningTimes", subject.generateJavaClassName("openingTimes"));

	}

	@Test
	public void othersDone() {

		assertEquals("hello", subject.generateJavaFieldName("Hello"));
		assertEquals("helloWorld", subject.generateJavaFieldName("HelloWorld"));

		assertEquals("Hello", subject.generateJavaClassName("Hello"));
		assertEquals("HelloWorld", subject.generateJavaClassName("HelloWorld"));

	}

}
