package uk.co.bigsoft.apps.cfgen4j.gen.files;

import java.util.ArrayList;
import java.util.List;

public class FileGenerators {
	ArrayList<CfGen4jFileGenerator> files = new ArrayList<>();

	public FileGenerators() {
		files.add(new CfGen4jAssetGenerator());
		files.add(new CfGen4jExceptionGenerator());
		files.add(new CfGen4jLocationGenerator());
		files.add(new CfGen4jTypeGenerator());
	}

	public List<CfGen4jFileGenerator> getFileGenerators() {
		return files;
	}
}
