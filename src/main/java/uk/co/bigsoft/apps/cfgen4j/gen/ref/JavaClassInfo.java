package uk.co.bigsoft.apps.cfgen4j.gen.ref;

import java.util.ArrayList;
import java.util.List;

import com.squareup.javapoet.CodeBlock;

public class JavaClassInfo {

	private String id;
	private String className;
	private ArrayList<CodeBlock> constructorBody = new ArrayList<>();

	public JavaClassInfo() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Override
	public String toString() {
		return "JavaClassInfo{" + "id:" + id + " className:" + className + "}";
	}

	public void addConstructorBody(CodeBlock block) {
		constructorBody.add(block);
	}

	public List<CodeBlock> getConstructorBody() {
		return constructorBody;
	}
}
