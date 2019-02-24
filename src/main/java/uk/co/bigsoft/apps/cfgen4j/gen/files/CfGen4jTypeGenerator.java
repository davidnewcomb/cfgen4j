package uk.co.bigsoft.apps.cfgen4j.gen.files;

import java.io.IOException;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;

import uk.co.bigsoft.apps.cfgen4j.gen.JavaFileFactory;
import uk.co.bigsoft.apps.cfgen4j.gen.JavaFileHandler;

public class CfGen4jTypeGenerator implements CfGen4jFileGenerator {

	private JavaFileFactory javaFileFactory = new JavaFileFactory();

	public CfGen4jTypeGenerator() {
	}

	@Override
	public void save(JavaFileHandler fileHandler, String packageName, String folder) throws IOException {
		TypeSpec type = init();
		JavaFile javaFile = javaFileFactory.create(packageName, type);
		fileHandler.write(javaFile, folder);
	}

	private TypeSpec init() {

		Builder klaus = TypeSpec.interfaceBuilder("CfGen4jType") //
				.addModifiers(Modifier.PUBLIC);

		return klaus.build();
	}
}
