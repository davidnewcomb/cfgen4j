package uk.co.bigsoft.apps.cfgen4j.gen;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.MethodSpec.Builder;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

public class PoetFactory {

	private NameConverter nameConverter = new NameConverter();

	public MethodSpec constructor(List<CodeBlock> blocks, ClassName type, String name) {
		Builder constructorBuilder = MethodSpec //
				.constructorBuilder() //
				.addModifiers(Modifier.PUBLIC) //
				.addParameter(type, name);

		for (CodeBlock block : blocks) {
			constructorBuilder.addCode(block);
		}

		MethodSpec constructor = constructorBuilder.build();
		return constructor;
	}

	public MethodSpec constructor(CodeBlock block, ClassName type, String name) {
		ArrayList<CodeBlock> blocks = new ArrayList<>(1);
		blocks.add(block);
		return constructor(blocks, type, name);
	}

	public ParameterizedTypeName parameterizedList(ClassName className) {
		return ParameterizedTypeName.get(ClassName.get(List.class), className);
	}

	public FieldSpec fieldBuilder(TypeName type, String fieldName) {

		FieldSpec fileSpec = FieldSpec.builder(type, fieldName, Modifier.PRIVATE).build();
		return fileSpec;
	}

	public MethodSpec fieldGetter(FieldSpec fieldSpec) {
		String methodName = nameConverter.generateJavaMethodName("get", fieldSpec.name);
		return MethodSpec //
				.methodBuilder(methodName) //
				.addModifiers(Modifier.PUBLIC) //
				.addStatement("return $N", fieldSpec) //
				.returns(fieldSpec.type) //
				.build();
	}

	public MethodSpec fieldSetter(FieldSpec fieldSpec) {
		String methodName = nameConverter.generateJavaMethodName("set", fieldSpec.name);
		return MethodSpec //
				.methodBuilder(methodName) //
				.addModifiers(Modifier.PUBLIC) //
				.addParameter(fieldSpec.type, fieldSpec.name) //
				.addStatement("this.$N = $N", fieldSpec, fieldSpec) //
				.build();
	}

}
