package uk.co.bigsoft.apps.cfgen4j.gen.files;

import java.io.IOException;
import java.util.Map;

import javax.lang.model.element.Modifier;

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

public class CfGen4jAssetGenerator implements CfGen4jFileGenerator {

	private JavaFileFactory javaFileFactory = new JavaFileFactory();
	private PoetFactory poetFactory = new PoetFactory();

	public CfGen4jAssetGenerator() {
	}

	@Override
	public void save(JavaFileHandler fileHandler, String packageName, String folder) throws IOException {
		TypeSpec type = init();
		JavaFile javaFile = javaFileFactory.create(packageName, type);
		fileHandler.write(javaFile, folder);
	}

	private TypeSpec init() {
		CodeBlock body = CodeBlock.builder() //
				.addStatement("Map<String, $T<String, Object>> file = asset.getField(\"file\")", Map.class) //
				.addStatement("Map<String, Object> details = file.get(\"details\")") //
				.addStatement("size = (Double) details.get(\"size\")") //
				.addStatement("url = asset.urlForImageWith($T.https())", CfCdaType.ImageOption) //
				.addStatement("title = asset.title()") //
				.addStatement("mimeType = asset.mimeType()") //
				.build();

		MethodSpec constructor = poetFactory.constructor(body, CfCdaType.CDAAsset, "asset");

		FieldSpec sizeField = FieldSpec.builder(Double.class, "size", Modifier.PRIVATE).build();
		FieldSpec urlField = FieldSpec.builder(String.class, "url", Modifier.PRIVATE).build();
		FieldSpec titleField = FieldSpec.builder(String.class, "title", Modifier.PRIVATE).build();
		FieldSpec mimeTypeField = FieldSpec.builder(String.class, "mimeType", Modifier.PRIVATE).build();

		Builder klaus = TypeSpec.classBuilder("CfGen4jAsset") //
				.addField(sizeField) //
				.addField(urlField) //
				.addField(titleField) //
				.addField(mimeTypeField) //
				.addMethod(constructor) //
				.addMethod(poetFactory.fieldGetter(sizeField)) //
				.addMethod(poetFactory.fieldGetter(urlField)) //
				.addMethod(poetFactory.fieldGetter(titleField)) //
				.addMethod(poetFactory.fieldGetter(mimeTypeField));

		return klaus.build();
	}
}
