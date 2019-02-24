package uk.co.bigsoft.apps.cfgen4j.gen;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.MethodSpec.Builder;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import uk.co.bigsoft.apps.cfgen4j.cf.CfCdaType;
import uk.co.bigsoft.apps.cfgen4j.gen.ref.JavaClassInfo;
import uk.co.bigsoft.apps.cfgen4j.gen.ref.Refs;

public class SupportClass {

	private JavaFileFactory javaFileFactory = new JavaFileFactory();
	private Refs refs;
	private static final String FN_CFID2JAVACLASS = "cfId2JavaClass";
	private TypeSpec.Builder classBuilder;

	public SupportClass(Refs refs) {
		this.refs = refs;
		ClassName type = ClassName.get(refs.getPackageName(), "CfGen4jType");
		TypeVariableName vtype = TypeVariableName.get("HashMap<String, Class<? extends CfGen4jType>>",
				ClassName.get(HashMap.class), type);
		FieldSpec fileSpec = FieldSpec.builder(vtype, "map", Modifier.PUBLIC, Modifier.STATIC) //
				.build();

		classBuilder = TypeSpec //
				.classBuilder("CfGen4jUtils") //
				.addModifiers(Modifier.PUBLIC) //
				.addField(fileSpec);
	}

	public void createMethodCfId2JavaClass() {

		ClassName type = ClassName.get(refs.getPackageName(), "CfGen4jType");
		TypeVariableName retn = TypeVariableName.get("Class<? extends CfGen4jType>", type);

		CodeBlock.Builder body = CodeBlock.builder() //
				.beginControlFlow("if (map == null)") //
				.addStatement("map = new $T<>()", HashMap.class);

		for (JavaClassInfo info : refs.getInfos()) {
			body.addStatement("map.put($S, $N.class)", info.getId(), info.getClassName());
		}
		body.endControlFlow() //
				.addStatement("return map.get(id)").build();

		Builder methodBuilder = MethodSpec //
				.methodBuilder(FN_CFID2JAVACLASS) //
				.addModifiers(Modifier.PUBLIC, Modifier.STATIC) //
				.addParameter(String.class, "id") //
				.addCode(body.build()) //
				.returns(retn);

		classBuilder.addMethod(methodBuilder.build());
	}

	public void createMethodGetIds() {

		StringBuilder line = new StringBuilder();
		line.append("return new String[] {");

		Set<String> ids = refs.getIds();
		for (String id : ids) {
			line.append("\"");
			line.append(id);
			line.append("\"");
			line.append(",");
		}
		if (ids.size() > 1) {
			line.deleteCharAt(line.length() - 1);
		}

		line.append("};");

		ArrayTypeName stringArray = ArrayTypeName.of(String.class);

		Builder methodBuilder = MethodSpec //
				.methodBuilder("getIds") //
				.addModifiers(Modifier.PUBLIC, Modifier.STATIC) //
				.addCode(line.toString()) //
				.returns(stringArray);

		classBuilder.addMethod(methodBuilder.build());
	}

	public void createMethodInterfaceDescriminator(ClassName interfaceName) {
		String packageName = interfaceName.packageName();
		String name = interfaceName.simpleName();
		String methodName = "load_" + name;
		ClassName exception = ClassName.get(packageName, "CfGen4jException");
		ClassName type = ClassName.get(packageName, "CfGen4jType");

		CodeBlock body = CodeBlock.builder() //
				.addStatement("String id = entry.contentType().id()") //
				.addStatement("Class<? extends $T> classDef = " + FN_CFID2JAVACLASS + "(id)", type) //
				.beginControlFlow("try") //
				// TODO replace name with type
				.addStatement("return (" + name + ") classDef.getConstructor($T.class).newInstance(entry)",
						CfCdaType.CDAEntry) //
				.endControlFlow("catch ($T e) {throw new $T(e);}//", Exception.class, exception) //
				.build();

		Builder methodBuilder = MethodSpec //
				.methodBuilder(methodName) //
				.addModifiers(Modifier.PUBLIC, Modifier.STATIC) //
				.addParameter(CfCdaType.CDAEntry, "entry") //
				.addCode(body) //
				.returns(interfaceName);

		classBuilder.addMethod(methodBuilder.build());
	}

	public void save(JavaFileHandler fileHandler, String folder, String packageName) throws IOException {
		JavaFile javaFile = javaFileFactory.create(packageName, classBuilder.build());
		fileHandler.write(javaFile, folder);
	}

}
