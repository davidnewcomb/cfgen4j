package uk.co.bigsoft.apps.cfgen4j.cfg;

import java.io.IOException;
import java.util.Properties;

import uk.co.bigsoft.apps.cfgen4j.Main;

class Version {
	private final String PROPERTIES_KEY_VERSION_NAME = "version.name";
	private final String PROPERTIES = "cfgen4j.properties";

	public String get() {
		Properties properties = new Properties();
		try {
			properties.load(Main.class.getClassLoader().getResourceAsStream(PROPERTIES));
		} catch (IOException e) {
			return "0.0.0";
		}
		return properties.getProperty(PROPERTIES_KEY_VERSION_NAME);
	}

}
