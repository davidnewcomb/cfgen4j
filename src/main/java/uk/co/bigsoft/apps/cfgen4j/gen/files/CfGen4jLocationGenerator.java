package uk.co.bigsoft.apps.cfgen4j.gen.files;

import java.io.IOException;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;

import uk.co.bigsoft.apps.cfgen4j.cf.CfCdaType;
import uk.co.bigsoft.apps.cfgen4j.gen.JavaFileFactory;
import uk.co.bigsoft.apps.cfgen4j.gen.JavaFileHandler;
import uk.co.bigsoft.apps.cfgen4j.gen.PoetFactory;

public class CfGen4jLocationGenerator implements CfGen4jFileGenerator {

	private JavaFileFactory javaFileFactory = new JavaFileFactory();
	private PoetFactory poetFactory = new PoetFactory();

	public CfGen4jLocationGenerator() {
	}

	@Override
	public void save(JavaFileHandler fileHandler, String packageName, String folder) throws IOException {
		TypeSpec type = init();
		JavaFile javaFile = javaFileFactory.create(packageName, type);
		fileHandler.write(javaFile, folder);
	}

	private TypeSpec init() {
		ClassName cDouble = ClassName.get(Double.class);
		CodeBlock body = CodeBlock.builder() //
				.addStatement("this.lon = entry.getField(\"lon\")") //
				.addStatement("this.lat = entry.getField(\"lat\")") //
				.build();

		MethodSpec constructor = poetFactory.constructor(body, CfCdaType.CDAEntry, "entry");

		FieldSpec lon = FieldSpec.builder(cDouble, "lon", Modifier.PRIVATE).build();
		FieldSpec lat = FieldSpec.builder(cDouble, "lat", Modifier.PRIVATE).build();

		Builder klaus = TypeSpec.classBuilder("CfGen4jLocation") //
				.addField(lon) //
				.addField(lat) //
				.addMethod(constructor) //
				.addMethod(poetFactory.fieldGetter(lon)) //
				.addMethod(poetFactory.fieldGetter(lat)) //
		;
		return klaus.build();
	}
}
