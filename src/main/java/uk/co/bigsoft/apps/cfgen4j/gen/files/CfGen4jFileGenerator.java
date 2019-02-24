package uk.co.bigsoft.apps.cfgen4j.gen.files;

import java.io.IOException;

import uk.co.bigsoft.apps.cfgen4j.gen.JavaFileHandler;

public interface CfGen4jFileGenerator {

	public void save(JavaFileHandler fileHandler, String packageName, String folder) throws IOException;

}
