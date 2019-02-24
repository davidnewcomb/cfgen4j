package uk.co.bigsoft.apps.cfgen4j.gen.files;

import java.io.IOException;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;

import uk.co.bigsoft.apps.cfgen4j.gen.JavaFileFactory;
import uk.co.bigsoft.apps.cfgen4j.gen.JavaFileHandler;
import uk.co.bigsoft.apps.cfgen4j.gen.PoetFactory;

public class CfGen4jExceptionGenerator implements CfGen4jFileGenerator {

	private JavaFileFactory javaFileFactory = new JavaFileFactory();
	private PoetFactory poetFactory = new PoetFactory();

	public CfGen4jExceptionGenerator() {
	}

	@Override
	public void save(JavaFileHandler fileHandler, String packageName, String folder) throws IOException {
		TypeSpec type = init();
		JavaFile javaFile = javaFileFactory.create(packageName, type);
		fileHandler.write(javaFile, folder);
	}

	private TypeSpec init() {
		CodeBlock body = CodeBlock.builder() //
				.addStatement("super(x)") //
				.build();

		MethodSpec constructor1 = poetFactory.constructor(body, ClassName.get(Throwable.class), "x");
		MethodSpec constructor2 = poetFactory.constructor(body, ClassName.get(String.class), "x");

		FieldSpec serialVersionUIDField = FieldSpec
				.builder(TypeName.LONG, "serialVersionUID", Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
				.initializer("-1").build();

		Builder klaus = TypeSpec.classBuilder("CfGen4jException") //
				.superclass(RuntimeException.class) //
				.addField(serialVersionUIDField) //
				.addMethod(constructor1) //
				.addMethod(constructor2);

		return klaus.build();
	}
}
