package uk.co.bigsoft.apps.cfgen4j.gen;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameConverter {
	public String generateJavaFieldName(String name) {
		name = clean(name);
		return name;
	}

	public String generateJavaClassName(String name) {
		name = clean(name);
		name = makeFirstCharUpper(name);
		return name;
	}

	public String generateJavaMethodName(String prefix, String name) {
		name = generateJavaClassName(name);
		name = prefix + name;
		return name;
	}

	public String clean(String name) {
		name = ruleNoSpaces(name);
		name = ruleNoOnlyNumbers(name);
		name = ruleNoOnlyUpperCase(name);
		name = ruleNoNumberAtFront(name);
		name = ruleNoCapitalsAtFront(name);
		return name;
	}

	private String ruleNoOnlyUpperCase(String name) {
		String pattern = "^([A-Z]+)$";

		Pattern r = Pattern.compile(pattern);

		Matcher m = r.matcher(name);
		if (m.matches() == false) {
			return name;
		}
		return name.toLowerCase();
	}

	private String ruleNoCapitalsAtFront(String name) {
		String pattern = "^([A-Z]+)(.*)";

		Pattern r = Pattern.compile(pattern);

		Matcher m = r.matcher(name);
		if (m.matches() == false) {
			return name;
		}
		if (m.group(1).length() == 1) {
			return makeFirstCharLower(name);
		}
		String newName = m.group(1).toLowerCase();
		String rest = m.group(2);
		if (Character.isUpperCase(rest.charAt(0))) {
			newName += rest;
		} else {
			newName += makeFirstCharUpper(rest);
		}

		return newName;
	}

	private String ruleNoSpaces(String name) {
		return name.replaceAll("\\s", "");
	}

	private String ruleNoOnlyNumbers(String name) {
		String pattern = "^([0-9]+)$";

		Pattern r = Pattern.compile(pattern);

		Matcher m = r.matcher(name);
		if (m.matches() == false) {
			return name;
		}
		return "cf" + name;
	}

	private String ruleNoNumberAtFront(String name) {
		String pattern = "([0-9]+)(.*)";

		Pattern r = Pattern.compile(pattern);

		Matcher m = r.matcher(name);

		if (m.matches() == false) {
			return name;
		}

		String num = m.group(1);
		if ("".equals(num)) {
			return name;
		}
		return m.group(2) + m.group(1).trim();
	}

	private String makeFirstCharUpper(String name) {
		String newName = String.valueOf(name.charAt(0)).toUpperCase();
		if (name.length() > 1) {
			newName += name.substring(1);
		}
		return newName;
	}

	private String makeFirstCharLower(String name) {
		String newName = String.valueOf(name.charAt(0)).toLowerCase();
		if (name.length() > 1) {
			newName += name.substring(1);
		}
		return newName;
	}

}
